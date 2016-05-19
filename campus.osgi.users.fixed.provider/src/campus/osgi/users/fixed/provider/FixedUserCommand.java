package campus.osgi.users.fixed.provider;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.service.component.annotations.Component;

import campus.osgi.users.api.User;

@Component(
    service = Object.class,
    property = {
    CommandProcessor.COMMAND_SCOPE + ":String=users",
    CommandProcessor.COMMAND_FUNCTION + ":String=add"
})
public class FixedUserCommand {
  
  public void add(String name, String gender, String email){
    FixedUserProvider.addUser(new User(name, gender, email));
  }
}
