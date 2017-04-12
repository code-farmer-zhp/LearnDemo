package spring.placeholder;


import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.Properties;

public class PlaceholderZhp {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]{"classpath*:applicationContext_zhp.xml"}, false);
        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setLocation(new ClassPathResource("application.properties"));
        applicationContext.addBeanFactoryPostProcessor(propertyPlaceholderConfigurer);

        //ConfigerZhp bean = applicationContext.getBean(ConfigerZhp.class);
        applicationContext.refresh();
        ConfigerZhp bean = applicationContext.getBean(ConfigerZhp.class);
        System.out.println(bean.getName());

        Properties properties = new Properties();
        properties.setProperty("zhpname", "zhoupeng");
        propertyPlaceholderConfigurer.setProperties(properties);
        propertyPlaceholderConfigurer.setLocalOverride(true);

        applicationContext.refresh();
        bean = applicationContext.getBean(ConfigerZhp.class);
        System.out.println(bean.getName());

    }
}
