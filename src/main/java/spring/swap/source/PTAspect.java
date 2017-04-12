package spring.swap.source;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.StopWatch;

@Aspect
public class PTAspect {

    @Pointcut("execution(public void spring.swap.source.NestableInvocationBO.method1())")
    public void method1() {
    }

    @Pointcut("execution(public void spring.swap.source.NestableInvocationBO.method2())")
    public void method2() {
    }

    @Pointcut("method1()||method2()")
    public void compositePc() {
    }


    @Around("compositePc()")
    public Object pt(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopwatch = new StopWatch();
        try {
            stopwatch.start();
            return joinPoint.proceed();
        } finally {
            stopwatch.stop();
            System.out.println(stopwatch.toString());

        }
    }
}
