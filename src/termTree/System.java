package termTree;

import exceptions.SystemUnsolvabilityException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class System {
    private List<Equality> equalities;

    public System(List<Equality> equalities) {
        this.equalities = equalities;
    }

    private static List<Equality> getWithout(List<Equality> initial, Equality withoutWhat) {
        return initial.stream().filter(eq -> !eq.equals(withoutWhat)).collect(Collectors.toList());
    }

    private static List<Equality> substitute(List<Equality> system, TermVariable termVariable, TermExpression replacement) {
        List<Equality> result = new ArrayList<>();
        for (Equality eq : system) {
            TermExpression sLeft = eq.left.substitute(termVariable, replacement);
            TermExpression sRight = eq.right.substitute(termVariable, replacement);
            result.add(new Equality(sLeft, sRight));
        }
        return result;
    }


    private boolean substitute(TermVariable left, TermExpression right) throws SystemUnsolvabilityException {
        if (right.getFreeVariables().contains(left)) {
            throw new SystemUnsolvabilityException(left, right);
        }
        Equality eq = new Equality(left, right);
        List<Equality> nextSystem = getWithout(equalities, eq);

        boolean isInG = false;
        for (Equality neq : nextSystem) {
            if (neq.left.getFreeVariables().contains(left) || neq.right.getFreeVariables().contains(left)) {
                isInG = true;
                break;
            }
        }
        if (isInG) {
            nextSystem = substitute(nextSystem, left, right);
            nextSystem.add(new Equality(left, right));
            equalities = nextSystem;
            return true;
        }
        return false;
    }

    private void swap(Function left, TermVariable right) {
        Equality eq = new Equality(left, right);
        List<Equality> nextSystem = getWithout(equalities, eq);
        nextSystem.add(new Equality(right, left));
        equalities = nextSystem;
    }

    private void expandFunctions(Function leftF, Function rightF) throws SystemUnsolvabilityException {
        Equality eq = new Equality(leftF, rightF);
        if (!leftF.getName().equals(rightF.getName()) || leftF.getArgs().size() != rightF.getArgs().size()) {
            throw new SystemUnsolvabilityException(leftF, rightF);
        }
        List<Equality> nextSystem = getWithout(equalities, eq);
        List<TermExpression> lefts = leftF.getArgs();
        List<TermExpression> rights = rightF.getArgs();
        for (int i = 0; i < lefts.size(); i++) {
            nextSystem.add(new Equality(lefts.get(i), rights.get(i)));
        }
        equalities = nextSystem;
    }

    public List<Equality> getUnifier() throws SystemUnsolvabilityException {
        boolean next = true;
        while (next) {
            next = false;
            if (equalities.isEmpty()) break;
            for (Equality eq : equalities) {
                if (eq.left.equals(eq.right)) {
                    equalities = getWithout(equalities, eq);
                    next = true;
                } else {
                    TermExpression left = eq.left;
                    TermExpression right = eq.right;

                    if (left instanceof Function && right instanceof Function) {
                        expandFunctions((Function) left, (Function) right);
                        next = true;
                    } else if (left instanceof Function && right instanceof TermVariable) {
                        swap((Function) left, (TermVariable) right);
                        next = true;
                    } else if (left instanceof TermVariable) {
                        next = substitute((TermVariable) left, right);
                    }
                }
                if (next) {
                    break;
                }
            }
        }
        return equalities;
    }

}
