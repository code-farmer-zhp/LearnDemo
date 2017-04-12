package spring.swap.source;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.TargetSource;
import org.springframework.aop.target.HotSwappableTargetSource;

import javax.sql.DataSource;


public class SwapSourceThrowsAdvice implements MethodInterceptor {

    private DataSource primary;

    private DataSource slave;

    private HotSwappableTargetSource targetSource;


    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {

            Object aThis = methodInvocation.getThis();
            if (aThis == primary) {
                System.out.println("主数据源");
                throw new RuntimeException("测试");
            } else if (aThis == slave){
                System.out.println("从数据源");
                throw new RuntimeException("测试");
            }

            return methodInvocation.proceed();
        } catch (Exception e) {
            //检测到异常 切换到另一个
            Object aThis = methodInvocation.getThis();
            if (aThis == primary) {
                targetSource.swap(slave);
            } else {
                targetSource.swap(primary);
            }
            throw e;
        }

    }


    public DataSource getPrimary() {
        return primary;
    }

    public void setPrimary(DataSource primary) {
        this.primary = primary;
    }

    public DataSource getSlave() {
        return slave;
    }

    public void setSlave(DataSource slave) {
        this.slave = slave;
    }

    public TargetSource getTargetSource() {
        return targetSource;
    }

    public void setTargetSource(HotSwappableTargetSource targetSource) {
        this.targetSource = targetSource;
    }
}
