package weight;

import javafx.util.Pair;

import java.util.*;

/**
 * 　平时，经常会遇到权重随机算法，从不同权重的N个元素中随机选择一个，并使得总体选择结果是按照权重分布的。如广告投放、负载均衡等。
 * <p>
 * 　如有4个元素A、B、C、D，权重分别为1、2、3、4，随机结果中A:B:C:D的比例要为1:2:3:4。
 * <p>
 * 总体思路：累加每个元素的权重A(1)-B(3)-C(6)-D(10)，则4个元素的的权重管辖区间分别为[0,1)、[1,3)、[3,6)、[6,10)。然后随机出一个[0,10)之间的随机数。落在哪个区间，则该区间之后的元素即为按权重命中的元素。
 * <p>
 * 　　实现方法：
 * <p>
 * 利用TreeMap，则构造出的一个树为:
 * 　　　　B(3)
 * 　　　 /   \
 * A(1)  D(10)
 * /
 * C(6)
 * 然后，利用treemap.tailMap().firstKey()即可找到目标元素。
 * 当然，也可以利用数组+二分查找来实现。
 */
public class WeightRandom<K, V extends Number> {

    private TreeMap<Double, K> treeMap = new TreeMap<>();

    WeightRandom(List<Pair<K, V>> list) {
        double lastWeight = 0D;
        for (Pair<K, V> pair : list) {
            lastWeight += pair.getValue().doubleValue();
            treeMap.put(lastWeight, pair.getKey());
        }
    }

    public K random() {
        double randomWeight = treeMap.lastKey() * Math.random();
        //大于随机权重的第一个key
        NavigableMap<Double, K> doubleKNavigableMap = treeMap.tailMap(randomWeight, false);
        Double key = doubleKNavigableMap.firstKey();
        return treeMap.get(key);
    }

    public static void main(String[] args) {
        List<Pair<String, Integer>> list = new ArrayList<>();
        list.add(new Pair<>("A", 1));
        list.add(new Pair<>("B", 2));
        list.add(new Pair<>("C", 3));
        list.add(new Pair<>("D", 4));

        WeightRandom<String, Integer> weightRandom = new WeightRandom<>(list);
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            String random = weightRandom.random();
            Integer sum = map.get(random);
            if (sum == null) {
                map.put(random, 1);
            } else {
                map.put(random, ++sum);
            }
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue() / 10000.0 * 100 + "%");
        }
    }
}
