package scratch.model;

import java.util.HashMap;
import java.util.Map;

public class Variable extends Action{
    public final Map<Variable,Integer> variableMap = new HashMap<>();
    public Variable(String name, Integer measure) {
        super(name, measure);
    }

    @Override
    public Action copy() {
        return null;
    }

    @Override
    public String getUnity() {
        return "";
    }

    @Override
    public String getDetailsName() {
        return "";
    }

    @Override
    public String getFormatName() {
        return "";
    }

}
