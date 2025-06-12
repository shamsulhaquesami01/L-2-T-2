interface i1{
    public void f1();
}

interface i2 extends i1{
    public void f2();
  
}
abstract class Myclass implements  i1{
      void f3(){

      }
   public void f1(){
        System.out.println("a");
    }
      
}
interface i3 extends Myclass{
    
}
class Hello extends Myclass{
    void f3(){
        System.out.println("mm");
    }
   public void f1(){
        System.out.println("mm");
    }
}

public class InterFface {
    
}
