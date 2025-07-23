package org.jeecg.common.aspect;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.jeecg.common.aspect.annotation.RateLimited;
import org.jeecg.common.exception.RateLimitExceededException;
import org.jeecg.common.aspect.RateLimiter;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.IPUtils;
import org.jeecg.common.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class RateLimiterAspect {
    @Autowired
    public RedisTemplate redisTemplate;

    private RateLimiter rateLimiter = new RateLimiter();

    @Before("@annotation(org.jeecg.common.aspect.annotation.RateLimited)")  // 使用 RateLimited 注解来标记需要限制频率的方法
    public void checkRateLimit() {
        String userId = getUserId();
       // LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();//获取当前用户的信息  // 获取用户ID，可以是请求头、登录用户信息等
      // 获取最大请求次数和时间窗口
        int maxRequests = 5;
        int timeWindow = 60;

        if (!rateLimiter.isAllowed(userId, maxRequests, timeWindow)) {
            throw new RateLimitExceededException("请求频率超过5次限制");
        }
    }

    private String getUserId() {
        //获取request
        HttpServletRequest request = SpringContextUtils.getHttpServletRequest();

        // 从请求中获取用户 ID，可能来自 Session、JWT 或请求参数等
        return IPUtils.getIpAddr(request);  // 示例
    }

}
