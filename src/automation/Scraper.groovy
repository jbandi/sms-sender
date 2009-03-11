package automation

import com.gargoylesoftware.htmlunit.*
import config.Configuration

@Grab(group='net.sourceforge.htmlunit', module='htmlunit', version='[2.4,)')
class Scraper {

  Configuration configuration;

  public Scraper(Configuration config){
    this.configuration = config;
  }

  public String getRemainingUnits() {
    def reporter = new Reporter()

    reporter.report "Retrieving login page ..."

    //def webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_7_0 , "proxy1.pnet.ch", 3128)
    def webClient = new WebClient(BrowserVersion.FIREFOX_2)
    webClient.setThrowExceptionOnScriptError(false)

    def page = webClient.getPage('https://www.orange.ch/vrtmyaccount?language=en&')
    assert page
    assert page.titleText == 'Orange :: my account'
    reporter.report("Login page retrieved successfully.")

    reporter.report "Setting username and password..."
    def loginPage = page.getFrames().get(0).getEnclosedPage()
    def loginForms = loginPage.getForms()
    def loginForm = loginForms.get(0)
    def username = loginForm.getInputByName('username')
    def passwordField = loginForm.getInputByName('password')

    username.setValueAttribute(configuration.username)
    passwordField.setValueAttribute(configuration.password)
    reporter.report "Username and password set."

    reporter.report "Submitting form ..."
    def firstPage = loginForm.submit()
    assert firstPage
    assert firstPage.titleText == 'Welcome to Orange'
    reporter.report "Login successfull!"

    def html = new XmlSlurper().parseText(firstPage.asXml())

    def info = html.'**'.grep {it.@id == 'text2'}

    info*.text()*.trim().each {reporter.report it }
  }
}
