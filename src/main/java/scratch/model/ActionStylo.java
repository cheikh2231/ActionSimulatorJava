package scratch.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ActionStylo extends Action{
    private BooleanProperty etatStyloAbaisser = new SimpleBooleanProperty(true);

    public ActionStylo(boolean etatStyloAbaisser) {
        super(etatStyloAbaisser ? "Abaisser stylo":"Lever stylo",0);
        setEtatStyloAbaisser(etatStyloAbaisser);
    }

    public boolean getEtatStyloAbaisser() {
        return etatStyloAbaisser.get();
    }

    public void setEtatStyloAbaisser(boolean etatStylo) {
        this.etatStyloAbaisser.set(etatStylo);
    }

    public String getName(){
       return super.getName();
    }

    @Override
    public Action copy() {
        return new ActionStylo(getEtatStyloAbaisser());
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean isDetailable() {
        return false;
    }

    @Override
    public String getUnity() {
        return "";
    }

    @Override
    public String getDetailsName() {
        return getName();
    }

    @Override
    public String getFormatName() {
        return "PEN_"+(getEtatStyloAbaisser() ? "DOWN":"UP");
    }
}
