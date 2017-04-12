package divide;

/**
 * 负数除法 移位测试
 */
public class DivideTest {
    public static void main(String[] args) {
        int i = -12340;
        int num = i >> 8;
        //负数的移位 和 除法是不相等的 需要加偏移量
        System.out.println(num);
        System.out.println(i / (int) Math.pow(2, 8));

        //负数加上偏移量（1>>k -1）矫正结果
        i += 1 >> 8 - 1;
        System.out.println(i / (int) Math.pow(2, 8));
    }
}
