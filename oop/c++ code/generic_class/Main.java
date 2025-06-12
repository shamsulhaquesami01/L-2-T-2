
class Gen<T> {
    T ob;

    Gen(T o) {
        ob = o;
    }

    T getOb() {
        return ob;
    }

    void showType() {
        System.out.println("Type of T: " + ob.getClass().getName());
    }
}

class Stats<T extends Number> {
    T[] nums;

    Stats(T[] o) {
        nums = o;
    }

    double average() {
        double sum = 0.0;
        for (T num : nums)
            sum += num.doubleValue();
        return sum / nums.length;
    }
}

public class Main {
    public static void main(String[] args) {
        // normal generic classs code

        // Gen<Integer> iOb = new Gen<Integer>(88);
        // iOb.showType();
        // int v = iOb.getOb();
        // System.out.println("Value: " + v);
        // Gen<String> strOb = new Gen<String>("Generic Test.");
        // strOb.showType();
        // String str = strOb.getOb();
        // System.out.println("value: " + str);

        // bounded method code
        String[] inums = { "hello", "hi" };

        Stats<Integer> iOb2 = new Stats<>(inums);
        System.out.println("Integer Avg: " + iOb2.average());
        Double[] dnums = { 2.3, 3.5, 4.3, 1.6, 9.4 };
        Stats<Double> dOb = new Stats<>(dnums);
        System.out.println("Double Avg: " + dOb.average());
        // double[] str ={"One", "Two", "Three"};
        // Stats<String> strOb = new Stats<String>(str);
    }
}