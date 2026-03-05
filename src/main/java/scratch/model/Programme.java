package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Programme {
    private ObservableList<Action> actionsProg = FXCollections.observableArrayList();

    public void ajouterAction(int idx) {
        Action action = Actions.getAction(idx);
        if (action != null)
            actionsProg.add(action.copy());
    }

    public void removeAction(int idx) {
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
        if (canMoveDown(idx)){
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


    public void dupplicate(int idx){
        if(existIndex(idx)) {
            actionsProg.add(actionsProg.get(idx).copy());
        }
    }

    public boolean canMoveDown(int idx){
        return idx < size()-1 && idx >=0;
    }

    public boolean existIndex(int idx){
        return idx>=0 && idx<actionsProg.size();
    }

    public int size(){
        return actionsProg.size();
    }


    public void clear(){
        actionsProg.clear();
    }

    public boolean isEmpty(){
        return actionsProg.isEmpty();
    }
}
