package scratch.model;

public class ActionTournerDroite extends ActionTourner{
    @Override
    public String toString() {
        return  super.toString() + " droite de "+(super.isCanAffiche()? super.getAngle() : "");
    }
}
