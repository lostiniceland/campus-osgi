package campus.osgi.swt.ui.internal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabItem;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import campus.osgi.swt.stage.internal.StageService;
import campus.osgi.swt.ui.AppScreen;

@Component
public class UI {
  private BundleContext context;
  private volatile StageService stageService;
  private volatile TabItem tabPane;
  /** needed to select the previous tab on refresh */
  int currentlySelectedTab;

  private final Map<ServiceReference<AppScreen>, AppScreen> screens =
      new ConcurrentHashMap<ServiceReference<AppScreen>, AppScreen>();

  @Activate
  public void start(BundleContext context) {
    this.context = context;
    Display.getCurrent().asyncExec(this::buildUI);
  }

  private void buildUI() {
//    Stage primaryStage = stageService.getStage();
//    FXMLLoader fxmlLoader = new FXMLLoader(context.getBundle().getResource("application.fxml"));
//    try {
//      Scene scene = fxmlLoader.load();
//      primaryStage.setScene(scene);
//    } catch (IOException e1) {
//      e1.printStackTrace();
//      // nothing we can do -- stop VM
//      System.exit(1);
//    }
//
//    primaryStage.setTitle("Dynamic Tabs");
//    tabPane = (TabPane) primaryStage.getScene().getRoot();
//    tabPane.getSelectionModel().selectedItemProperty().addListener(
//        (observableValue, oldTab, newTab) -> currentlySelectedTab = tabPane.getSelectionModel().getSelectedIndex());
//
//    screens.values().forEach(this::createTab);
//
//    // user different stylesheet for presentation
//    if ("true".equals(System.getProperty("presentation"))) {
//      primaryStage.getScene().getStylesheets()
//          .add(FrameworkUtil.getBundle(this.getClass()).getResource("application-presentation.css").toExternalForm());
//    } else {
//      primaryStage.getScene().getStylesheets()
//          .add(FrameworkUtil.getBundle(this.getClass()).getResource("application.css").toExternalForm());
//    }
//
//
//    primaryStage.setOnCloseRequest(event -> {
//      Platform.runLater(() -> {
//        // stopping osgi container
//        BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
//        try {
//          Framework fw = context.getBundle(0).adapt(Framework.class);
//          fw.stop();
//        } catch (Exception e) {
//          System.err.println("Error stopping OSGI-Container...killing VM now");
//          e.printStackTrace();
//          // hard way
//          System.exit(1);
//        }
//      });
//    });
//    primaryStage.show();
  }

//  private void createTab(AppScreen screen) {
//    Tab tab = new Tab(screen.getName());
//    tab.setContent(screen.getContent());
//    tab.setClosable(false);
//    if (screen.getPosition() < tabPane.getTabs().size()) {
//      tabPane.getTabs().add(screen.getPosition(), tab);
//    } else {
//      tabPane.getTabs().add(tab);
//    }
//    tabPane.getSelectionModel().select(tabPane.getTabs().size() - 1);
//    stageService.getStage().show();
//
//  }

  @Reference(unbind = "removeScreen", service = AppScreen.class, cardinality = ReferenceCardinality.MULTIPLE,
      policy = ReferencePolicy.DYNAMIC)
  public void addScreen(ServiceReference<AppScreen> ref, AppScreen screen) {
    if (tabPane != null) {
      Display.getCurrent().asyncExec(() -> {
//        createTab(screen);
//        tabPane.getSelectionModel().select(currentlySelectedTab);
      });
    }
    screens.put(ref, screen);
  }

  @Reference
  public void bindStageService(StageService stageService) {
    this.stageService = stageService;
  }

  public void removeScreen(ServiceReference<AppScreen> ref, AppScreen screen) {
	  Display.getCurrent().asyncExec(() -> {
      AppScreen remove = screens.remove(ref);
//      Optional<Tab> findAny =
//          tabPane.getTabs().stream().filter(t -> remove != null && t.getText().equals(remove.getName())).findAny();
//      if (findAny.isPresent()) {
//        tabPane.getTabs().remove(findAny.get());
//      }
    });
  }

}
