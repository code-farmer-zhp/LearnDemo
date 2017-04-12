package guava;


import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 避免使用null
 */
public class NotNull {
    public static String hideStrTwo(String str, int beginLen, int endLen) {
        Pattern p = Pattern.compile("(.{" + beginLen + "})(.+)(.{" + endLen + "})");
        Matcher m = p.matcher(str);

        if (m.find()) {
            String maskStr = m.group(2);
            maskStr = maskStr.replaceAll(".", "*");
            str = m.group(1) + maskStr + m.group(3);
        }
        return str;
    }


    public static void main(String[] args) {
        //Optional<Object> of = Optional.of(null); 快速失败
        //创建引用缺失的Optional实例

        System.out.println(hideStrTwo("18939560636", 3, 4));


        Optional<Object> absent = Optional.absent();
        System.out.println(absent.isPresent());

        //创建指定引用的Optional实例，若引用为null则表示缺失
        Optional<Integer> integerOptional = Optional.fromNullable(5);
        System.out.println(integerOptional.isPresent());

        //返回Optional所包含的引用，若引用缺失，则抛出java.lang.IllegalStateException
        System.out.println(integerOptional.get());


        integerOptional.or(6);

        Integer integer = MoreObjects.firstNonNull(1, 2);
        System.out.println(integer);

        Integer or = Optional.of(1).or(20);


    }
}
