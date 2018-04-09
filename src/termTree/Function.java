package termTree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Function extends TermExpression {

    private final List<TermExpression> args;

    private final String name;

    public Function(List<TermExpression> args, String name) {
        this.args = args;
        this.name = name;
    }

    public List<TermExpression> getArgs() {
        return args;
    }

    public String getName() {
        return name;
    }

    @Override
    public TermExpression substitute(TermVariable termVariable, TermExpression replacement) {
        List<TermExpression> newArgs = new ArrayList<>();
        for (TermExpression arg : getArgs()) {
            newArgs.add(arg.substitute(termVariable, replacement));
        }
        return new Function(newArgs, getName());
    }

    @Override
    public Set<TermVariable> getFreeVariables() {
        Set<TermVariable> set = new HashSet<>();
        for (TermExpression var : getArgs()) {
            set.addAll(var.getFreeVariables());
        }
        return set;
    }

    @Override
    public String parseTermToType() {
        List<TermExpression> args = getArgs();
        if (args.size() != 2) {
            throw new IllegalArgumentException("Incorrect arguments size in term");
        }
        StringBuilder result = new StringBuilder();
        if (args.get(0) instanceof TermVariable) {
            result.append(((TermVariable) args.get(0)).getName());
        } else {
            result.append("(").append(args.get(0).parseTermToType()).append(")");
        }
        result.append("->");
        result.append(args.get(1).parseTermToType());
        return result.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Function function = (Function) o;

        if (getArgs() != null ? !getArgs().equals(function.getArgs()) : function.getArgs() != null) {
            return false;
        }
        return !(getName() != null ? !getName().equals(function.getName()) : function.getName() != null);

    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(name);
        if (!args.isEmpty()) {
            result.append("(");
            result.append(args.get(0).toString());
            for (int i = 1; i < args.size(); i++) {
                result.append(", ").append(args.get(i).toString());
            }
            result.append(")");
        }
        return result.toString();
    }

    @Override
    public int hashCode() {
        int result = getArgs() != null ? getArgs().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }
}
