package lambdaTree;

import exceptions.SubstituteException;
import termTree.Equality;
import termTree.TermExpression;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Abstraction extends LambdaExpression {
    private final LambdaExpression variable;
    private final LambdaExpression statement;

    public Abstraction(LambdaExpression variable, LambdaExpression statement) {
        this.variable = variable;
        this.statement = statement;
    }

    public LambdaExpression getVariable() {
        return variable;
    }

    public LambdaExpression getStatement() {
        return statement;
    }

    @Override
    public void getFreeVariables(Map<LambdaExpression, Integer> counter, Set<LambdaVariable> answer) {
        counter.putIfAbsent(variable, 0);
        Integer oldValue = counter.get(variable);
        counter.put(variable, oldValue + 1);
        statement.getFreeVariables(counter, answer);
        counter.put(variable, oldValue);
        if (oldValue == 0) {
            counter.remove(variable);
        }
    }

    @Override
    public LambdaExpression substitute(LambdaVariable oldLambdaVariable, LambdaExpression replacement, Map<LambdaExpression, Integer> counter, Set<LambdaVariable> replacementFreeVars) throws SubstituteException {
        counter.putIfAbsent(variable, 0);
        Integer oldValue = counter.get(variable);
        counter.put(variable, oldValue + 1);
        LambdaExpression statementSub = statement.substitute(oldLambdaVariable, replacement, counter, replacementFreeVars);
        LambdaExpression result = new Abstraction(variable, statementSub);
        counter.put(variable, oldValue);
        if (oldValue == 0) {
            counter.remove(variable);
        }
        return result;
    }

    @Override
    public int makeEquations(List<Equality> system, AtomicInteger nextNumber, Map<LambdaVariable, Integer> counter, Map<LambdaVariable, Integer> context) {
        int oldNumber = counter.getOrDefault(variable, -1);
        int newNumber = nextNumber.getAndIncrement();
        counter.put((LambdaVariable) variable, newNumber);
        int stNumber = statement.makeEquations(system, nextNumber, counter, context);
        int resNumber = nextNumber.getAndIncrement();
        system.add(new Equality(TermExpression.getVariableByNum(resNumber), TermExpression.getArrow(newNumber, stNumber)));
        counter.remove(variable);
        if (oldNumber != -1) {
            counter.put((LambdaVariable) variable, oldNumber);
        }
        return resNumber;
    }

    @Override
    public LambdaExpression headNormalForm() {
        return this;
    }

    @Override
    public LambdaExpression normalForm() {
        if (checkMemory(this)) {
            return loadNormalForm(this);
        }
        Abstraction abstraction = new Abstraction(variable, statement.normalForm());
        remember(this, abstraction);
        return abstraction;
    }

    @Override
    public String toString() {
        return "(" + "\\" + variable.toString() + "." + statement.toString() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Abstraction that = (Abstraction) o;

        if (getVariable() != null ? !getVariable().equals(that.getVariable()) : that.getVariable() != null) {
            return false;
        }
        return (getStatement() != null ? getStatement().equals(that.getStatement()) : that.getStatement() == null);

    }

    @Override
    public int hashCode() {
        int result = getVariable() != null ? getVariable().hashCode() : 0;
        result = 31 * result + (getStatement() != null ? getStatement().hashCode() : 0);
        return result;
    }

}
