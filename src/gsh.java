import java.util.*;

public class gsh {
    public static void main (String args[]) {
        shell();
    }
    private static void shell() {
        System.out.println("gsh v0.1\nlicense() for license or exit() to exit the program or enter command.");
        while (true) {
            System.out.print("> ");
            String ans = getInput();
            if (ans.replace(" ", "").equals("license()")) {
                System.out.println("NO WARRANTY OF ANY KIND IS PROVIDED!\ngsh is licensed under the GNU General Public License (GPL) v2.0");
            } else if (ans.replace(" ", "").equals("exit()")) {
                System.exit(0);
            }else {
                interpret(ans);
            }
            continue;
        }
    }
    public static String getInput() {
        Scanner s = new Scanner(System.in);
        String in = s.nextLine();
        return in.stripTrailing().stripLeading();
    }
    public static void interpret(String line) {
        lex(line);
    }
    private static void lex(String line) {
        ArrayList<String> result = new ArrayList<String>();
        String z = "";
        boolean ifString = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            switch (c) {
                case '(': case ')': case ';': case '=': case '+': case '-': case '*': case '/':case '{': case '}':case ':':
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
            if (current.matches("echo")) {
                if (tokens.isEmpty()) {
                    System.err.println("gsh: Error! Expected value!");
                    break;
                }
                current = tokens.get(0);
                tokens.remove(0);
                System.out.println(current);
                if (!tokens.isEmpty()) { 
                    current = tokens.get(0);
                    tokens.remove(0);
                    if (current.matches(";")) {
                        continue;
                    } else {
                        break;
                    }
                }
            } else {
                System.err.println("gsh: Error! Invalid statement!");              
            }
        }
    }
}