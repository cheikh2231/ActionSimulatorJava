package scratch.model;

public class ActionTournerGauche extends ActionTourner{

    public ActionTournerGauche( int mesure) {
        super("gauche de ", mesure);
    }

    public ActionTournerGauche() {
        super("gauche de ");
    }

    @Override
    public Action copy() {
        return new ActionTournerGauche(getMesure());
    }
    @Override
    public String toString() {
        return  super.getName()  + getMesure();
    }

    @Override
    public String getDetailsName() {
        return super.getDetailsName() + "gauche de ";
    }
}
