package exceptions;

import termTree.Function;
import termTree.TermExpression;
import termTree.TermVariable;

public class SystemUnsolvabilityException extends Exception {
    public SystemUnsolvabilityException(TermVariable left, TermExpression right) {
        super("Система неразрешима: переменная " + left + " входит свободно в " + right);
    }

    public SystemUnsolvabilityException(Function left, Function right) {
        super("Система неразрешима: " + left + " != " + right);
    }
}
