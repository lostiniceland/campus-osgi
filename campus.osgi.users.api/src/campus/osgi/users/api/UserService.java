package campus.osgi.users.api;

import java.util.stream.Stream;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface UserService {

  Stream<User> getUsers() throws ServiceException;

}
