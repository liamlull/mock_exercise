package sales;

public class ClassToBeMocked {
    public String f3() {
        return "f3";
    }

    public String f4() {
        System.out.println(f3());
        return "f4";
    }
}
