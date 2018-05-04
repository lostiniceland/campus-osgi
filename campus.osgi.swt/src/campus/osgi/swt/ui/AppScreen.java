package campus.osgi.swt.ui;

import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface AppScreen {

  String getName();

//  Node getContent();

  int getPosition();
}
