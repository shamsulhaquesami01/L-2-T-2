package Intterface_advance;

interface A{
    default void show(){
       System.out.println("kutta");
    }
}
interface B{
    void show();
}
public class One implements firsta {
    @Override
   public  void show(){
        .super.show();
    }
    public static void main(String[] args) {
        One a = new One();
        a.show();
    }
}
