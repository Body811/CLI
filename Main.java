import java.io.File;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
            Scanner obj =new Scanner(System.in);
            Terminal t = new Terminal();
            while(true){
                System.out.println(t.getCurrentDirectory()+"> ");
                String input =obj.nextLine();
                t.parse(input);
                t.chooseCommandAction();

            }

    }
}
