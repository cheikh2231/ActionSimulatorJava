package scratch.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scratch.model.*;


public class ViewModelAction {

    private final Programme programme;
    private ObservableList<Action> actionsPalette = Actions.getActions();
    private ObservableList<Action> actionScene = FXCollections.observableArrayList();
    private IntegerProperty paletteIndex = new SimpleIntegerProperty(-1);
    private IntegerProperty progIndex = new SimpleIntegerProperty(-1);
    private StringProperty mesureProperty = new SimpleStringProperty();
    private BooleanProperty isvalidMesureProperty = new SimpleBooleanProperty(true);
    private BooleanProperty isDetailableProperty = new SimpleBooleanProperty(false);
    private ObjectProperty<Action> actionSelected= new SimpleObjectProperty<>();
    private BooleanProperty hasChargerScene = new SimpleBooleanProperty(false);

    public ObjectProperty<Action> actionSelectedProperty() {
        return actionSelected;
    }

    public ObservableList<Action> getActionsProg() {
        return programme.getActionsProg();
    }

    public ObservableList<Action> getActionPalette() {
        return FXCollections.unmodifiableObservableList(actionsPalette);
    }

    public ObservableList<Action> getActionScene() {
        return actionScene;
    }

    public void addToListScene(){
        this.actionScene.addAll(programme.getActionsProg());
    }

    public ViewModelAction(Programme programme) {
        this.programme = programme;
        listeners();
    }


    public IntegerProperty paletteIndexProperty() {
        return paletteIndex;
    }

    public IntegerProperty progIndexProperty() {
        return progIndex;
    }

    public StringBinding getSelectedActionProgDetailsName() {

        return Bindings.createStringBinding(() -> {
                    Action actionSelected = programme.getActionProg(progIndex.get());
                    if (actionSelected != null) {
                        return actionSelected.getDetailsName();
                    }
                    return " No action selected ";
                }, progIndex ,getActionsProg()
        );

    }

    public StringBinding getSelectedActionProgDetailsUnity(){
        return Bindings.createStringBinding(() -> {
                    Action actionSelected = programme.getActionProg(progIndex.get());
                    if (actionSelected != null) {
                        return actionSelected.getUnity();
                    }
                    return "";
                }, progIndex , getActionsProg()
        );
    }

    public void addPaletteToProg() {
        int idx = paletteIndexProperty().get();
        programme.ajouterAction(idx);
    }

    public void setIsDetailableProperty(boolean isDetailableProperty) {
        this.isDetailableProperty.set(isDetailableProperty);
    }

    public BooleanProperty IsDetailableProperty(){
        return isDetailableProperty;
    }


    public StringProperty getMesureProperty() {
        return mesureProperty;
    }

    public void listeners() {
       actionSelectedProperty().addListener((obs, o, n) -> {
            if (n != null){
                mesureProperty.set(String.valueOf(n.getMesure()));
                this.setIsDetailableProperty(n.isDetailable());
            }
            else {
                mesureProperty.setValue("");
                this.setIsDetailableProperty(false);
            }
            isvalidMesureProperty.set(true);
        });

        mesureProperty.addListener((obs, o, n) -> {
            if (n.isEmpty()){
                isvalidMesureProperty.set(false);
                return;
            }
            try {
                Action action = programme.getActionProg(progIndex.get());
                if(action!=null){
                    int val = Integer.parseInt(n);
                    setMesure(val);
                    isvalidMesureProperty.set(action.isValidMesure(val));
                }
            }catch (NumberFormatException e){
                isvalidMesureProperty.set(false);
            }

        });
    }

    public void setMesure(int mesure) {
        Action action = programme.getActionProg(progIndex.get());
        if (action != null){
            action.setMesure(mesure);
        }
    }

    public void removeAction() {
        System.out.println(progIndex.get());
        programme.removeAction(progIndex.get());
        System.out.println(progIndex.get());
    }

    public void clearAllActions() {
        programme.clear();
    }

    public BooleanBinding isEmptyActionProg() {
        return Bindings.createBooleanBinding(programme::isEmpty, programme.getActionsProg());
    }

    public void duplicateAction() {
        programme.dupplicate(progIndex.get());
    }

    public BooleanBinding isSelectedActionProg() {
        return progIndex.greaterThanOrEqualTo(0);
    }

    public BooleanBinding canMoveUp() {
       return progIndex.greaterThan(0);
    }

    public BooleanBinding canMoveDown() {
        return Bindings.createBooleanBinding(() -> {
            return programme.canMoveDown(progIndex.get());
        },progIndex,getActionsProg());
    }

    public void moveUp(){
        if (canMoveUp().get()) {
            programme.up(progIndex.get());
            progIndex.set(progIndex.get() - 1);
        }
    }

    public void moveDown(){
        if (canMoveDown().get()) {
            programme.down(progIndex.get());
            progIndex.set(progIndex.get() + 1);
        }
    }

    public BooleanProperty isValidMesureProperty() {
        return isvalidMesureProperty;
    }


    public BooleanBinding hasAnomalies(){
        return Bindings.createBooleanBinding(()->{
            return hasDuplicate() || isEmptyActionProg().get();
        },getActionsProg());
    }

    public BooleanBinding hasActionsProgChange(){
        return Bindings.createBooleanBinding(()->{
            if (hasChargerScene.get()){
                hasChargerScene.set(false);
                return true;
            }
            return false;
        },getActionsProg());
    }

    public void charger(){
        setHasChargerScene(true);
        progIndex.set(-1);
    }

    public void executer(){
        if(!programme.isEmpty())
            progIndex.set(0);
    }

    public void reinitialiser(){
        progIndex.set(0);
        // vider le tableau de dessin et recharger la scene
    }

    private boolean hasDuplicate(){
        boolean abaisser = true;
        for (Action action: getActionsProg() ) {
               if (action instanceof ActionStylo actionStylo){
                   if (actionStylo.getEtatStyloAbaisser() == abaisser)
                       return true;
                   else
                       abaisser = actionStylo.getEtatStyloAbaisser();
               }
        }
        return false;
    }

    public void setHasChargerScene(boolean hasChargerScene) {
        this.hasChargerScene.set(hasChargerScene);
    }

    public BooleanProperty hasChargerSceneProperty() {
        return hasChargerScene;
    }
}