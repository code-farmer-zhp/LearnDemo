package thrift.micmiu.thrift.demo;

import org.apache.thrift.TException;


public class HelloWorldImpl implements HelloWorldService.Iface {
    @Override
    public String sayHello(String username) throws TException {
        System.out.println(username);
        return "welcome " + username;
    }
}
