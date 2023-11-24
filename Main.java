import java.io.File;
import java.io.IOException;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws Exception {
            Scanner obj =new Scanner(System.in);
            Terminal t = new Terminal();
            while(true){
                System.out.print(t.getCurrentDirectory()+"> ");
                String input =obj.nextLine();
                t.parse(input);
                t.chooseCommandAction();

            }

    }
}
