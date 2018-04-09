package lambdaTree;

import exceptions.SubstituteException;
import termTree.Equality;
import termTree.TermExpression;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Application extends LambdaExpression {
    private final LambdaExpression left;
    private final LambdaExpression right;

    public Application(LambdaExpression left, LambdaExpression right) {
        this.left = left;
        this.right = right;
    }

    public LambdaExpression getLeft() {
        return left;
    }

    public LambdaExpression getRight() {
        return right;
    }

    @Override
    public void getFreeVariables(Map<LambdaExpression, Integer> counter, Set<LambdaVariable> answer) {
        left.getFreeVariables(counter, answer);
        right.getFreeVariables(counter, answer);
    }

    @Override
    public LambdaExpression substitute(LambdaVariable oldLambdaVariable, LambdaExpression replacement, Map<LambdaExpression, Integer> counter, Set<LambdaVariable> replacementFreeVars) throws SubstituteException {
        LambdaExpression leftSub = left.substitute(oldLambdaVariable, replacement, counter, replacementFreeVars);
        LambdaExpression rightSub = right.substitute(oldLambdaVariable, replacement, counter, replacementFreeVars);
        return new Application(leftSub, rightSub);
    }

    @Override
    public int makeEquations(List<Equality> system, AtomicInteger nextNumber, Map<LambdaVariable, Integer> counter, Map<LambdaVariable, Integer> context) {
        int leftNumber = left.makeEquations(system, nextNumber, counter, context);
        int rightNumber = right.makeEquations(system, nextNumber, counter, context);
        int resNumber = nextNumber.getAndIncrement();
        system.add(new Equality(TermExpression.getVariableByNum(leftNumber), TermExpression.getArrow(rightNumber, resNumber)));
        return resNumber;
    }

    @Override
    public LambdaExpression headNormalForm() {
        if (checkHeadMemory(this)) {
            return loadHeadNormalForm(this);
        }
        LambdaExpression leftNormal = left.headNormalForm();
        if (leftNormal instanceof Abstraction) {
            Abstraction leftIt = (Abstraction) leftNormal;
            LambdaVariable leftItLambdaVariable = (LambdaVariable) leftIt.getVariable();
            LambdaExpression leftItStatement = leftIt.getStatement();
            LambdaExpression substitution = leftItStatement.substitute(leftItLambdaVariable, right);
            LambdaExpression headNormalForm = substitution.headNormalForm();
            rememberHead(this, headNormalForm);
            return headNormalForm;
        } else {
            Application applicative = new Application(leftNormal, right);
            rememberHead(this, applicative);
            return applicative;
        }
    }

    @Override
    public LambdaExpression normalForm() {
        if (checkMemory(this)) {
            return loadNormalForm(this);
        }
        LambdaExpression headLeftNormalForm = left.headNormalForm();
        if (headLeftNormalForm instanceof Abstraction) {
            Abstraction leftIt = (Abstraction) headLeftNormalForm;
            LambdaVariable leftItLambdaVariable = (LambdaVariable) leftIt.getVariable();
            LambdaExpression leftItStatement = leftIt.getStatement();
            LambdaExpression normalForm = leftItStatement.substitute(leftItLambdaVariable, right).normalForm();
            remember(this, normalForm);
            return normalForm;
        } else {
            Application applicative = new Application(headLeftNormalForm.normalForm(), right.normalForm());
            remember(this, applicative);
            return applicative;
        }
    }

    @Override
    public String toString() {
        return "(" + left + " " + right + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Application that = (Application) o;

        if (getLeft() != null ? !getLeft().equals(that.getLeft()) : that.getLeft() != null) {
            return false;
        }
        return (getRight() != null ? getRight().equals(that.getRight()) : that.getRight() == null);

    }

    @Override
    public int hashCode() {
        int result = getLeft() != null ? getLeft().hashCode() : 0;
        result = 31 * result + (getRight() != null ? getRight().hashCode() : 0);
        return result;
    }

}
