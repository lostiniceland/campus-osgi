package campus.osgi.fx.tabs;


import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import campus.osgi.fx.ui.AppScreen;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


@Component
public class MainTab implements AppScreen {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public String getName() {
    return "Main";
  }

  @Override
  public Node getContent() {
    VBox vbox = new VBox();
    vbox.setSpacing(5);
    vbox.setPadding(new Insets(10, 10, 10, 10));

    Button button = new Button("Log something");
    button.setOnAction(e -> logger.info("Button was triggered"));
    vbox.getChildren().add(button);

    Button button2 = new Button("Toggle");
    button2.setOnAction(e -> button.setDisable(button.isDisable() ? false : true));
    vbox.getChildren().add(button2);
    return vbox;
  }

  @Override
  public int getPosition() {
    return 0;
  }
}
