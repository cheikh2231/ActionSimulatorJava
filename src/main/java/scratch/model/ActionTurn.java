package scratch.model;

public abstract class ActionTurn extends Action{

    public ActionTurn(String name , String mesure) {
        super("Tourner à " + name,mesure);
    }

    public ActionTurn(String name) {
        super("Tourner à " + name,"90");
    }

    @Override
    public Action copy() {
        return null;
    }

    public String toString(){
        return getName();
    }

    @Override
    public boolean isValidMeasure(String mesure) {
        String regex = "^[a-zA-Z_][a-zA-Z0-9_]*$";
        Integer val = getValue(mesure);
        if (val != null)
            return val > 0 && val <= 180;
        return mesure.matches(regex);
    }

    @Override
    public String getUnity() {
        return " degrés";
    }

    @Override
    public String getDetailsName() {
        return " Angle vers la ";
    }

    @Override
    public String getFormatName() {
        return "TURN_";
    }

}
