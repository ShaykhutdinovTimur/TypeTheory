package lambdaTree;

import exceptions.SubstituteException;
import termTree.Equality;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class LambdaExpression {

    private final static Map<LambdaExpression, LambdaExpression> memory = new HashMap<>();
    private final static Map<LambdaExpression, LambdaExpression> headMemory = new HashMap<>();

    protected static void rememberHead(LambdaExpression from, LambdaExpression to) {
        if (!headMemory.containsKey(from)) {
            headMemory.put(from, to);
        }
    }

    protected static boolean checkHeadMemory(LambdaExpression expression) {
        return headMemory.containsKey(expression);
    }

    protected static LambdaExpression loadHeadNormalForm(LambdaExpression expression) {
        return headMemory.get(expression);
    }


    protected static void remember(LambdaExpression from, LambdaExpression to) {
        if (!memory.containsKey(from)) {
            memory.put(from, to);
        }
    }

    protected static boolean checkMemory(LambdaExpression expression) {
        return memory.containsKey(expression);
    }

    protected static LambdaExpression loadNormalForm(LambdaExpression expression) {
        return memory.get(expression);
    }

    public LambdaExpression headNormalForm() {
        throw new IllegalArgumentException("Unknown type");
    }

    public LambdaExpression normalForm() {
        throw new IllegalArgumentException("Unknown type");
    }

    public void getFreeVariables(Map<LambdaExpression, Integer> counter, Set<LambdaVariable> answer) {
        throw new IllegalArgumentException("Unknown type");
    }

    public final Set<LambdaVariable> getFreeVariables() {
        Map<LambdaExpression, Integer> counter = new HashMap<>();
        Set<LambdaVariable> result = new HashSet<>();
        this.getFreeVariables(counter, result);
        return result;
    }

    public LambdaExpression substitute(LambdaVariable oldLambdaVariable,
                                       LambdaExpression replacement,
                                       Map<LambdaExpression, Integer> counter,
                                       Set<LambdaVariable> replacementFreeVars)
            throws SubstituteException {
        throw new IllegalArgumentException("Unknown type");
    }

    public final LambdaExpression substitute(LambdaVariable oldLambdaVariable,
                                             LambdaExpression replacement)
            throws SubstituteException {
        return substitute(oldLambdaVariable, replacement, new HashMap<>(), new HashSet<>());
    }

    public int makeEquations(List<Equality> system,
                             AtomicInteger nextNumber,
                             Map<LambdaVariable, Integer> counter,
                             Map<LambdaVariable, Integer> context) {

        throw new IllegalArgumentException("Unknown type");
    }
}
