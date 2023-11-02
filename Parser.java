import java.util.Arrays;

public class Parser {

    private String commandName;
    private String[] args;

    public void parse(String input){
        //parse the input into command and array of args
        String[] splitText = input.trim().split("\\s+");
        if(splitText.length > 0){
            commandName = splitText[0].toLowerCase();
            args = new String[splitText.length-1];
            System.arraycopy(splitText, 1, args, 0, splitText.length - 1);
        }
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }


}
