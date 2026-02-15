package scratch.model;

public class ActionTournerDroite extends ActionTourner{

    @Override
    public Action copy() {
        return new ActionTournerDroite();
    }
    @Override
    public String toString() {
        return  super.toString() + " droite de "+(super.isCanAffiche()? super.getAngle() : "");
    }
}
