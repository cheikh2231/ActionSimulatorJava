package scratch.viewmodel;

import scratch.model.Program;
import scratch.model.SceneActions;

public class MainViewModel {
    private final ActionViewModel actionVm;

    public MainViewModel(Program program, SceneActions sceneActions) {
        this.actionVm = new ActionViewModel(program,sceneActions);
    }

    public ActionViewModel getActionVm() {
        return actionVm;
    }


}
