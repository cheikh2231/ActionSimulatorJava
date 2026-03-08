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
        return new ActionTournerDroite(getMesure());
    }
    @Override
    public String toString() {
        return  super.getName() + getMesure();
    }
    @Override
    public String getDetailsName() {
        return super.getDetailsName() + "droite de ";
    }

    @Override
    public String getFormatName() {
        return super.getFormatName()+"RIGHT";
    }

}
