package config


class Configuration {

  String username
  String password
  List contacts

  public void init(String cfgFilePath) {
    def cfgFile = new File(cfgFilePath)
    assert cfgFile

    username = (cfgFile.text =~ /username=(.*)/)[0][1]
    password = (cfgFile.text =~ /password=(.*)/)[0][1]

    contacts = ((cfgFile.text =~ /(?ms)contacts:(.*)/)[0][1] =~ /(.*),(.*)/).collect{new Contact(name: it[1], number: it[2])}

  }
}



