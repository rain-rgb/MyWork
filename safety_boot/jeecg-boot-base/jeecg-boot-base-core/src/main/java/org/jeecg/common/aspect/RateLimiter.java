package org.jeecg.common.aspect;

import org.jeecg.common.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

@Component
public class RateLimiter {
    private static final String REDIS_HOST = "10.8.0.130";  // Redis 服务地址
    private static final int REDIS_PORT = 6379;           // Redis 服务端口
    private Jedis jedis;



    public RateLimiter() {
        jedis = new Jedis(REDIS_HOST, REDIS_PORT);
    }

    /**
     * 检查用户是否超过访问频率限制
     *
     * @param userId 用户 ID 或者 IP 地址
     * @return true 如果未超过请求次数，false 如果超过请求次数
     */
    public boolean isAllowed(String userId,int MAX_REQUESTS,int WINDOW_TIME) {
        String key = "request_count:" + userId;
        long currentTime = System.currentTimeMillis() / 1000; // 当前时间（单位：秒）
        String windowKey = key + ":" + currentTime / WINDOW_TIME; // 窗口key，按窗口时间划分

        try {
            // 获取当前窗口的请求次数
            String count = ObjectUtils.isEmpty(jedis.get(windowKey)) ? null:(String) jedis.get(windowKey);
            int requestCount = count == null ? 0 : Integer.parseInt(count);

            if (requestCount >= MAX_REQUESTS) {
                // 超过最大请求次数，拒绝请求
                return false;
            }

            // 否则，增加请求次数
            jedis.incr(windowKey);
            // 设置过期时间为指定的时间窗口（秒）
            jedis.expire(windowKey, WINDOW_TIME);

            return true;
        } catch (JedisException e) {
            e.printStackTrace();
            return false;
        }
    }
}
