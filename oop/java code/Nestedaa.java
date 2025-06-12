class Outer {
    private int a;
    Outer(int x) { a = x; }
    public void show() { System.out.println("Outer: " + a);}
    public void showInner(int x) {
    Inner inner = new Inner(x);
    inner.show();
    }
    public class Inner {
    private int b;
    Inner(int y) { b = y; }
    public void show() { System.out.println("Inner: " + b); }
    public void showOuter(Outer ob) { ob.show(); }
    };
    public class Inner2 {
    public void show(String str) { System.out.println(str);}
    };
    Inner2 inner2 = new Inner2();
    };
    public class Nestedaa{
        public static void main(String[] args) {
        Outer outer = new Outer(10);
        outer.show();
        outer.showInner(20);
        Outer.Inner inner = outer.new Inner(30);
        inner.show();
        inner.showOuter(outer);
        outer.inner2.show("Hello from Inner2!");
        }
        }
