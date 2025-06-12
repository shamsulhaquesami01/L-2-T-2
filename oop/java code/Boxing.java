public class Boxing {
    public static void main(String[] args) {
        Integer iOb1 = Integer.valueOf(100); //iob1 = 100
        Integer iOb2 = 150;  // i0b2 = 150
        double num1 = iOb1.doubleValue(); // 100.0
        int num2 = iOb2.intValue();  //150
        double sum1 = num1 + num2;  //100.0+150 = 250.0
        int sum2 = iOb1 + iOb2;   //100+150 = 250
        double num = iOb1.doubleValue(); //100.0
        System.out.println("Primitive Sum: " + sum1);
        System.out.println("Object sum: " + sum2);
        // etar + diye concetanate
        System.out.println("Primitive Sum: " + num1 + num2); //100.0150
        System.out.println("Object sum: " + iOb1 + iOb2);  //100150
        System.out.println(num); //100.0
    }
}
