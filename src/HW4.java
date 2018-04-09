import exceptions.ParserException;
import lambdaTree.LambdaExpression;
import parsers.LambdaParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class HW4 {


    public static void main(String[] args) {
        try (Scanner in = new Scanner(new File("input.txt"));
             PrintWriter out = new PrintWriter(new File("output.txt"))) {
            String input = getInput(in);
            LambdaParser parser = LambdaParser.getInstance();
            LambdaExpression expression = parser.parse(input);
            LambdaExpression normalExpression = expression.normalForm();
            out.print(normalExpression);

        } catch (FileNotFoundException | ParserException e) {
            e.printStackTrace();
        }
    }

    private static String getInput(Scanner in) {
        StringBuilder input = new StringBuilder();
        while (in.hasNextLine()) {
            input.append(in.nextLine());
        }
        return input.toString().trim();
    }

}
