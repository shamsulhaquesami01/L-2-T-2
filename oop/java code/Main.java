public class Main {
    static void XTest() {
        try {
            throw new RuntimeException("Runtime Exception.");
        } catch (RuntimeException e) {
            System.out.println("Caught in XTest: " + e);
            throw ; // only throw is not work
        }
    }

    public static void main(String[] args) {
        try {
            XTest();
        } catch (RuntimeException e) {
            System.out.println("Caught in main: " + e.getMessage());
        }
    }
}