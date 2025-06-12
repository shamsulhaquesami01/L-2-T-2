
interface InterfaceDemo {
    public double PI = 3.14159;

    public void volume();

    default double circleArea(double radius) {
        System.out.println();
        return PI * radius * radius;
    }
}
    interface Animal {
        public final int l = 90;

        void eat();

        String makesound();
    }

    class Dog implements Animal ,InterfaceDemo {
        @Override
        public void volume(){
        }
        @Override
        public void eat() {

            System.out.println("dog eats bones");
        }
         @Override
        public String makesound() {
            return "barkl";
        }
    }

public class Interface {
    public static void main(String[] args) {
        Dog d = new Dog();
        d.eat();
        System.out.println(d.makesound());
    }
}
