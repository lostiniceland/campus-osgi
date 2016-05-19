package campus.osgi.fx.launch.internal;

import java.util.concurrent.Executors;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.sun.javafx.application.HostServicesDelegate;

import campus.osgi.fx.stage.internal.StageService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

@Component
public class App extends Application {

  @Activate
  public void startBundle() {
    Executors.defaultThreadFactory().newThread(() -> {
      Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
      launch();
    }).start();
  }

  @Deactivate
  public void stopBundle() {
    Platform.exit();
  }


  @Override
  public void start(Stage primaryStage) throws Exception {
    BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

    context.registerService(StageService.class, new FxStageService(primaryStage), null);

    if ("true".equals(System.getProperty("open-config"))) {
      HostServicesDelegate hostServices = HostServicesDelegate.getInstance(this);
      hostServices.showDocument("http://localhost:8080/system/console/configMgr");
    }
  }

  @Override
  public void stop() throws Exception {
    super.stop();
  }


}
