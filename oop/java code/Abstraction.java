
abstract  class Figure {
    double dim1, dim2;
    Figure(double a, double b){ dim1 = a; dim2 = b;}
    abstract double area();
    void show(){System.out.println("Abstract");}
    }
    class Rectangle extends Figure {
    Rectangle(double a, double b) { super(a, b);}
    double area(){ return dim1*dim2;}
    void show(){
    System.out.println("Rectangle Area: "+area());
    }
    }
    class Triangle extends Figure {
    Triangle(double a, double b) {super(a, b);}
    double area(){ return 0.5*dim1*dim2;}
    void show(){
    System.out.println("Triangle Area: "+area());
    }
    }


public class Abstraction {
    
    public static void main(String[] args) {
        Rectangle r = new Rectangle(10,7);
        Triangle t = new Triangle(10, 5);
        Figure figref;
        figref = r;
        figref.show();
        figref = t;
        figref.show();
        }
}
