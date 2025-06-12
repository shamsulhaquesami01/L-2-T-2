import java.time.LocalDate;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner obj = new Scanner(System.in);
        int a = obj.nextInt();
        System.out.println(a);
        LocalDate time = LocalDate.now();
        System.out.println(time);
    }
}
