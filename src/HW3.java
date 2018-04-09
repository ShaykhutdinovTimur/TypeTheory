import exceptions.ParserException;
import exceptions.SubstituteException;
import lambdaTree.LambdaExpression;
import lambdaTree.LambdaVariable;
import parsers.LambdaParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class HW3 {

    public static void main(String[] args) {
        try (Scanner in = new Scanner(new File("input.txt"));
             PrintWriter out = new PrintWriter(new File("output.txt"))) {
            String input = getInput(in);
            int indexOf = input.indexOf('[');
            String exprStr = input.substring(0, indexOf);
            String subExpr = input.substring(indexOf);
            int index = subExpr.indexOf(":=");
            String varStr = subExpr.substring(1, index);
            String subStr = subExpr.substring(index + 2, subExpr.length() - 1);
            LambdaVariable var = new LambdaVariable(varStr);
            LambdaParser parser = LambdaParser.getInstance();
            LambdaExpression substitution = parser.parse(subStr);
            LambdaExpression expression = parser.parse(exprStr);
            try {
                Set<LambdaVariable> freeVars = substitution.getFreeVariables();
                LambdaExpression result = expression.substitute(var, substitution, new HashMap<>(), freeVars);
                out.print(result);
            } catch (SubstituteException e) {
                out.print(e.getMessage());
            }
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
