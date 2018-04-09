import exceptions.ParserException;
import exceptions.SystemUnsolvabilityException;
import parsers.TermParser;
import termTree.Equality;
import termTree.System;
import termTree.TermExpression;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HW5 {


    public static void main(String[] args) throws ParserException {
        try (Scanner in = new Scanner(new File("input.txt"));
             PrintWriter out = new PrintWriter(new File("output.txt"))) {

            TermParser parser = TermParser.getInstance();

            List<Equality> equalities = new ArrayList<>();

            while (in.hasNextLine()) {
                String line = in.nextLine();
                int index = line.indexOf("=");
                String subTerm1 = line.substring(0, index);
                String subTerm2 = line.substring(index + 1);
                TermExpression term1 = parser.parse(subTerm1);
                TermExpression term2 = parser.parse(subTerm2);
                equalities.add(new Equality(term1, term2));
            }

            System system = new System(equalities);

            for (Equality eq : system.getUnifier()) {
                out.println(eq.left + "=" + eq.right);
            }

        } catch (FileNotFoundException | SystemUnsolvabilityException e) {
            e.printStackTrace();
        }

    }
}
