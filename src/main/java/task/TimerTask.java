package task;

import java.util.*;


class CyclicArray<T> {

    private Set<T>[] array;

    private int currentIndex = 0;

    public CyclicArray(int len) {
        array = new HashSet[len + 1];
    }

    //放到上一个solt
    public int putPre(T t) {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = array.length - 1;
        }
        Set<T> ts = array[currentIndex];
        if (ts == null) {
            ts = new HashSet<>();
            array[currentIndex] = ts;
        }
        ts.add(t);
        return currentIndex;
    }

    //获得下一个solt
    public Set<T> getNex() {
        currentIndex++;
        if (currentIndex == array.length) {
            currentIndex = 0;
        }
        return array[currentIndex];
    }

    public Set<T> get(int index) {
        return array[index];
    }

    @Override
    public String toString() {
        return "CyclicArray{" +
                "array=" + Arrays.toString(array) +
                '}';
    }
}

/**
 * 时间轮算法 高效处理过期
 */
public class TimerTask {
    public static void main(String[] args) {
        CyclicArray<Integer> integerCyclicArray = new CyclicArray<>(3);
        Map<Integer, Integer> soltMap = new HashMap<>();


        //当请求到时 uuid
        Integer uuid = 1;

        Integer soltIndex = soltMap.get(uuid);
        if (soltIndex != null) {
            Set<Integer> uuids = integerCyclicArray.get(soltIndex);
            //移除
            uuids.remove(uuid);
        }
        //放入
        soltIndex = integerCyclicArray.putPre(uuid);
        //更新soltIndex
        soltMap.put(uuid, soltIndex);

        //找一个定时器 每秒扫描nex 清除超时
        Set<Integer> nex = integerCyclicArray.getNex();
        if (nex != null) {
            nex.clear();
        }


    }

}
