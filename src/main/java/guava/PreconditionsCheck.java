package guava;

import com.google.common.base.Preconditions;

/**
 * Created by peng.zhou1 on 2016/12/2.
 */
public class PreconditionsCheck {
    public static void main(String[] args) {
        Preconditions.checkArgument(1 < 2, "errormessage");

        Integer integer = Preconditions.checkNotNull(2);

        Preconditions.checkState(true, "not true");

        //检查index作为索引值对某个列表、字符串或数组是否有效。index>=0 && index<size *
        Preconditions.checkElementIndex(1, 2);

        Preconditions.checkPositionIndex(1, 2);

        Preconditions.checkPositionIndexes(1, 2, 3);
    }
}
