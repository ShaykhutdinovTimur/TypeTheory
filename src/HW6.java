import exceptions.ParserException;
import exceptions.SystemUnsolvabilityException;
import lambdaTree.LambdaExpression;
import lambdaTree.LambdaVariable;
import parsers.LambdaParser;
import termTree.Equality;
import termTree.System;
import termTree.TermExpression;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HW6 {


    public static void main(String[] args) {
        try (Scanner in = new Scanner(new File("input.txt"));
             PrintWriter out = new PrintWriter(new File("output.txt"))) {
            String input = getInput(in);
            LambdaParser parser = LambdaParser.getInstance();
            LambdaExpression expression = parser.parse(input);

            List<Equality> system = new ArrayList<>();
            Map<LambdaVariable, Integer> context = new HashMap<>();
            int equationAnswer = expression.makeEquations(system, new AtomicInteger(1), new HashMap<>(), context);

            try {
                List<Equality> solution = (new System(system)).getUnifier();

                Map<TermExpression, TermExpression> leftToRight = new HashMap<>();
                for (Equality eq : solution) {
                    leftToRight.put(eq.left, eq.right);
                }

                TermExpression answer = leftToRight.get(TermExpression.getVariableByNum(equationAnswer));
                String resultingType;
                if (answer == null) {
                    resultingType = TermExpression.getVariableByNum(equationAnswer).toString();
                } else {
                    resultingType = answer.parseTermToType();
                }

                out.println(resultingType);

                for (Map.Entry<LambdaVariable, Integer> entry : context.entrySet()) {
                    int index = entry.getValue();
                    TermExpression termExpression = leftToRight.get(TermExpression.getVariableByNum(index));
                    String freeVarType = termExpression.parseTermToType();
                    out.println(entry.getKey() + ":" + freeVarType);
                }

            } catch (SystemUnsolvabilityException e) {
                out.println("Лямбда-выражение не имеет типа.");
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
