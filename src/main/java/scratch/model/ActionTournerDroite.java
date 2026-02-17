package scratch.model;

public class ActionTournerDroite extends ActionTourner{
    private String name = "droite de ";
    @Override
    public Action copy() {
        return new ActionTournerDroite();
    }
    @Override
    public String toString() {
        return  super.getName() + name +(super.isCanAffiche()? getMesure() : "");
    }
}
