package automation

import com.gargoylesoftware.htmlunit.*
import com.gargoylesoftware.htmlunit.html.*
import config.Configuration

@Grab(group='net.sourceforge.htmlunit', module='htmlunit', version='[2.4,)')
class Sender {

  Configuration configuration;

  public Sender(Configuration config) {
    this.configuration = config;
  }

  void sendSms(String number, String text) {

    //def webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_7_0 , "proxy1.pnet.ch", 3128)
    def webClient = new WebClient(BrowserVersion.FIREFOX_2)
    webClient.setThrowExceptionOnScriptError(false)


    def page = webClient.getPage('https://www.orange.ch/vrtmyaccount?language=en&')
    println "Page loaded!"
    println page


    def loginPage = page.getFrames().get(0).getEnclosedPage()
    def loginForms = loginPage.getForms()
    def loginForm = loginForms.get(0)
    def username = loginForm.getInputByName('username')
    def passwordField = loginForm.getInputByName('password')

    username.setValueAttribute(configuration.username)
    passwordField.setValueAttribute(configuration.password)

    loginForm.submit()

    def sendPage = webClient.getPage('https://www.orange.ch/vrtstatic/en/residential_myaccount.htm')
    assert sendPage
    sendPage.body

    page = sendPage.getFrames().get(0).getEnclosedPage()
    def anchor = page.getAnchorByHref("https://www.orange.ch/vrtmyaccount/emailandmore")
    assert anchor
    def page2 = anchor.click()
    def div = page2.getElementById("SMS")
    HtmlAnchor link = div.getChildElements().iterator().next().getChildElements().iterator().next()
    def page3 = link.click()
    sendPage = page3.getFrames().get(0).getEnclosedPage()

    def sendForms = sendPage.getForms()
    def sendForm = sendForms.get(0)
    def numberField = sendForm.getInputByName('destinationNumberInput')
    def textField = sendForm.getTextAreaByName('messageInput')
    def wuit = sendForm.getInputByName('wui_target_id')
    def wuie = sendForm.getInputByName('wui_event_id')

    numberField.setValueAttribute(number)
    textField.setText(text)
    wuit.setValueAttribute('sendButton')
    wuie.setValueAttribute('onclick')
    sendForm.submit()
  }
}