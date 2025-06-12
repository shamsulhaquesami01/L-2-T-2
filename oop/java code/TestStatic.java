public class TestStatic {
        int as[];
        int sd =10;
        static int a=3, b;
        int c;
        {
        c=10;
        }
        static {
        b=a*4;
        c=b;
        }
        int f2() {
        return a*b;
        }
        static void f1(int x) { 
        System.out.println("x = " + x);
        System.out.println("a = " + a); 
        System.out.println("b = " + b); 
        System.out.println("c = " + c);
        }
        public static void main (String [] args) {
             f1(42);
        System.out.println("b = " + b);
         System.out.println("Area = " + f2());
        }
    
}
