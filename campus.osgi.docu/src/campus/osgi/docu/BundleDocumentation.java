package campus.osgi.docu;

import java.util.Map.Entry;
import java.util.stream.Stream;

public interface BundleDocumentation {

  void addBundleDocumentation(String bsn, String docutext);

  void removeBundleDocumentation(String bsn);

  Stream<Entry<String,String>> getEntries();

}
