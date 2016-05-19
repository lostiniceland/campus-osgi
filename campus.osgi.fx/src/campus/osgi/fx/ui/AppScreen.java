package campus.osgi.fx.ui;

import org.osgi.annotation.versioning.ConsumerType;

import javafx.scene.Node;

@ConsumerType
public interface AppScreen {

  String getName();

  Node getContent();

  int getPosition();
}
