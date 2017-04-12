package test;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Test {
    public static List<? extends Fruits> fruits() {
        List<Apple> apples = new ArrayList<>();
        apples.add(new Apple());
        return apples;
    }

    public static void main(String[] args) {
        String str = "start";
        for (int i = 0; i < 100; i++) {
            str = str + "hello";
        }
        System.out.println(str);

        List<? extends Fruits> list = fruits();
        Fruits fruits = list.get(0);
        System.out.println(fruits);
        List<String> a = new ArrayList<String>();
        a.add("1");
        a.add("2");
        Iterator var5 = a.iterator();

       /* while(var5.hasNext()) {
            String temp = (String)var5.next();
            if("2".equals(temp)) {
                a.remove(temp);
            }
        }*/
       /* for (String temp : a) {
            if("2".equals(temp)){
                a.remove(temp);
            }
        }*/
        System.out.println(a);

        List<? super Fruits> list1 = new ArrayList<>();
        list1.add(new Apple());
        list1.add(new Fruits());

    }
}
