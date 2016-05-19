package campus.osgi.users.random.me.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import campus.osgi.users.api.User;

@Provider
public class RandomUserMeJsonMapper implements MessageBodyReader<Stream<User>> {

  @Override
  public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return Stream.class.isAssignableFrom(type);
  }

  @Override
  public Stream<User> readFrom(Class<Stream<User>> type, Type genericType, Annotation[] annotations,
      MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
      throws IOException, WebApplicationException {
    try {
      List<User> results = new ArrayList<>();
      JSONObject rootObject = new JSONObject(convertStreamToString(entityStream));
      JSONArray resultArray = rootObject.getJSONArray("results");
      for (int i = 0; i < resultArray.length(); i++) {
        JSONObject userPart = resultArray.getJSONObject(i);
        JSONObject namePart = userPart.getJSONObject("name");


        results.add(new User(namePart.getString("first") + " " + namePart.getString("last"),
            userPart.getString("gender"), userPart.getString("email")));
      }

      return results.stream();
    } catch (JSONException shouldNotHappen) {
      throw new IllegalStateException(shouldNotHappen);
    }
  }

  public String convertStreamToString(InputStream is) throws IOException {
    Writer writer = new StringWriter();
    char[] buffer = new char[1024];
    try {
      Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      int n;
      while ((n = reader.read(buffer)) != -1) {
        writer.write(buffer, 0, n);
      }
    } finally {
      is.close();
    }
    return writer.toString();
  }

}
