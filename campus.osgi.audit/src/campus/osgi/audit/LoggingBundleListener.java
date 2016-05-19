package campus.osgi.audit;

import java.util.Optional;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingBundleListener implements BundleActivator, BundleListener {

  private transient Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void start(BundleContext context) throws Exception {
    logger.info("Auditing started.");
    context.addBundleListener(this);
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    context.removeBundleListener(this);
    logger.info("Auditing stopped.");
  }

  @Override
  public void bundleChanged(BundleEvent event) {
    Optional<String> eventString = decodeEvent(event.getType());
    if (eventString.isPresent()) {
      logger.info("Bundle {} with version {} has been {}", event.getBundle().getSymbolicName(),
          event.getBundle().getVersion(), eventString.get());
    }
  }

  private Optional<String> decodeEvent(int type) {
    switch (type) {
      case BundleEvent.INSTALLED:
        return Optional.of("installed");
      case BundleEvent.STARTED:
        return Optional.of("started");
      case BundleEvent.STOPPED:
        return Optional.of("stopped");
      case BundleEvent.UNINSTALLED:
        return Optional.of("uninstalled");
      case BundleEvent.UPDATED:
        return Optional.of("updated");
      default:
        return Optional.empty();
    }
  }
}
