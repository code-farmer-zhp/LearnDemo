package guava;

import java.math.BigDecimal;
import java.util.Arrays;


public class Comparators {
    private static boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        if (str.contains(".")) {
            String[] split = str.split("\\.");
            String dot = split[1];
            char[] chars = dot.toCharArray();
            for (char achar : chars) {
                if (achar != '0') {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {

        BigDecimal bigDecimal = new BigDecimal("12.10");
        String s = bigDecimal.toString();
        boolean integer = isInteger(s);
        if (integer) {
            System.out.println("integer");
            String[] split = s.split("\\.");
            BigDecimal anew = new BigDecimal(split[0] + ".0");
            System.out.println(anew);
        } else {
            System.out.println("not integer");
            char[] chars = s.toCharArray();
            System.out.println(Arrays.toString(chars));
            int index = -1;
            for (int i = chars.length - 1; i >= 0; i--) {
                System.out.println(chars[i]);
                if (chars[i] != '0') {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                s = s.substring(0, index + 1);
            }
            BigDecimal bigDecimal1 = new BigDecimal(s);
            System.out.println(bigDecimal1);
        }


    }
}
