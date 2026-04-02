package scratch.model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Map;
import java.util.Objects;

public class Program {
    private ObservableList<Action> actionsProg =
            FXCollections.observableArrayList(
                    action -> new Observable[] { action.measureProperty(), action.nameVariableProperty() }
            );

    public void addAction(int idx,int pos) {
        Action action = Actions.getAction(idx);
        if (action != null)
            actionsProg.add(pos,action.copy());
    }

    public void removeAction(int idx) {
        if(existIndex(idx)) {
            Action selected = getActionProg(idx);
            if (selected.getActionType() == ActionType.VARIABLEDECLARATION) {
                // on vérifie que celle selectionné est celle qui est dans la liste
                if(Variable.variableMap.get(selected.getNameVariable()) == selected){
                    Variable.variableMap.remove(selected.getNameVariable());

                    if (actionsProg.stream().filter(n-> Objects.equals(n.getNameVariable(), selected.getNameVariable())).count()>1){
                        String name = selected.getNameVariable();
                        actionsProg.remove(idx);
                        Variable.variableMap.put(name,(Variable) actionsProg.stream().filter(m-> Objects.equals(m.getNameVariable(), name)).findFirst().get());
                        return;
                    }
                }
            }
            actionsProg.remove(idx);
        }
    }
    public boolean canIncrDecr(int idx){
        return existIndex(idx) && getActionProg(idx).canIncrDecr();
    }
    public void up(int idx){
        if (idx>0 && idx < actionsProg.size()){
            Action actionPrec = actionsProg.get(idx-1);
            Action action = actionsProg.get(idx);
            actionsProg.set(idx-1,action);
            actionsProg.set(idx,actionPrec);
        }
    }

    public void increment(int idx){
        getActionProg(idx).increment();
    }

    public void decrement(int idx){
        getActionProg(idx).decrement();
    }

    public void down(int idx){
        if (canMoveDown(idx)){
            Action action = actionsProg.get(idx);
            Action actionSuivant = actionsProg.get(idx+1);
            actionsProg.set(idx+1,action);
            actionsProg.set(idx,actionSuivant);
        }
    }

    public ObservableList<Action> getActionsProgList() {
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
        Variable.variableMap.clear();
        actionsProg.clear();
    }

    public boolean isEmpty(){
        return actionsProg.isEmpty();
    }


    public boolean allAssignationsAreValid() {
        return getActionsProgList().stream()
                .filter(a -> a.getActionType() == ActionType.VARIABLEASSIGNATION)
                .allMatch(a -> Variable.variableMap.containsKey(a.getNameVariable()));
    }

    public boolean hasUndefinedValues(){
        return getActionsProgList().stream().anyMatch(action -> action.getValue(action.getMeasure()) == null);
    }
    private boolean hasDuplicatePenStatus() {
        boolean abaisser = true;
        for (Action action : getActionsProgList()) {
            if (action.getActionType() == ActionType.ACTIONPEN) {
                ActionPen actionPen = (ActionPen) action;
                if (actionPen.getPenDownStatus() == abaisser)
                    return true;
                else
                    abaisser = actionPen.getPenDownStatus();
            }
        }
        return false;
    }

    public boolean hasAnomalies(){
        return hasDuplicatePenStatus() || hasActionBeforeDecla() || haveDifferenceBetweenMapAndList() || !allAssignationsAreValid() || hasUndefinedValues();
    }

    private boolean hasActionBeforeDecla(){
        boolean hasActionNotDecla = false;
        if (getActionsProgList().stream().noneMatch(a -> a.getActionType() == ActionType.VARIABLEDECLARATION)){
            return false;
        }
        for (Action a  : getActionsProgList()){
            if (a.getActionType() != ActionType.VARIABLEDECLARATION){
                hasActionNotDecla = true;
            }else if (hasActionNotDecla){
                return true;
            }
        }
        return false;
    }

    public boolean updateVariableToMap(String newName, int idxProg, String value){
        Map<String, Variable> map = Variable.variableMap;

        if(getActionProg(idxProg).getActionType()== ActionType.VARIABLEDECLARATION){
            VariableDeclaration vD = (VariableDeclaration) getActionProg(idxProg);

            String oldName = vD.getNameVariable();

            // si rien ne change on ne fait rien
            if (newName == null || newName.equals(oldName)) return true;

            boolean alreadyExist = map.containsKey(newName);

            // le nouveau nom est vide on supp l'ancien
            if (newName.isEmpty()) {
                if (oldName != null && !oldName.isEmpty()) {
                    map.remove(oldName);
                }
                vD.setNameVariable(newName);
                return true;
            }

            // mettre a jour le map
            if (oldName != null && !oldName.isEmpty() && map.get(oldName) == (vD)) {
                map.remove(oldName);
            }
            // si la variableName existe déjà dans la map et qu'on essaie de déclare une autre variable avec le meme nom
            if (alreadyExist && map.get(newName) != vD) {
                vD.setNameVariable(newName);
                return true;
            }


            if (vD.isValidVariableName(newName)) {
                vD.setNameVariable(newName);
                vD.addToVariableMap();
                return true;
            }
        }else if(getActionProg(idxProg).getActionType() == ActionType.VARIABLEASSIGNATION){
            VariableAssignation vA = (VariableAssignation) getActionProg(idxProg);
            if (!vA.isValidVariableName(newName)) return false;
            vA.setNameVariable(newName);
            vA.setMeasure(value);
            if(Variable.variableMap.containsKey(newName)){
                if (value != null) {
                    Variable.variableMap.get(newName).setMeasure(value);
                    Variable.variableMap.put(newName,Variable.variableMap.get(newName));
                    return true;
                }
                return false;
            }
            return true;

        }else {
            if (value != null && getActionProg(idxProg).isValidMeasure(value)){
                getActionProg(idxProg).setMeasure(value);
                return true;
            }else
                return false;

        }
        return false;
    }

    public boolean isDeclaration(int idx){
        return getActionProg(idx) != null && getActionProg(idx).getActionType()==ActionType.VARIABLEDECLARATION;
    }
    public boolean isAssignation(int idx){
        return getActionProg(idx) != null && getActionProg(idx).getActionType()==ActionType.VARIABLEASSIGNATION;
    }

    public boolean haveDifferenceBetweenMapAndList(){
        return actionsProg.stream().filter(a -> a.getActionType() == ActionType.VARIABLEDECLARATION).count() != Variable.variableMap.size();
    }
}
