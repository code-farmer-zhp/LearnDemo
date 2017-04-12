package redis;


import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Tuple;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RedisDelayTask {

    private JedisCluster jedisCluster;

    public RedisDelayTask(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public void addDelayTask(Object info, double executeTime) {
        String id = UUID.randomUUID().toString();
        DelayTask delayTask = new DelayTask();
        delayTask.setId(id);
        delayTask.setData(info);
        jedisCluster.zadd("delaytaskDemo", executeTime, JSONObject.toJSONString(delayTask));

    }

    /**
     * 后台一个任务 一直找任务
     */
    public void start() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new SynchronousQueue<>());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    Set<Tuple> delaytask = jedisCluster.zrangeWithScores("delaytaskDemo", 0, 0);
                    if (delaytask.size() > 0) {
                        for (Tuple tuple : delaytask) {
                            double score = tuple.getScore();
                            String key = tuple.getElement();
                            double resTime = score - System.currentTimeMillis();
                            if (resTime <= 0) {
                                JSONObject jsonObject = JSONObject.parseObject(key);
                                String id = jsonObject.getString("id");
                                //TODO 用分布式锁 对 id 加锁
                                //从延迟队列中移除
                                Long rem = jedisCluster.zrem("delaytaskDemo", key);
                                if (rem != null && rem > 0) {
                                    //放入合适的队列中去处理
                                    jedisCluster.rpush("taskqueue", key);
                                    System.out.println(key);
                                }
                            }
                        }
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        //ignore
                    }
                }
            }
        });

    }

    public static void main(String[] args) {
        HostAndPort hostAndPort1 = new HostAndPort("10.202.249.175", 20621);
        HostAndPort hostAndPort2 = new HostAndPort("10.202.249.176", 20622);
        HostAndPort hostAndPort3 = new HostAndPort("10.202.249.177", 20622);
        Set<HostAndPort> set = new HashSet<>();
        set.add(hostAndPort1);
        set.add(hostAndPort2);
        set.add(hostAndPort3);
        JedisCluster jedisCluster = new JedisCluster(set);
        RedisDelayTask redisDelayTask = new RedisDelayTask(jedisCluster);
        redisDelayTask.start();
        for (int i = 0; i < 10; i++) {
            long time = System.currentTimeMillis() + new Random().nextInt(10000);
            redisDelayTask.addDelayTask("good", time);
        }
    }

}
