class StaticDemo {
    static int a = 3;
    static int b;
    static int c;
    static void show(int x) {
    a++; b--;
    System.out.print("A: " + a + " ");
    System.out.print("B: " + b + " ");
    System.out.print("C: " + c + " ");
    System.out.println("X: " + x);
    }
    static {
    System.out.println("Static is Initialized.");
    b = a * 4;
    }
    }
public class Staticsami{
    public static void main(String[] args) {
        StaticDemo sd = new StaticDemo();
        StaticDemo.c = 5;
        for (int i = 0; i < 2; ++i) {
        StaticDemo.show(40 + i);
        }
        // show(10);
        StaticDemo.show(10);
        System.out.println("B:"+ StaticDemo.b);
        }
}
