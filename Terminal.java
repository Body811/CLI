import java.io.*;
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

    public void chooseCommandAction() throws Exception{
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

            case"touch":
                touch(args);
                break;
            case"cat":
                cat(args);
                break;
            case"wc":
                wc(args);
                break;
            case"mkdir":
                mkdir(args);
                break;
            case"rmdir":
                rmdir(args);
                break;
            case"cp":
                cp(args);
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

    public void touch(String[] args){
        File file = new File(args[0]);
        try {
            file.createNewFile();
        }
        catch (IOException e){
        }
    }

    public void cat(String[] args){
        try {
            File file=new File(args[0]);
            FileInputStream fis=new FileInputStream(file);
            int r=0;
            while((r=fis.read())!=-1) {
                System.out.print((char)r);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }System.out.print('\n');
        if (args.length==2) {
            try {
                File file=new File(args[1]);
                FileInputStream fis=new FileInputStream(file);
                int r=0;
                while((r=fis.read())!=-1) {
                    System.out.print((char)r);
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            System.out.print('\n');
        }
    }

    public void wc(String[] args) throws IOException {
        int charCount = 0, wordCount = 0,lineCount = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            String currentLine = reader.readLine();
            while (currentLine != null) {
                lineCount++;
                String[] words = currentLine.split(" ");
                wordCount = wordCount + words.length;
                for (String word : words) {
                    charCount = charCount + word.length();
                }
                currentLine = reader.readLine();
            }
            System.out.println(lineCount+" "+wordCount+" "+charCount+" "+args[0]);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void mkdir(String[] args) {
        if (args.length > 0) {
            for (String arg : args) {
                createDirectory(arg);
            }
        } else {
            System.out.println("Error: Missing directory names in mkdir command.");
        }
    }

    private void createDirectory(String dirPath) {
        File newDir;

        if (dirPath.endsWith(File.separator)) {
            dirPath = dirPath.substring(0, dirPath.length() - 1);
        }

        if (dirPath.contains(File.separator)) {
            newDir = new File(dirPath);
        } else {
            newDir = new File(currentDirectory + File.separator + dirPath);
        }

        if (newDir.exists()) {
            System.out.println("Directory already exists: " + newDir.getAbsolutePath());
        } else {
            newDir.mkdir();
        }
    }


    public void rmdir(String[] args) {
        if (args.length == 1) {
            if (args[0].equals("*")) {
                removeEmptyDirectoriesInCurrentDirectory(currentDirectory);
            } else {
                String directoryPath = args[0];
                if (directoryPath.startsWith("/")) {
                    // Handle full path
                    removeEmptyDirectory(directoryPath);
                } else {
                    // Handle relative (short) path
                    String fullPath = currentDirectory + File.separator + directoryPath;
                    removeEmptyDirectory(fullPath);
                }
            }
        } else {
            System.out.println("Error: Invalid number of arguments for rmdir");
        }
    }

    private void removeEmptyDirectoriesInCurrentDirectory(String directoryPath) {
        File currentDir = new File(directoryPath);
        if (currentDir.exists() && currentDir.isDirectory()) {
            File[] subdirs = currentDir.listFiles();
            if (subdirs != null) {
                for (File subdir : subdirs) {
                    if (subdir.isDirectory() && subdir.list().length == 0) {
                        if (subdir.delete()) {
                            System.out.println("Removed directory: " + subdir.getAbsolutePath());
                        } else {
                            System.out.println("Failed to remove directory: " + subdir.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    private void removeEmptyDirectory(String directoryPath) {
        File dir = new File(directoryPath);
        if (dir.exists() && dir.isDirectory()) {
            if (dir.list().length == 0) {
                if (dir.delete()) {
                    System.out.println("Removed directory: " + dir.getAbsolutePath());
                } else {
                    System.out.println("Failed to remove directory: " + dir.getAbsolutePath());
                }
            } else {
                System.out.println("Error: The directory is not empty and cannot be removed.");
            }
        } else {
            System.out.println("Error: Directory not found: " + directoryPath);
        }
    }

    public void cp(String[] args) {
        if (args.length != 2) {
            System.out.println("Error: cp command requires exactly two arguments.");
            return;
        }

        String sourceFilePath = args[0];
        String destinationFilePath = args[1];

        try {
            File sourceFile = new File(sourceFilePath);
            File destinationFile = new File(destinationFilePath);

            if (!sourceFile.exists()) {
                System.out.println("Error: Source file does not exist.");
            } else if (destinationFile.exists() && !destinationFile.isFile()) {
                System.out.println("Error: Destination is not a valid file.");
            } else {
                copyFile(sourceFile, destinationFile);
            }
        } catch (IOException e) {
            System.out.println("Error: An error occurred while copying the file.");
            e.printStackTrace();
        }
    }

    private void copyFile(File source, File destination) throws IOException {
        try (InputStream inStream = new FileInputStream(source);
             OutputStream outStream = new FileOutputStream(destination)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
        }
    }

}
