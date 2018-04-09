package termTree;

import java.util.HashSet;
import java.util.Set;

public class TermVariable extends TermExpression {

    private final String name;

    public TermVariable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public TermExpression substitute(TermVariable termVariable, TermExpression replacement) {
        if (equals(termVariable)) {
            return replacement;
        }
        return this;
    }

    @Override
    public Set<TermVariable> getFreeVariables() {
        Set<TermVariable> result = new HashSet<>();
        result.add(this);
        return result;
    }


    @Override
    public String parseTermToType() {
        return name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TermVariable termVariable = (TermVariable) o;

        return !(getName() != null ? !getName().equals(termVariable.getName()) : termVariable.getName() != null);

    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

}
