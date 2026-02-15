package scratch.model;

public class ActionTournerGauche extends ActionTourner{


    @Override
    public Action copy() {
        return new ActionTournerGauche();
    }
    @Override
    public String toString() {
        return super.toString() + " gauche de "+(super.isCanAffiche()? super.getAngle() : "");
    }
}
