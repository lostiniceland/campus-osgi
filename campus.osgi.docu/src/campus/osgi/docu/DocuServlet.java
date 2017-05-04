package campus.osgi.docu;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
    service = Servlet.class,
    property = "osgi.http.whiteboard.servlet.pattern=/docu"
)
public class DocuServlet extends GenericServlet {

  private static final long serialVersionUID = 1L;
  
  @Reference
  private BundleDocumentation docuService;
  
  @Override
  public void service(ServletRequest request, ServletResponse resp) throws ServletException, IOException {
    StringBuilder content = new StringBuilder();
    content.append("<html><body>");
    content.append("<h2>Service Documentation</h2>");
    docuService.getEntries().forEach(entry -> content.append("<hr/><h3>").append(entry.getKey()).append("</h3>").append("<div>").append(entry.getValue()).append("</div>"));
    content.append("</body></html>");
    resp.getWriter().print(content.toString());    
  }

  
  
}
