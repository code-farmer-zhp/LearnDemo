package com.zhp.thrift;

import org.apache.thrift.TException;

public class MySecondServiceHandler implements MySecondService.Iface {
    @Override
    public void log(String msg) throws TException {

    }

    @Override
    public int muilty(int x, int y) throws TException {
        return 0;
    }

    @Override
    public int getSize(String fileName) throws MyError, TException {
        return 0;
    }

    @Override
    public int divide(int num1, int num2) throws DivisionByZeroError, WrongTypeError, TException {
        try {
            return num1 / num2;
        }catch (Exception e){
            throw new DivisionByZeroError(e.getMessage());
        }
    }
}
