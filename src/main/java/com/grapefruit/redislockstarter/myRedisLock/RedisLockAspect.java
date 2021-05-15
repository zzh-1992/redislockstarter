package com.grapefruit.redislockstarter.myRedisLock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Grapefruit
 * @version 1.0
 * @date 2021/4/26
 */
@Component
@Aspect
public class RedisLockAspect {
    @Autowired
    private Jedis jedis;

    private static final String LOCK = "lock";

    /**
     * 使用标记注解定义pointCut
     */
    @Pointcut(value = "@annotation(com.grapefruit.redislockstarter.myRedisLock.RedisLock)")
    public void redisLockPointCut(){}

    /**
     * 定义环绕方法
     */
    @Around(value = "redisLockPointCut()")
    public Object handle(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取LOCK
        
        // 获取ttl
        int ttl = getTTL(joinPoint);
        long begin = System.currentTimeMillis();
        Timer timer = new Timer();
        // 启动定时器
        myTimer(jedis,LOCK,timer,ttl);

        Object proceed = null;
        if(tryLock(ttl)){
            // 执行业务方法
            proceed = joinPoint.proceed();
        }

        // 任务结束删除琐,取消定时器
        jedis.del(LOCK);
        timer.cancel();
        System.out.println("time:" + (System.currentTimeMillis() - begin));
        return proceed;
    }

    // 尝试获取锁
    private boolean tryLock(int secondsToExpire){
        SetParams params = new SetParams();
        params.nx();
        params.ex(secondsToExpire);
        String set = jedis.set(LOCK, "",params);
        return "OK".equals(set);
    }

    // 获取ttl
    public int getTTL(ProceedingJoinPoint joinPoint){
        String methodName = joinPoint.getSignature().getName();
        Class<?> targetClass = joinPoint.getTarget().getClass();
        Optional<Method> optionalMethod = Arrays.stream(targetClass.getDeclaredMethods()).filter(o -> o.getName().equals(methodName)).findFirst();
        return optionalMethod.get().getAnnotation(RedisLock.class).ttl();
    }

    // 定时器
    private void myTimer(Jedis jedis, String lock , Timer timer,int ttl){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if(jedis.exists(lock)){
                        jedis.expire(lock,ttl);
                        System.out.println("===redis续命====>" + LocalDateTime.now());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, (long) ttl * 1000 * 2 / 3, (long) ttl * 1000 * 2 / 3);
    }
}
