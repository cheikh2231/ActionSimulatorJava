package scratch.model;

public abstract class ActionTurn extends Action{

    public ActionTurn(String name , int mesure) {
        super("Tourner à " + name,mesure);
    }

    public ActionTurn(String name) {
        super("Tourner à " + name,90);
    }

    @Override
    public Action copy() {
        return null;
    }

    public String toString(){
        return getName();
    }

    @Override
    public boolean isValidMeasure(int mesure) {
        return mesure>0 && mesure<=180;
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
