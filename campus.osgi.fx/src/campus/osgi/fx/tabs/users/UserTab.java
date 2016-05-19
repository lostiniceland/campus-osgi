package campus.osgi.fx.tabs.users;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import campus.osgi.fx.ui.AppScreen;
import campus.osgi.users.api.ServiceException;
import campus.osgi.users.api.User;
import campus.osgi.users.api.UserService;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

@Component
public class UserTab implements AppScreen {
  // dynamic services should be volatile
  private volatile UserService userService;
  private Button button;

  @Override
  public String getName() {
    return "Users";
  }

  @SuppressWarnings("unchecked")
  @Override
  public Node getContent() {
    VBox vbox = new VBox();
    vbox.setSpacing(5);
    vbox.setPadding(new Insets(10, 10, 10, 10));

    TableView<User> table = new TableView<>();
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    TableColumn<User, String> name = new TableColumn<>("Name");
    name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
    TableColumn<User, String> gender = new TableColumn<>("Gender");
    gender.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getGender()));
    TableColumn<User, String> email = new TableColumn<>("EMail");
    email.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getEmail()));

    table.getColumns().addAll(name, gender, email);

    button = new Button("Load Users");
    button.setOnAction(e -> {
      table.getItems().clear(); 
      loadUsers(table);
    });
    // it is possible that no service is bound during startup
    if (userService == null) {
      button.setDisable(true);
    } else {
      button.setDisable(false);
    }

    vbox.getChildren().addAll(table, button);
    return vbox;
  }

  private void loadUsers(final TableView<User> table) {
    ObservableList<User> data = FXCollections.observableArrayList();
    try {
      userService.getUsers().forEach(user -> data.add(user));
    } catch (ServiceException ex) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setTitle("Error loading Users");
      alert.setHeaderText(ex.getMessage());
      alert.setContentText(ex.getCause().getMessage());
      alert.showAndWait();
    }

    table.setItems(data);
  }

  @Override
  public int getPosition() {
    return 10;
  }

  @Reference(unbind = "unbindUserService", cardinality = ReferenceCardinality.OPTIONAL,
      policy = ReferencePolicy.DYNAMIC, policyOption = ReferencePolicyOption.GREEDY)
  void bindUserService(UserService service) {
    this.userService = service;
    if (button != null)
      button.setDisable(false);
  }

  void unbindUserService(UserService service) {
    // multiple services at play...the local field might already be bound to another service
    if (this.userService == service) {
      this.userService = null;
    }
    if (userService == null && button != null) {
      button.setDisable(true);
    }
  }

}

