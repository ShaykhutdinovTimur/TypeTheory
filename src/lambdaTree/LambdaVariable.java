package lambdaTree;

import exceptions.SubstituteException;
import termTree.Equality;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class LambdaVariable extends LambdaExpression {
    private final String name;

    public LambdaVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    @Override
    public void getFreeVariables(Map<LambdaExpression, Integer> counter, Set<LambdaVariable> answer) {
        if (!counter.containsKey(this)) {
            answer.add(this);
        }
    }

    @Override
    public LambdaExpression substitute(LambdaVariable oldLambdaVariable, LambdaExpression replacement, Map<LambdaExpression, Integer> counter, Set<LambdaVariable> replacementFreeVars) throws SubstituteException {
        if (!counter.containsKey(oldLambdaVariable) && equals(oldLambdaVariable)) {
            for (LambdaVariable freeVar : replacementFreeVars) {
                if (counter.containsKey(freeVar)) {
                    throw new SubstituteException("Нет свободы для подстановки для переменной " + freeVar);
                }
            }
            return replacement;
        }
        return this;
    }

    @Override
    public int makeEquations(List<Equality> system, AtomicInteger nextNumber, Map<LambdaVariable, Integer> counter, Map<LambdaVariable, Integer> context) {
        if (counter.containsKey(this)) {
            return counter.get(this);
        }
        if (context.containsKey(this)) {
            return context.get(this);
        }
        int resNumber = nextNumber.getAndIncrement();
        context.put(this, resNumber);
        return resNumber;
    }

    @Override
    public LambdaExpression headNormalForm() {
        return this;
    }

    @Override
    public LambdaExpression normalForm() {
        return this;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LambdaVariable lambdaVariable = (LambdaVariable) o;

        return (getName() != null ? getName().equals(lambdaVariable.getName()) : lambdaVariable.getName() == null);

    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

}
