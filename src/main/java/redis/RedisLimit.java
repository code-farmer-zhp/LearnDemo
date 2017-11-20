package redis;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RedisLimit {

    private JedisCluster jedisCluster;

    private String key;

    /**
     * 限制次数
     */
    private int limit;

    /**
     * 限制时间 毫秒
     */
    private long expire;

    private static final String LUA_SIMPLE_CHECK = " local size = redis.call('get',KEYS[1]) " +
            " if size and tonumber(size) >= tonumber(ARGV[1]) then " +
            "    return 1 " +
            " end " +
            " local incr = redis.call('incr',KEYS[1]) " +
            " if incr == 1 then " +
            "    redis.call('pexpire',KEYS[1],ARGV[2]) " +
            " end " +
            " return 0 ";

    private static String luaSimpleCheckSha;

    private static final String LUA_PRECISE_CHECK = " local size = redis.call('llen',KEYS[1]) " +
            " local timeNow = tonumber(ARGV[3])" +
            " if size >= tonumber(ARGV[1]) then " +
            "    local tiemOld = redis.call('lpop',KEYS[1]) " +
            "    redis.call('rpush',KEYS[1],timeNow) " +
            "    if timeNow - tonumber(tiemOld) < tonumber(ARGV[2]) then" +
            "       return 1 " +
            "    else " +
            "       return 0" +
            "    end " +
            " end " +
            " redis.call('rpush',KEYS[1],timeNow)" +
            " return 0";

    private static String luaPreciseCheckSha;


    public RedisLimit(JedisCluster jedisCluster, String key, int limit, int expire) {
        this.jedisCluster = jedisCluster;
        this.key = key;
        this.limit = limit;
        this.expire = expire;
    }

    /**
     * 在给定的时间内限制访问多少次。如果超过了则返回true
     * 简单判断不够准确 优点存储数据量低
     * 这些操作不是原子性的
     */
    public boolean simpleCheck() {
        String sizeStr = jedisCluster.get(key);
        if (sizeStr != null && Long.valueOf(sizeStr) >= limit) {
            return true;
        }
        Long incr = jedisCluster.incr(key);
        if (incr == 1) {
            jedisCluster.pexpire(key, expire);
        }
        return false;
    }

    /**
     * 在给定的时间内限制访问多少次。如果超过了则返回true
     * 简单判断不够准确 优点存储数据量低
     * 通过lua实现原子性操作
     */
    public boolean simpelCheckByLua() {
        if (luaSimpleCheckSha == null) {
            luaSimpleCheckSha = jedisCluster.scriptLoad(LUA_SIMPLE_CHECK, "luaSimpleCheck");
        }
        Long eval;
        try {
            eval = (Long) jedisCluster.evalsha(luaSimpleCheckSha, Arrays.asList(key),
                    Arrays.asList(String.valueOf(limit), String.valueOf(expire)));
        } catch (Exception e) {
            //TODO 打印日志
            e.printStackTrace();
            eval = (Long) jedisCluster.eval(LUA_SIMPLE_CHECK, Arrays.asList(key),
                    Arrays.asList(String.valueOf(limit), String.valueOf(expire)));
        }

        return eval == 1;
    }

    /**
     * 精确判断 缺点是存储数据量多 依赖服务器时间戳
     */
    public boolean preciseCheck() {
        Long card = jedisCluster.llen(key);
        if (card >= limit) {
            //移除最早访问时间
            String timeOld = jedisCluster.lpop(key);
            //当前访问时间
            long timeNow = System.currentTimeMillis();
            jedisCluster.rpush(key, String.valueOf(timeNow));
            if (timeNow - Long.valueOf(timeOld) < expire) {
                return true;
            } else {
                return false;
            }
        }
        long timeNow = System.currentTimeMillis();
        jedisCluster.rpush(key, String.valueOf(timeNow));
        return false;
    }

    public boolean preciseCheckByLua() {
        if (luaPreciseCheckSha == null) {
            luaPreciseCheckSha = jedisCluster.scriptLoad(LUA_PRECISE_CHECK, "luaPreciseCheck");
        }
        Long eval;
        try {
            eval = (Long) jedisCluster.evalsha(luaPreciseCheckSha, Arrays.asList(key),
                    Arrays.asList(String.valueOf(limit), String.valueOf(expire), String.valueOf(System.currentTimeMillis())));

        } catch (Exception e) {
            //TODO 打印日志
            e.printStackTrace();
            eval = (Long) jedisCluster.eval(LUA_PRECISE_CHECK, Arrays.asList(key),
                    Arrays.asList(String.valueOf(limit), String.valueOf(expire), String.valueOf(System.currentTimeMillis())));
        }
        return eval == 1;

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

        RedisLimit redisLimit = new RedisLimit(jedisCluster, "localhostzhpincr", 10, 100);
        for (int i = 0; i < 50; i++) {
            if (redisLimit.simpelCheckByLua()) {
                System.out.println("超过限制");
            } else {
                System.out.println("可以执行");
            }
        }
    }
}
