package scratch.model;

public class ActionTournerDroite extends ActionTourner{


    public ActionTournerDroite( int mesure) {
        super("droite de ", mesure);
    }

    public ActionTournerDroite() {
        super("droite de ");
    }

    @Override
    public Action copy() {
        return new ActionTournerDroite();
    }
    @Override
    public String toString() {
        return  super.getName()  +(super.isCanAffiche()? getMesure() : "");
    }
}
