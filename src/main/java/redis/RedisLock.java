package redis;


import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 分布式锁
 */
public class RedisLock {


    private JedisCluster jedisCluster;

    private String value;

    private static final String OK = "OK";

    private static final long defaultExpireTime = 2000;

    public RedisLock(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public void lock(String key) {
        value = UUID.randomUUID().toString();
        //不存在则添加 且设置过期时间（单位ms）
        for (; ; ) {
            String status = jedisCluster.set(key, value, "NX", "PX", defaultExpireTime);
            //获得锁
            if (OK.equals(status)) {
                break;
            }
            try {
                Thread.sleep(2);
            } catch (Exception e) {
                //ignore
            }
        }
    }

    public boolean tryLock(String key, long awitTime) {
        value = UUID.randomUUID().toString();
        long expireTime = System.currentTimeMillis() + awitTime;
        //不存在则添加 且设置过期时间（单位ms）
        while (System.currentTimeMillis() < expireTime) {
            String status = jedisCluster.set(key, value, "NX", "PX", defaultExpireTime);
            //获得锁
            if (OK.equals(status)) {
                return true;
            }
            try {
                Thread.sleep(2);
            } catch (Exception e) {
                //ignore
            }
        }
        return false;
    }

    public boolean tryLock(String key) {
        value = UUID.randomUUID().toString();
        //不存在则添加 且设置过期时间（单位ms）
        String status = jedisCluster.set(key, value, "NX", "PX", defaultExpireTime);
        //获得锁
        return OK.equals(status);
    }

    public void unlock(String key) {
        String cacheValue = jedisCluster.get(key);
        //TODO 非原子性的  有风险 可能刚好过期然后造成误删除其他获得锁的value
        //可以使用lua脚本解决
        //如果是单台redis 可以使用watch
        if (value.equals(cacheValue)) {
            jedisCluster.del(key);
        } else {
            throw new IllegalStateException("锁过期");
        }
    }

    public static void main(String[] args) {
        //10.202.249.175:2181
        //url:10.202.249.175:20621
        //url:10.202.249.176:20622
        //url:10.202.249.177:20622
        HostAndPort hostAndPort1 = new HostAndPort("10.202.249.175", 20621);
        HostAndPort hostAndPort2 = new HostAndPort("10.202.249.176", 20622);
        HostAndPort hostAndPort3 = new HostAndPort("10.202.249.177", 20622);
        Set<HostAndPort> set = new HashSet<>();
        set.add(hostAndPort1);
        set.add(hostAndPort2);
        set.add(hostAndPort3);
        JedisCluster jedisCluster = new JedisCluster(set);

    }
}
