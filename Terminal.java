import java.io.File;
import java.nio.file.Paths;
import java.util.*;

public class Terminal {

    private Parser parser;
    private String currentDirectory;
    private String homeDirectory;
    private ArrayList<String> commandHistory;

    Terminal(){
        // Initializes the terminal with Parser object and sets the current directory to the system working directory
        parser = new Parser();
        homeDirectory = currentDirectory = System.getProperty("user.dir");
        commandHistory = new ArrayList<String>();
    }

    public String getCurrentDirectory() {
        return currentDirectory;
    }

    public void parse(String input){
        parser.parse(input);
    }

    public void chooseCommandAction(){
        //calls the correct function based on the command name
        String commandName = parser.getCommandName();
        String[] args = parser.getArgs();
        commandHistory.add(commandName);

        switch(commandName){
            case"echo":
                echo(args);
                break;

            case"pwd":
                pwd();
                break;

            case"cd":
                cd(args);
                break;

            case"ls":
                LS();
                break;

            case"ls-r":
                LSR();
                break;

            case"rm":
                RM();
                break;

            case"history":
                history();
                break;

            // Add implemented functions' switch cases

            case"exit":
                System.exit(0);
                break;
            default:
                System.out.println("Error: Invalid Command!");
                commandHistory.remove(commandHistory.size() -1);
                break;
        }
    }

    //Implemented Commands

    public void echo(String[] args) {
        if (args.length == 1) {
            System.out.println(args[0]);
        } else {
            System.out.println("Error: Invalid number of arguments!");
        }
    }

    public void pwd(){
        System.out.println(currentDirectory);
    }

    public void cd(String[] args){
        try {
            if (args.length > 0) {
                if (args[0].equals("..")) {
                    File parentDir = new File(currentDirectory).getParentFile();
                    if(parentDir!= null){
                        currentDirectory = parentDir.getAbsolutePath();
                    }
                } else{
                    File[] roots = File.listRoots();
                    for (File file: roots) {
                        if (args[0].startsWith(file.toString())) {
                            File newDir = new File(args[0]);
                            if (newDir != null){
                                currentDirectory = newDir.getAbsolutePath();
                            }
                        }else{
                            File newDir = new File(currentDirectory+File.separator+args[0]);
                            if(newDir != null){
                                currentDirectory = newDir.getAbsolutePath();
                            }
                        }
                    }
                }
            }else{
                currentDirectory = homeDirectory;
            }
        }catch(java.nio.file.InvalidPathException e){
            System.out.println("Error: Invalid Path!");
        }
    }

    public void LS(){
//       currentDirectory= Paths.get("").toAbsolutePath().toString();
        File directory = new File(currentDirectory);
        String files[] = directory.list();
        Arrays.sort(files);
        for (int i = 0 ; i<files.length ; i++){
            System.out.println(files[i]);
        }
    }
    public void LSR(){
//        currentDirectory= Paths.get("").toAbsolutePath().toString();
        File directory = new File(currentDirectory);
        String files[] = directory.list();
        Arrays.sort(files, Collections.reverseOrder());
        for (int i = 0 ; i<files.length ; i++){
            System.out.println(files[i]);
        }
    }

    public void RM(){
        Scanner obj = new Scanner(System.in);
        String name=obj.nextLine();
        File file = new File(name);
        file.delete();
    }
    public void history(){
        int index = 1;
        for(String command : commandHistory){
            System.out.println(String.format("%-2d      %-10s", index, command));
            index++;
        }
    }
}
