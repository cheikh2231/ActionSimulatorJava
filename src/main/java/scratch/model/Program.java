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
    // Observable sur measure et nameVariable pour que les listeners se déclenchent quand ces propriétés changent
    private ObservableList<Action> actionsProg =
            FXCollections.observableArrayList(
                    action -> new Observable[]{action.measureProperty(), action.nameVariableProperty()}
            );

    public void addAction(int paletteIdx, int progIdx) {
        // si rien de sélectionné (progIdx == -1), on ajoute en fin de liste
        int pos = progIdx == -1 ? size() : progIdx + 1;
        Action action = Actions.getAction(paletteIdx);
        if (action != null) {
            actionsProg.add(pos, action.copy());
            indentAction();
        }
    }

    public void removeAction(int idx) {
        if (!existIndex(idx)) return;
        actionsProg.remove(idx);
        indentAction();
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
            Action a = actionsProg.remove(idx);
            actionsProg.add(idx + 1, a);
        }
    }

    public ObservableList<Action> getActionsProgList() {
        return actionsProg;
    }

    public Action getActionProg(int idx) {
        return existIndex(idx) ? actionsProg.get(idx) : null;
    }

    public void dupplicate(int idx) {
        if (existIndex(idx))
            actionsProg.add(actionsProg.get(idx).copy());
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

    // vrai si toutes les assignations/incréments référencent une variable déclarée avec une mesure non vide
    public boolean declarationCanGetAMeasure() {
        return getActionsProgList().stream()
                .filter(a -> a.getActionType() == ActionType.VARIABLEASSIGNATION
                        || a.getActionType() == ActionType.VARIABLEINCRDECR)
                .allMatch(a -> isDeclared(a.getNameVariable()) && !a.getMeasure().isEmpty());
    }

    private boolean isDeclared(String var) {
        return getActionsProgList().stream()
                .anyMatch(a -> a.getActionType() == ActionType.VARIABLEDECLARATION
                        && a.getNameVariable().equals(var));
    }

    // vrai si une action utilise une valeur qui n'est ni un entier ni une variable déclarée
    public boolean hasUndefinedValues() {
        return getActionsProgList().stream()
                .anyMatch(action -> !action.getMeasure().isEmpty()
                        && notInt(action.getMeasure())
                        && !isDeclared(action.getMeasure()));
    }

    private boolean notInt(String val) {
        try {
            Integer.parseInt(val);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    // vrai si deux stylos consécutifs ont le même état (ex: PEN_DOWN suivi de PEN_DOWN)
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
    // vrai si une même variable est déclarée plus d'une fois
    //ex  {val,val,nb} map sur nameVariable avec distinct donne {val,nb} 2  on verfie si egual a toutes les declaration {val,val,nb} 3
    private boolean hasDuplicateDeclaration() {
        return getActionsProgList().stream()
                .filter(a -> a.getActionType() == ActionType.VARIABLEDECLARATION)
                .map(Action::getNameVariable)
                .distinct().count()
                != getActionsProgList().stream()
                .filter(a -> a.getActionType() == ActionType.VARIABLEDECLARATION)
                .count();
    }

    public boolean hasAnomalies() {
        return hasActionBeforeDecla()
                || hasDuplicatePenStatus()
                || !declarationCanGetAMeasure()
                || hasUndefinedValues()
                || hasUndefinedDeclaration()
                || hasDuplicateDeclaration()
                || hasErrorWithAllRepeat()
                || hasNameVariableUsingByRepeatInScope();
    }

    // vrai si une action non-déclaration apparaît avant une déclaration
    private boolean hasActionBeforeDecla() {
        if (getActionsProgList().stream().noneMatch(a -> a.getActionType() == ActionType.VARIABLEDECLARATION))
            return false;
        boolean hasActionNotDecla = false;
        for (Action a : getActionsProgList()) {
            if (a.getActionType() != ActionType.VARIABLEDECLARATION)
                hasActionNotDecla = true;
            else if (hasActionNotDecla)
                return true;
        }
        return false;
    }

    public boolean updateActionFromUI(String newName, int idxProg, String value) {
        Action action = getActionProg(idxProg);
        if (action == null) return false;

        if (action.getActionType() == ActionType.VARIABLEDECLARATION) {
            VariableDeclaration vD = (VariableDeclaration) action;
            vD.setNameVariable(newName);
            if (newName == null || newName.equals(vD.getNameVariable())) return true;
            if (!newName.isEmpty() && !vD.isValidVariableName(newName)) return false;
            return true;

        } else if (action.getActionType() == ActionType.VARIABLEASSIGNATION
                || action.getActionType() == ActionType.VARIABLEINCRDECR) {
            if (!action.isValidVariableName(newName)) return false;
            action.setNameVariable(newName);
            action.setMeasure(value);
            return true;

        } else {
            // actions de mouvement, stylo, repeat...
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

    // vrai si une déclaration a un nom de variable vide
    private boolean hasUndefinedDeclaration() {
        return getActionsProgList().stream()
                .anyMatch(action -> action.getActionType() == ActionType.VARIABLEDECLARATION
                        && action.getNameVariable().isEmpty());
    }

    // vrai si le nombre de REPEAT ne correspond pas au nombre de END_REPEAT
    public boolean hasErrorToRepeatAndEndSize() {
        return getActionsProgList().stream().filter(a -> a.getActionType() == ActionType.REPEAT).count()
                != getActionsProgList().stream().filter(a -> a.getActionType() == ActionType.ENDREPEAT).count();
    }

    public boolean isEndRepeat(int idx) {
        return getActionProg(idx) != null && getActionProg(idx).getActionType() == ActionType.ENDREPEAT;
    }

    // vrai si la structure REPEAT/END_REPEAT est invalide (mal imbriquée ou déséquilibrée)
    public boolean hasErrorWithAllRepeat() {
        int res = 0;
        if (hasErrorToRepeatAndEndSize()) return true;
        for (int i = 0; i < getActionsProgList().size(); i++) {
            if (getActionProg(i).getActionType() == ActionType.REPEAT)
                res += 1;
            else if (getActionProg(i).getActionType() == ActionType.ENDREPEAT)
                res -= 1;
            // res < 0 = END_REPEAT sans REPEAT correspondant
            if (res < 0) return true;
        }
        return res != 0;
    }

    // calcule et applique le niveau d'indentation de chaque action
    public void indentAction() {
        for (int i = 0; i < getActionsProgList().size(); i++) {
            getActionProg(i).setIndentLevel(nbRepeatBefore(i));
        }
    }

    // compte les REPEAT ouverts avant l'index donné (= niveau d'imbrication)
    public int nbRepeatBefore(int idx) {
        long countRep = getActionsProgList().stream()
                .limit(idx)
                .filter(a -> a.getActionType() == ActionType.REPEAT)
                .count();
        long countEndR = getActionsProgList().stream()
                .limit(idx)
                .filter(a -> a.getActionType() == ActionType.ENDREPEAT)
                .count();

        int count = (int) Math.max(0, countRep - countEndR);

        // END_REPEAT se place un cran en arrière par rapport à son bloc
        if (getActionProg(idx).getActionType() == ActionType.ENDREPEAT)
            return Math.max(0, count - 1);

        return count;
    }

    // construit la liste des enfants de chaque REPEAT (pour le calcul des boucles imbriquées)
    public void buildRepeatChildren() {
        List<ActionRepeat> stack = new ArrayList<>();
        for (Action a : getActionsProgList()) {
            if (a.getActionType() == ActionType.REPEAT) {
                ((ActionRepeat) a).getChildren().clear();
                // ce REPEAT est enfant du REPEAT parent s'il y en a un
                if (!stack.isEmpty()) stack.getLast().addChild(a);
                stack.add((ActionRepeat) a);
            } else if (a.getActionType() == ActionType.ENDREPEAT) {
                if (!stack.isEmpty()) stack.removeLast();
            } else {
                // action normale → enfant du REPEAT le plus profond
                if (!stack.isEmpty()) stack.getLast().addChild(a);
            }
        }
    }

    // vrai si un REPEAT utilise une variable comme mesure qui entre en conflit avec ses enfants
    public boolean hasNameVariableUsingByRepeatInScope() {
        for (Action a : actionsProg) {
            if (a.getActionType() == ActionType.REPEAT) {
                if (((ActionRepeat) a).hasConflictWithChildren())
                    return true;
            }
        }
        return false;
    }
}