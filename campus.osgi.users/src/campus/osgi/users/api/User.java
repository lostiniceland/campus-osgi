package campus.osgi.users.api;

public class User {

  private String name;
  private String gender;
  private String email;

  public User(String name, String gender, String email) {
    super();
    this.name = name;
    this.gender = gender;
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public String getGender() {
    return gender;
  }

  public String getEmail() {
    return email;
  }


  @Override
  public String toString() {
    return "name: " + name + "\n" + "gender: " + gender + "\n" + "email: " + email + "\n";
  }
}
