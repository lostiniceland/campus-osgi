package campus.osgi.docu;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class BundleDocumentationService implements BundleDocumentation {

  private Logger logger = LoggerFactory.getLogger(getClass());
  
  private Map<String, String> docuEntries = new ConcurrentHashMap<>();
  
  @Override
  public void addBundleDocumentation(String bsn, String docutext){
    docuEntries.put(bsn, docutext);
    logger.info("Documentation added for Bundle {}.", bsn);
  }

  @Override
  public void removeBundleDocumentation(String bsn) {
    docuEntries.remove(bsn);
    logger.info("Documentation removed in Bundle {}.", bsn);
  }
  
  @Override
  public Stream<Entry<String,String>> getEntries(){
    return docuEntries.entrySet().stream();
  }
}
