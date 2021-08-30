import java.util.Locale;

public class Printer {

    public static void printLineLn(String line){
        System.out.println(line.toUpperCase(Locale.ROOT));
    }

    public static void printLine(String line){
        System.out.print(line.toUpperCase(Locale.ROOT));
    }
}
