package campus.osgi.users.random.me.provider;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.stream.Stream;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

import com.eclipsesource.jaxrs.consumer.ConsumerFactory;

import campus.osgi.users.api.ServiceException;
import campus.osgi.users.api.User;
import campus.osgi.users.api.UserService;

@ObjectClassDefinition(pid = "RandomUser", name = "Campus-OSGI: Randomuser.Me Configuration",
    description = "Configures the communication with http://api.randomuser.me")
@interface RandomUserConfig {
  @AttributeDefinition(description = "Amount of random users")
  int amount() default 5;

  @AttributeDefinition(description = "Limit gender of random users",
      options = {@Option(label = "Male", value = "male"), @Option(label = "Female", value = "female")})
  String gender();
}


@ObjectClassDefinition(pid = "Proxy", name = "Campus-OSGI: Proxy Configuration",
    description = "Configure a proxy for outgoing connections")
@interface ProxyConfig {
  @AttributeDefinition(name = "Proxy-Host")
  String proxyHost();

  @AttributeDefinition(name = "Proxy-Port")
  String proxyPort();

  @AttributeDefinition(name = "Proxy-User")
  String proxyUser();

  @AttributeDefinition(name = "Proxy-Password", type = AttributeType.PASSWORD)
  String proxyPassword();
}


@Component(property = {Constants.SERVICE_RANKING + ":Integer=100"}, configurationPid = {"RandomUser", "Proxy"})
public class RandomMeUserServiceProvider implements UserService {

  private RandomUserMeJaxRs client;
  private int amount = 3;
  private String gender;

  @Activate
  public void activate(RandomUserConfig config, ProxyConfig proxyConfig) {
    amount = config.amount();
    gender = config.gender();
    String baseUrl = "http://api.randomuser.me";
    ClientConfig clientConfig = new ClientConfig().register(new RandomUserMeJsonMapper());
    clientConfig.property(ClientProperties.CONNECT_TIMEOUT, 1000);
    clientConfig.property(ClientProperties.READ_TIMEOUT, 1000);

    
    if (proxyConfig.proxyHost() != null && proxyConfig.proxyPort() != null) {
//       clientConfig.property(ClientProperties.PROXY_URI, proxyConfig.proxyHost() + ":" +
//       proxyConfig.proxyPort());
      System.setProperty("http.proxyHost", proxyConfig.proxyHost());
      System.setProperty("http.proxyPort", proxyConfig.proxyPort());
    }
    if (proxyConfig.proxyUser() != null && proxyConfig.proxyPassword() != null) {
//       clientConfig.property(ClientProperties.PROXY_USERNAME, proxyConfig.proxyUser());
      System.setProperty("http.proxyUser", proxyConfig.proxyUser());
      System.setProperty("http.proxyPassword", proxyConfig.proxyPassword());

      Authenticator.setDefault(new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(proxyConfig.proxyUser(), proxyConfig.proxyPassword().toCharArray());
        }
      });
    }
    client = ConsumerFactory.createConsumer(baseUrl, clientConfig, RandomUserMeJaxRs.class);
  }

  @Override
  public Stream<User> getUsers() throws ServiceException {
    try {
      if (gender != null && gender.trim().length() > 0) {
        return client.getUsers(amount, gender);
      } else {
        return client.getUsers(amount);
      }
    } catch (Exception ex) {
      throw new ServiceException("Could not retrive data from 'http://api.randomuser.me'", ex);
    }
  }

}
