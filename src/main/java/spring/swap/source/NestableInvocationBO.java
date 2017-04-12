package spring.swap.source;

import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.framework.AopContext;

//exposeProxy 要设置为true
public class NestableInvocationBO {

    public void method1() {
        //获得当前代理 然后调用方法
        ((NestableInvocationBO) AopContext.currentProxy()).method2();
        //method2();
        System.out.println("method1====>");
    }

    public void method2() {
        System.out.println("method2====>");
    }

    public static void main(String[] args) {
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory(new NestableInvocationBO());
        proxyFactory.setProxyTargetClass(true);
        //设置为true AopContext.currentProxy() 方可使用
        proxyFactory.setExposeProxy(true);
        proxyFactory.addAspect(PTAspect.class);

        NestableInvocationBO proxy = proxyFactory.getProxy();
        proxy.method1();
        //proxy.method2();
    }
}
