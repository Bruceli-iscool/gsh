import java.util.*;
import java.io.File;
public class gsh {
    private static String currentDir = System.getProperty("user.dir");
    private static List<String> history = new ArrayList<>();
    public static void main (String args[]) {
        shell();
    }
    private static void shell() {
        System.out.println("gsh v0.1\nlicense for license or quit to exit the program or enter command.");
        while (true) {
            System.out.print("gsh@ "+currentDir+ "> ");
            String ans = getInput();
            interpret(ans);
            
        }
    }
    public static String getInput() {
        Scanner s = new Scanner(System.in);
        String in = s.nextLine();
        return in.stripTrailing().stripLeading();
    }
    public static void interpret(String line) {
        history.add(line);
        lex(line);
    }
    private static void lex(String line) {
        ArrayList<String> result = new ArrayList<String>();
        String z = "";
        boolean ifString = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            switch (c) {
                case '(': case ')': case ';': case '=': case '+': case '-': case '*':case '{': case '}':case ':':
                    if (!z.isEmpty()) {
                        result.add(z);
                        z = "";
                    }
                    result.add(String.valueOf(c));
                    break;
                case '"':
                    if (!z.isEmpty() && ifString) {
                        ifString = false;
                        result.add(z);
                        z = "";
                    } else {
                        ifString = true;
                    }
                    result.add("\"");
                    break;
                case ' ':
                    if (!z.isEmpty() && !ifString) {
                        result.add(z);
                        z = "";
                    } else if (ifString) {
                        z += c;
                    }
                    break;
                default:
                    z += c;
                    break;
            }
        }
        if (!z.isEmpty()) {
            result.add(z);
        }
        execute(result);
    }
    private static void execute(List<String> tokens) {
        while (!tokens.isEmpty()) {
            String current = tokens.get(0);
            tokens.remove(0);
            if (current.matches("license")) {
                System.out.println("NO WARRANTY OF ANY KIND IS PROVIDED!\ngsh is licensed under the GNU General Public License (GPL) v2.0");
            } else if (current.matches("quit")) {
                System.exit(0);
            }else if (current.matches("echo")) {
                if (tokens.isEmpty()) {
                    System.err.println("gsh: Error! Expected value!");
                    break;
                }
                current = tokens.get(0);
                tokens.remove(0);
                System.out.println(current);
            } else if (current.matches("history")){
                for (String s:history) {
                    System.out.println(s);
                }
            } else if (current.matches("cd")) {
	    		if (!tokens.isEmpty()) {
	    			current= tokens.get(0);
	    			tokens.remove(0);
	    			if (current.matches(";")) {
	    				currentDir = System.getProperty("user.dir");
	    				continue;
	    			} else {
	    				File tempDir = new File(current);
	    				if (!tempDir.isAbsolute()) {
	    					tempDir = new File (currentDir +File.separator+current);
	    				} 
                        if (tempDir.exists()&&tempDir.isDirectory()) {
                            currentDir = tempDir.getAbsolutePath();
                        } else {
	    					System.err.println("gsh: Error! "+current+" is not a valid directory!");
	    					break;
	    				}
	    			}
	    		} else {
	    			currentDir = System.getProperty("user.dir");
	    			break;
	    		}
	    	} else if (current.matches("mkdir")) {
                if (tokens.isEmpty()) {
                    System.out.println("gsh: Error! Expected argument!");
                    break;
                } else {
                    current = tokens.get(0);
                    tokens.remove(0);
                    File tempDir = new File(current);
                    if (!tempDir.isAbsolute()) {
                        tempDir = new File(currentDir + File.separator + tempDir);
                    }
                    if (tempDir.mkdir()) {
                        
                    } else {    
                        System.out.println("gsh: Error! Directory not created!");
                        break;
                    }
                }
            } else if (current.matches("help")) {
                if (tokens.isEmpty()) {
                    System.out.println("gsh: Error! Usage help [command]");
                    break;
                } else {
                    current = tokens.get(0);
                    switch (current) {
                        case "cd":
                            cdHelp();
                            break;
                        case "echo":
                            echoHelp();
                            break;
                        case "history":
                            historyHelp();
                            break;
                        case "mkdir":
                            mkdirHelp();
                            break;
                        case "ls":
                            lsHelp();
                            break;
                        default:
                            System.out.println("gsh: Error! Invalid command!");
                            break;
                    }
                    if (tokens.isEmpty()) {
                        break;
                    }
                    tokens.remove(0);
                }
            } else if (current.matches("ls")) {
                File tempDir = new File(currentDir);
                File[] contents = tempDir.listFiles();
                if (contents != null) {
                    for (File i : contents) {
                        System.out.println(i.getName());
                    }
                }
            }else {
                System.err.println("gsh: Error! Invalid statement!");              
            }
            if (tokens.isEmpty()) {
                break;
            }
            current = tokens.get(0);
            tokens.remove(0);
            if (current.matches(";")) {
                continue;
            } else {
                break; 
            }
        }

    }
    // help functions
    private static void cdHelp() {
        System.out.println("cd: switch between directories.\nUsage: cd [directory]/cd\nIf used without arguments, the directory will change to the directory in which the program was launched.");
    }
    private static void echoHelp() {
        System.out.println("echo: print a value to the console.\nUsage: echo [value]");
    }
    private static void historyHelp() {
        System.out.println("history: print previously used commands in a session.\nUsage: history");
    }
    private static void mkdirHelp() {
        System.out.println("mkdir: creates a directory.\nUsage: mkdir [path]");
    }
    private static void lsHelp() {
        System.out.println("ls: lists all files in the current working directory.\nUsage: ls [path]");
    }
}
