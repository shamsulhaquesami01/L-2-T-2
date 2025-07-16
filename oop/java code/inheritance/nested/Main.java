package nested;
import java.net.SocketImpl;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;
interface Test{
  static int x=0;
  int g=0;
  @Override
  static void inc(){
    x=5;
  }
  
}
num_test_cases = int(sys.stdin.readline())
for _ in range(num_test_cases):
    solve()
class Box{
  double h,l,w;
  Box(){
    h=l=w=1;
  }

    public Box(double h, double l, double w) {
        this.h = h;
        this.l = l;
        this.w = w;
    }
  
};
class BoxWeight extends Box{
  double weight;

    public BoxWeight(double weight, double h, double l, double w) {
        this.h = h;
        this.l=l;
        this.w=w;
        this.weight = weight;
    }

    
  
}

public class Main{
  public static void main(String[] args) {
  //  String s1 = JOptionPane.showInputDialog(null, "Enter 1st Number: ");
  //  String s2 = JOptionPane.showInputDialog(null, "Enter 2nd Number: ");
  //  int num1 = Integer.parseInt(s1);
  //  int num2 = Integer.parseInt(s2);
  //  JOptionPane.showMessageDialog(null, "Sum :"+(num1+num2));
  
  String a = new String("hello world");
  // String x;
  // char[] buff = new char[10];
  // a.getChars(2,5,buff,0);
  // for(char f : buff){
  //   System.out.println(f+" ");
  // }
  // String res = a.replace('l', 's');
  // a.trim();
  // a=a.toUpperCase();
  // System.out.println(a);
  // System.out.println(res.trim());
  // int n = 100;
  // String sami =n+"";
  // StringTokenizer token = new StringTokenizer(a);
  // System.out.println(token.countTokens());
  // while(token.hasMoreTokens()){
  //   System.out.println(token.nextToken());
  // }
  // StringBuffer sf = new StringBuffer(a);
  // System.out.println(sf.capacity());
  // System.out.println(sf.charAt(1));
  // sf.append(" sami");
  // System.out.println(sf);
  // System.out.println(a);
  // sf.delete(1, 3);
  // System.out.println(sf);
  BoxWeight s = new BoxWeight(1, 2, 3, 4);
  Box plain = new Box();
 plain = s;
 BoxWeight sami = plain;

  }
}