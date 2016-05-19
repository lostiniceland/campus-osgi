package campus.osgi.fx.launch.internal;

import campus.osgi.fx.stage.internal.StageService;
import javafx.stage.Stage;

public class FxStageService implements StageService {

  private final Stage stage;

  public FxStageService(Stage stage) {
    this.stage = stage;
  }

  @Override
  public Stage getStage() {
    return this.stage;
  }
}
