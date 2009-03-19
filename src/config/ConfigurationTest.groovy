package config

public class ConfigurationTest extends GroovyTestCase{

  void test_config_should_contain_username_and_password_and_contacts(){

    def config = new Configuration()
    config.init("configuration_example.txt")

    assertTrue(config.username.length() > 0)
    assertTrue(config.password.length() > 0)
    assertTrue(config.contacts.size() > 0) 
  }
}