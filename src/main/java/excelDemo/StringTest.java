package excelDemo;

import javax.jws.WebParam;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

class A {
    public String str;
}

public class StringTest<T, K> implements Serializable {
    @SuppressWarnings("aaabb")
    public static void set(@WebParam A a) throws Exception {
        a.str = "a";
    }

    public static void main(String[] args) throws Exception {
        A a = new A();
        set(a);
        System.out.println(a.str);
        NumberFormat nf = new DecimalFormat(",###.##");
        String format = nf.format(new BigDecimal(39));
        System.out.println(format);
        Set<String> set = new HashSet<>();
        int[] ints = new int[8];
        A[] as = new A[2];
        set.add("ababbb");
    }
}
