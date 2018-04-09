package termTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class TermExpression {

    private static final String VAR_NAME = "t";
    private static final String FUNC_NAME = "f";

    public static TermVariable getVariableByNum(int n) {
        return new TermVariable(VAR_NAME + n);
    }

    private static TermExpression getArrow(TermVariable v1, TermVariable v2) {
        List<TermExpression> args = new ArrayList<>();
        args.add(v1);
        args.add(v2);
        return new Function(args, FUNC_NAME);
    }

    public static TermExpression getArrow(int n1, int n2) {
        return getArrow(getVariableByNum(n1), getVariableByNum(n2));
    }

    TermExpression substitute(TermVariable termVariable, TermExpression replacement) {
        throw new IllegalArgumentException("Unknown type");
    }

    Set<TermVariable> getFreeVariables() {
        throw new IllegalArgumentException("Unknown type");
    }

    public String parseTermToType() {
        throw new IllegalArgumentException("Unknown type");
    }


}
