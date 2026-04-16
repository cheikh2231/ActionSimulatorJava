package scratch.model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Program {
    private ObservableList<Action> actionsProg =
            FXCollections.observableArrayList(
                    action -> new Observable[]{action.measureProperty(), action.nameVariableProperty()}
            );

    public void addAction(int idx, int pos) {
        Action action = Actions.getAction(idx);
        if (action != null) {
            actionsProg.add(pos, action.copy());
            indentAction();
        }
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
            indentAction();
        }
    }

    public boolean canIncrDecr(String measureProperty, int idx) {
        return existIndex(idx) && getActionProg(idx).canIncrDecr(measureProperty);
    }

    public void up(int idx) {
        if (idx > 0 && idx < actionsProg.size()) {
            Action a = actionsProg.remove(idx);
            actionsProg.add(idx - 1, a);
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
            up(idx+1);
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


    public boolean declarationCanGetAMeasure() {
        return getActionsProgList().stream()
                .filter(a -> a.getActionType() == ActionType.VARIABLEASSIGNATION || a.getActionType() == ActionType.VARIABLEINCRDECR)
                .allMatch(a -> isDeclared(a.getNameVariable()) && !a.getMeasure().isEmpty());
    }

    private boolean isDeclared(String var) {
        return getActionsProgList().stream().anyMatch(a -> a.getActionType() == ActionType.VARIABLEDECLARATION && a.getNameVariable().equals(var));
    }

    public boolean hasUndefinedValues() {
        return getActionsProgList().stream().anyMatch(action -> notInt(action.getMeasure()) && !isDeclared(action.getMeasure()));
    }

    private boolean notInt(String val) {
        try {
            Integer.parseInt(val);
            return false;
        } catch (Exception e) {
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
        return hasActionBeforeDecla() || hasDuplicatePenStatus() || !declarationCanGetAMeasure() || hasUndefinedValues()
                || hasUndefinedDeclaration() || hasErrorWithAllRepeat();
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

        } else if (action.getActionType() == ActionType.VARIABLEASSIGNATION || action.getActionType() == ActionType.VARIABLEINCRDECR) {
            if (!action.isValidVariableName(newName)) return false;
            action.setNameVariable(newName);
            action.setMeasure(value);
            return true;
        } else {
            if (value == null) return false;
            if (!action.isValidMeasure(value)) return false;
            if (!action.isValidVariableName(value)) return false;
            if (action.isValidMeasure(value) || isDeclared(value) || action.isValidVariableName(value)) {
                action.setMeasure(value);
                return true;
            }

        }
        return false;
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

    private boolean hasUndefinedDeclaration() {
        return getActionsProgList().stream().anyMatch(action -> action.getActionType() == ActionType.VARIABLEDECLARATION && (action.getNameVariable().isEmpty()));
    }


    public boolean hasErrorToRepeatAndEndSize(){
        return getActionsProgList().stream().filter(a->a.getActionType()==ActionType.REPEAT).count() != getActionsProgList().stream().filter(a->a.getActionType()==ActionType.ENDREPEAT).count();
    }

    public boolean isEndRepeat(int idx){
        return getActionProg(idx) != null && getActionProg(idx).getActionType() == ActionType.ENDREPEAT;
    }

    // r er
    public boolean hasErrorWithAllRepeat(){
        int res = 0;
        if (hasErrorToRepeatAndEndSize()) return true;
        for (int i = 0; i < getActionsProgList().size(); i++) {
            if (getActionProg(i).getActionType()==ActionType.REPEAT){
                res+=1;
            }else if (getActionProg(i).getActionType()==ActionType.ENDREPEAT)
                res -= 1;
            if (res<0)
                return true;
        }
        return res != 0;
    }

// r
    // r
    // fr
    public void indentAction() {
        for (int i = 0; i < getActionsProgList().size(); i++) {
            getActionProg(i).setIndentLevel(nbRepeatBefore(i));
        }
    }


    public int nbRepeatBefore(int idx) {
        long countRep = getActionsProgList().stream()
                .limit(idx)  // éléments strictement avant idx
                .filter(a -> a.getActionType() == ActionType.REPEAT)
                .count();

        long countEndR = getActionsProgList().stream()
                .limit(idx)
                .filter(a -> a.getActionType() == ActionType.ENDREPEAT)
                .count();
        int count = (int)(countRep - countEndR);
        if (count < 0) count = 0;

        // Un ENDREPEAT ferme le bloc  il s'affiche un cran en arrière
        if (getActionProg(idx).getActionType() == ActionType.ENDREPEAT) {
            return Math.max(0, count - 1);
        }

        return count;
    }


    public void buildRepeatChildren() {
        List<ActionRepeat> list = new ArrayList<>();

        for (Action a : getActionsProgList()) {
            if (a.getActionType() == ActionType.REPEAT) {
                ((ActionRepeat) a).getChildren().clear();
                if (!list.isEmpty()) {
                    list.getLast().addChild(a); // le REPEAT interne est enfant du parent
                }
                list.add((ActionRepeat) a);
            } else if (a.getActionType() == ActionType.ENDREPEAT) {
                if (!list.isEmpty()) list.removeLast();
            } else {
                if (!list.isEmpty()) {
                    list.getLast().addChild(a); // seulement le repeat le plus profond
                }
            }
        }
    }

}
