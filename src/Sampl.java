import java.util.Scanner;

public class Sampl {
    public static void main(String nvn[])
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("pls enter your name!");
        getName();
        getName();
    }

    static void getName()
    {
        System.out.println("your name"+getHisname());
    }

    static String getHisname()
    {
        System.out.println("enter");
        Scanner scan = new Scanner(System.in);
        return scan.nextLine();
    }
}
