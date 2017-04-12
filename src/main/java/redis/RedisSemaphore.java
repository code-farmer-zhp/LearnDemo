package redis;


import redis.clients.jedis.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 分布式信号量
 */
public class RedisSemaphore {
    private String value;
    private JedisPool jedisPool;

    public RedisSemaphore(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    //适合单个机器  或机器上的时间一致
    public boolean acquireSemaPhore_V1(String key, int limit, int timeout) {
        value = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        Jedis resource = jedisPool.getResource();
        try {
            Pipeline pipelined = resource.pipelined();
            pipelined.multi();
            //清除过期的信号量持有者
            pipelined.zremrangeByScore(key, "-inf", (now - timeout) + "");

            //获取信号量
            //向有序集合里面添加
            pipelined.zadd(key, now, value);
            //获得排名
            pipelined.zrank(key, value);
            Response<List<Object>> exec = pipelined.exec();
            try {
                pipelined.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            List<Object> list = exec.get();
            long rank = (long) list.get(list.size() - 1);
            //获取成功
            if (rank < limit) {
                return true;
            }
            //获取失败删除添加的标识符
            resource.zrem(key, value);
            return false;
        } finally {
            resource.close();
        }
    }

    public boolean releaseSemaPhore_V1(String key) {
        Jedis resource = jedisPool.getResource();
        try {
            Long zrem = resource.zrem(key, value);
            return zrem != null && zrem > 0;
        } finally {
            resource.close();
        }
    }

    public boolean acquireFairSemaphore(String key, int limit, int timeout) {
        value = UUID.randomUUID().toString();
        String czset = key + ":owner";
        String ctr = key + ":counter";
        long now = System.currentTimeMillis();
        Jedis resource = jedisPool.getResource();
        try {
            Pipeline pipelined = resource.pipelined();
            pipelined.multi();
            //删除过期的
            pipelined.zremrangeByScore(key, "-inf", (now - timeout) + "");
            ZParams zParams = new ZParams();
            zParams.weightsByDouble(1, 0);
            pipelined.zinterstore(czset, zParams, czset, key);

            pipelined.incr(ctr);

            Response<List<Object>> exec = pipelined.exec();
            List<Object> list = exec.get();
            long counter = (long) list.get(list.size() - 1);

            pipelined.zadd(key, now, value);
            pipelined.zadd(czset, counter, value);

            pipelined.zrank(czset, value);

            Response<List<Object>> response = pipelined.exec();
            List<Object> pipeTwo = response.get();
            long rank = (long) pipeTwo.get(pipeTwo.size() - 1);

            if (rank < limit) {
                return true;
            }

            pipelined.zrem(key, value);
            pipelined.zrem(czset, value);

            pipelined.exec();
            return false;
        } finally {
            resource.close();
        }
    }

    public boolean releaseFairSemaphore(String key) {
        Jedis resource = jedisPool.getResource();
        try {
            Pipeline pipelined = resource.pipelined();
            pipelined.zrem(key, value);
            pipelined.zrem(key + ":owner", value);
            Response<List<Object>> exec = pipelined.exec();
            long remove = (long) exec.get().get(0);
            return remove > 0;
        }finally {
            resource.close();
        }
    }

    public static void main(String[] args) {
        JedisPool jedisPool = new JedisPool("10.202.249.175", 20621);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, Integer.MAX_VALUE, 1, TimeUnit.SECONDS, new SynchronousQueue<>());
        String key = "redis:semaphore";
        for (int i = 0; i < 10; i++) {
            final int index = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(index);
                        RedisSemaphore redisSemaphore = new RedisSemaphore(jedisPool);
                        boolean b = redisSemaphore.acquireSemaPhore_V1(key, 5, 10);
                        System.out.println(index);
                        if (b) {
                            System.out.println("获取成功。");
                            try {
                                Thread.sleep(2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            redisSemaphore.releaseSemaPhore_V1(key);
                        } else {
                            System.out.println("获取失败。");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executor.shutdown();
    }
}
