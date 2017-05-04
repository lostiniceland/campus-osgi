package campus.osgi.docu;

import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentationExtender implements BundleListener, BundleActivator {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private ServiceTracker<BundleDocumentation, BundleDocumentation> tracker;

  @Override
  public void start(BundleContext context) throws Exception {
    context.addBundleListener(this);
    // ServiceTracker needed because during startup the internal Service is not yet available
    tracker = new ServiceTracker<>(context, BundleDocumentation.class,
        new ServiceTrackerCustomizer<BundleDocumentation, BundleDocumentation>() {
          
          @Override
          public BundleDocumentation addingService(ServiceReference<BundleDocumentation> reference) {
            BundleDocumentation service = (BundleDocumentation) context.getService(reference);
            if (service != null) {
              fullBundleScan(context);
              return service;
            }
            return null;
          }

          @Override
          public void modifiedService(ServiceReference<BundleDocumentation> reference, BundleDocumentation service) {
            // not interesting
          }

          @Override
          public void removedService(ServiceReference<BundleDocumentation> reference, BundleDocumentation service) {
            // not interesting
          }

        });

    tracker.open();
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    context.removeBundleListener(this);
    tracker.close();
  }

  @Override
  public void bundleChanged(BundleEvent event) {
    Optional<URL> url = Optional.ofNullable(getDocumentationURL(event.getBundle()));;
    if (url.isPresent() && event.getType() == BundleEvent.STARTED) {
      try (Scanner s = new Scanner(url.get().openStream(), "UTF-8").useDelimiter("\\A")) {
        getServiceAndExecute(servlet -> servlet.addBundleDocumentation(event.getBundle().getSymbolicName(),
            s.hasNext() ? s.next() : ""));
      } catch (Exception e) {
        logger.error("Error reading docu", e);
      }
    } else if (url.isPresent() && event.getType() == BundleEvent.STOPPED) {
      getServiceAndExecute(servlet -> servlet.removeBundleDocumentation(event.getBundle().getSymbolicName()));
    }

  }

  private URL getDocumentationURL(Bundle bundle) {
    return bundle.getEntry("docu/service.txt");
  }


  private void getServiceAndExecute(Consumer<BundleDocumentation> function) {
    // hook into OSGi-Framework
    final BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
    // get-service, execute function, and unget-service
    ServiceReference<BundleDocumentation> serviceRef = context.getServiceReference(BundleDocumentation.class);
    if (serviceRef != null) {
      BundleDocumentation servlet = context.getService(serviceRef);
      if (servlet != null) {
        function.accept(servlet);
        servlet = null;
      }
      context.ungetService(serviceRef);
    }
  }

  private void fullBundleScan(BundleContext context) {
    Arrays.stream(context.getBundles()).forEach(bundle -> {
      Optional<URL> url = Optional.ofNullable(getDocumentationURL(bundle));
      if (url.isPresent()) {
        try (Scanner s = new Scanner(url.get().openStream(), "UTF-8").useDelimiter("\\A")) {
          getServiceAndExecute(
              service -> service.addBundleDocumentation(bundle.getSymbolicName(), s.hasNext() ? s.next() : ""));
        } catch (Exception e) {
          logger.error("Error reading docu", e);
        }
      }
    });
  }

}
