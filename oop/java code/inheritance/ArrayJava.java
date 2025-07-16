
import java.util.Stack;

class Student{
    String name;
    public Student(String name){
        this.name=name;
    }
    public String  get(){
        return name;
    }
}
class Parent{
    static {
        System.out.println("par static");
    }
    {
        System.out.println("par in");
    }

    public Parent() {
        System.out.println("par cons");
    }
    
}
class Child extends Parent{
    static {
        System.out.println("chil static");
    }
    {
        System.out.println("chil in");
    }

    public Child() {
        System.out.println("chil cons");
    }
    
}
public class ArrayJava {
    public static void main(String[] args) {
    //   String s1 = new String("sami");
    //   s1= s1.intern();
    //   String s2 = "sami";
    //   System.out.println(s1==s2);
    new Child();
    }
}
