package campus.osgi.users.random.me.provider;

import java.util.stream.Stream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import campus.osgi.users.api.User;

@Path("/")
public interface RandomUserMeJaxRs {

  @GET
  Stream<User> getUsers(@QueryParam("results") int results);

  @GET
  Stream<User> getUsers(@QueryParam("results") int amount, @QueryParam("gender") String gender);
}
