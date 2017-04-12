package guava;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

/**
 * Created by peng.zhou1 on 2016/12/2.
 */
public class ObjectMethods {
    private int aString;
    private int anInt;
    private int anEnum;

    public int compareTo(ObjectMethods that) {
        return ComparisonChain.start()
                .compare(this.aString, that.aString)
                .compare(this.anInt, that.anInt)
                .compare(this.anEnum, that.anEnum, Ordering.natural().nullsLast())
                .result();
    }

    public static void main(String[] args) {
        Objects.equal(1, 2);
        Objects.hashCode(1);

        MoreObjects.toStringHelper(ObjectMethods.class).toString();
    }
}
