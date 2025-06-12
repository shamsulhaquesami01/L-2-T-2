abstract class Animal {
    abstract public void sound();
}
class Dog extends Animal {
    public void sound() { System.out.println("Bark"); }
}
class Cat extends Animal {
    public void sound() { System.out.println("Meow"); }
}
class Sami {
    public void Sami(Object ob) {
        System.out.println(ob.getClass());
    }
}


public class Runtime {
public static void main(String[] args) {
Dog dog = new Dog();
Cat cat = new Cat();
Double length = 25.5;
Integer num = 10;
Sami pt = new Sami();
if (dog instanceof Dog)
System.out.println("Yes, Dog.");
pt.Sami(dog);
pt.Sami(cat);
Animal animal = dog;
pt.Sami(animal);
animal = cat;
pt.Sami(animal);
Object ob = length;
pt.Sami(ob);
ob = num;
pt.Sami(ob);
ob = dog;
pt.Sami(ob);
}
}
