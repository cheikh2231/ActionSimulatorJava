package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Programme {
    private ObservableList<Action> actionsProg = FXCollections.observableArrayList();

    public void ajouterAction(int idx) {
        Action action = Actions.getAction(idx);
        if (action != null)
            actionsProg.add(action);
    }

    public void ajouterActionCopy(int idx) {
        Action actionCopy = getActionProg(idx).copy();
        if (actionCopy != null)
            actionsProg.add(actionCopy);
    }

    public void suprimerAction(int idx) {
        if(existIndex(idx))
            actionsProg.remove(idx);
    }

    public void up(int idx){
        if (idx>0 && idx < actionsProg.size()){
            Action actionPrec = actionsProg.get(idx-1);
            Action action = actionsProg.get(idx);
            actionsProg.set(idx-1,action);
            actionsProg.set(idx,actionPrec);
        }
    }

    public void down(int idx){
        if (idx>=0 && idx < actionsProg.size()-1){
            Action action = actionsProg.get(idx);
            Action actionSuivant = actionsProg.get(idx+1);
            actionsProg.set(idx+1,action);
            actionsProg.set(idx,actionSuivant);
        }
    }

    public ObservableList<Action> getActionsProg() {
        return actionsProg;
    }

    public Action getActionProg(int idx){
        if(existIndex(idx))
            return actionsProg.get(idx);
        return null;
    }

    public int getActionProgSize(){
        return actionsProg.size();
    }

    public void dupplicate(int idx){
        if(existIndex(idx)) {
            ajouterActionCopy(idx);
        }
    }

    public boolean existIndex(int idx){
        return idx>=0 && idx<actionsProg.size();
    }
}
