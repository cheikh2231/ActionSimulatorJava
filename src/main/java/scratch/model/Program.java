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

    public void addAction(int idx, int pos) {
        Action action = Actions.getAction(idx);
        if (action != null)
            actionsProg.add(pos, action.copy());
    }


    public void removeAction(int idx) {
        if (existIndex(idx)) {
            Action selected = getActionProg(idx);
            if (selected.getActionType() == ActionType.VARIABLEDECLARATION) {
                // on vérifie que celle selectionné est celle qui est dans la liste
                if (Variable.variableMap.get(selected.getNameVariable()) == selected) {
                    Variable.variableMap.remove(selected.getNameVariable());

                    if (actionsProg.stream().filter(n -> Objects.equals(n.getNameVariable(), selected.getNameVariable())).count() > 1) {
                        String name = selected.getNameVariable();
                        actionsProg.remove(idx);
                        Variable.variableMap.put(name, (Variable) actionsProg.stream().filter(m -> Objects.equals(m.getNameVariable(), name)).findFirst().get());
                        return;
                    }
                }
            }
            actionsProg.remove(idx);
        }
    }

    public boolean canIncrDecr(String measureProperty, int idx) {
        return existIndex(idx) && getActionProg(idx).canIncrDecr(measureProperty);
    }

    public void up(int idx) {
        if (idx > 0 && idx < actionsProg.size()) {
            Action actionPrec = actionsProg.get(idx - 1);
            Action action = actionsProg.get(idx);
            actionsProg.set(idx - 1, action);
            actionsProg.set(idx, actionPrec);
        }
    }

    public void increment(int idx) {
        getActionProg(idx).increment();
    }

    public void decrement(int idx) {
        getActionProg(idx).decrement();
    }

    public void down(int idx) {
        if (canMoveDown(idx)) {
            Action action = actionsProg.get(idx);
            Action actionSuivant = actionsProg.get(idx + 1);
            actionsProg.set(idx + 1, action);
            actionsProg.set(idx, actionSuivant);
        }
    }

    public ObservableList<Action> getActionsProgList() {
        return actionsProg;
    }

    public Action getActionProg(int idx) {
        if (existIndex(idx))
            return actionsProg.get(idx);
        return null;
    }


    public void dupplicate(int idx) {
        if (existIndex(idx)) {
            actionsProg.add(actionsProg.get(idx).copy());
        }
    }

    public boolean canMoveDown(int idx) {
        return idx < size() - 1 && idx >= 0;
    }

    public boolean existIndex(int idx) {
        return idx >= 0 && idx < actionsProg.size();
    }

    public int size() {
        return actionsProg.size();
    }


    public void clear() {
        Variable.variableMap.clear();
        actionsProg.clear();
    }

    public boolean isEmpty() {
        return actionsProg.isEmpty();
    }


    public boolean allAssignationsAreValid() {
        return getActionsProgList().stream()
                .filter(a -> a.getActionType() == ActionType.VARIABLEASSIGNATION)
                .allMatch(a -> isDeclared(a.getNameVariable()) && !a.getMeasure().isEmpty());
    }

    private boolean isDeclared(String var){
        return getActionsProgList().stream().anyMatch(a -> a.getActionType() == ActionType.VARIABLEDECLARATION && a.getNameVariable().equals(var));
    }

    public boolean hasUndefinedValues() {
        return getActionsProgList().stream().anyMatch(action -> notInt(action.getMeasure()) && !isDeclared(action.getMeasure()));
    }

    private boolean notInt(String val){
        try{
            Integer.parseInt(val);
            return false;
        }catch (Exception e){
            return true;
        }
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

    public boolean hasAnomalies() {
//        return hasDuplicatePenStatus() || hasActionBeforeDecla() || haveDifferenceBetweenMapAndList() || !allAssignationsAreValid() || hasUndefinedValues();
        return hasActionBeforeDecla() || hasDuplicatePenStatus() || !allAssignationsAreValid() || hasUndefinedValues()  || hasUndefinedDeclaration() ;
    }

    private boolean hasActionBeforeDecla() {
        boolean hasActionNotDecla = false;
        if (getActionsProgList().stream().noneMatch(a -> a.getActionType() == ActionType.VARIABLEDECLARATION)) {
            return false;
        }
        for (Action a : getActionsProgList()) {
            if (a.getActionType() != ActionType.VARIABLEDECLARATION) {
                hasActionNotDecla = true;
            } else if (hasActionNotDecla) {
                return true;
            }
        }
        return false;
    }

    public boolean updateActionFromUI(String newName, int idxProg, String value) {
        Action action = getActionProg(idxProg);
        if (action == null) return false;

        if (action.getActionType() == ActionType.VARIABLEDECLARATION) {

            VariableDeclaration vD = (VariableDeclaration) action;
            vD.setNameVariable(newName);
            System.out.println(vD.getNameVariable());
            if (newName == null || newName.equals(vD.getNameVariable())) return true;
            if (!newName.isEmpty() && !vD.isValidVariableName(newName)) return false;

            return true;

        } else if (action.getActionType() == ActionType.VARIABLEASSIGNATION) {
            VariableAssignation vA = (VariableAssignation) action;
            if (!vA.isValidVariableName(newName)) return false;
            vA.setNameVariable(newName);
            vA.setMeasure(value);
            return true;

        } else {
            if (value == null) return false;
            if (!action.isValidMeasure(value)) return false;
            if (!action.isValidVariableName(value)) return false;
            if (action.isValidMeasure(value) || isDeclared(value) || action.isValidVariableName(value)) {
                action.setMeasure(value);
                return true;
            }
            return false;
        }
    }


    public boolean isDeclaration(int idx) {
        return getActionProg(idx) != null && getActionProg(idx).getActionType() == ActionType.VARIABLEDECLARATION;
    }

    public boolean isAssignation(int idx) {
        return getActionProg(idx) != null && getActionProg(idx).getActionType() == ActionType.VARIABLEASSIGNATION;
    }

    public boolean isIncrDecr(int idx) {
        return getActionProg(idx) != null && getActionProg(idx).getActionType() == ActionType.VARIABLEINCRDECR;
    }

    private boolean hasUndefinedDeclaration(){
        return getActionsProgList().stream().anyMatch(action -> action.getActionType() == ActionType.VARIABLEDECLARATION && (action.getNameVariable().isEmpty()));
    }
}
