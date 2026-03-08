package scratch.viewmodel;

import scratch.model.Programme;
import scratch.model.SceneActions;

public class MainViewModel {
    private final ActionViewModel actionVm;

    public MainViewModel(Programme programme, SceneActions sceneActions) {
        this.actionVm = new ActionViewModel(programme,sceneActions);
    }

    public ActionViewModel getActionVm() {
        return actionVm;
    }


}
