import algo.SortingStation;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String prompt = "input> ";
        String output = "output: ";
        String help = "possible commands: help, quit";
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();
            if (line.equals("help"))
                System.out.println(help);
            else if (line.equals("quit"))
                break;
            else
                System.out.println(output + SortingStation.proceedExpression(line));
        }
    }
}