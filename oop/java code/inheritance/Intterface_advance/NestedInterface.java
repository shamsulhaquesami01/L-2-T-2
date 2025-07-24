package Intterface_advance;
interface outer{
    void show();
    interface inner{
        void display();
    }
}
class prothom implements outer{
    @Override
    public void show(){
        System.out.println("shudhu outer ke korsi");
//    }
//    @Override
//    public void display(){
//
//    }
}
    }
class ditiyo implements outer.inner{
//    @Override
//    public void show(){
//        System.out.println("hello");
//    }
    @Override
    public void display(){
        System.out.println("shudhu inner ke korsi");
    }
}
class tritiyo implements outer, outer.inner{

    @Override
    public void show() {
        System.out.println("duiatare korsi-1");
    }
    @Override
    public void display(){
        System.out.println("duiatare korsi-2");
    }
}
public class NestedInterface {
    public static void main(String[] args) {
        prothom p = new prothom();
        p.show();
        ditiyo d = new ditiyo();
        d.display();
        tritiyo t =new tritiyo();
        t.display();t.show();
    }
}
