package ru.fefelov.summer.market.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class StatisticsWatcher {
    private static final Map<String, Long> statistics = new HashMap<>();


    @Around("execution(public * ru.geekbrains.summer.market.services.*Service.*(..))")
    public Object methodProfiling(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long begin = System.currentTimeMillis();
        Object out = proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();
        long duration = end - begin;
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        addToStatistics(signature.getDeclaringTypeName(), duration);
        return out;
    }

    private void addToStatistics(String serviceName, Long duration){
        if (statistics.containsKey(serviceName)){
            duration += statistics.get(serviceName);
        }
        statistics.put(serviceName, duration);
        System.out.println(statistics.values());
    }

    public static Map <String, Long> getStatistics(){
        return statistics;
    }

}
