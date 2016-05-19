package campus.osgi.users.fixed.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;

import campus.osgi.users.api.User;
import campus.osgi.users.api.UserService;

@Component
public class FixedUserProvider implements UserService {

  private static final List<User> users = new ArrayList<>(3);

  static {
    users.add(new User("Alex Honold", "male", "alex.honold@dontknow.us"));
    users.add(new User("Tracy Hanah", "female", "tracey.hanah@dontknow.us"));
    users.add(new User("Wade Simmons", "male", "wade@dontknow.us"));
  }
  
  
  protected static void addUser(User user){
    if(user == null){
      throw new IllegalArgumentException("User must not be null!");
    }
    users.add(user);
  }

  @Override
  public Stream<User> getUsers() {
    return users.stream();
  }

}
