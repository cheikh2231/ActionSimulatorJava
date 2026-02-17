package scratch.model;

public class ActionTournerGauche extends ActionTourner{
    private String name = "gauche de ";

    @Override
    public Action copy() {
        return new ActionTournerGauche();
    }
    @Override
    public String toString() {
        return  super.getName() + name +(super.isCanAffiche()? getMesure() : "");
    }
}
