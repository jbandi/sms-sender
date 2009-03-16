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

  String getRemainingFreeSms(){

    WebClient webClient = createWebClient()

    performLogin(webClient)

    HtmlPage sendPage = navigateToSmsSendPage(webClient)

    HtmlElement remainingSmsText = sendPage.getHtmlElementById("para13Text")

    remainingSmsText.getTextContent()
  }

  void sendSms(String number, String text) {

    WebClient webClient = createWebClient()

    performLogin(webClient)

    HtmlPage sendPage = navigateToSmsSendPage(webClient)

    def sendForms = sendPage.getForms()
    def sendForm = sendForms.get(0)
    def numberField = sendForm.getInputByName('destinationNumberInput')
    def textField = sendForm.getTextAreaByName('messageInput')
    def wuit = sendForm.getInputByName('wui_target_id')
    def wuie = sendForm.getInputByName('wui_event_id')

    HtmlElement remainingSmsText = sendPage.getHtmlElementById("para13Text")

    println remainingSmsText.getTextContent()

    numberField.setValueAttribute(number)
    textField.setText(text)
    wuit.setValueAttribute('sendButton')
    wuie.setValueAttribute('onclick')
//    sendForm.submit()
  }

  private HtmlPage navigateToSmsSendPage(WebClient webClient) {
    def accountPage = webClient.getPage('https://www.orange.ch/vrtstatic/en/residential_myaccount.htm')
    assert accountPage

    def innerAccountPage = accountPage.getFrames().get(0).getEnclosedPage()
    def linkToEmailAndMore = innerAccountPage.getAnchorByHref("https://www.orange.ch/vrtmyaccount/emailandmore")
    assert linkToEmailAndMore
    def expandedAccountPage = linkToEmailAndMore.click()

    def div = expandedAccountPage.getElementById("SMS")
    // navigate from the div to the <li> and further to the <a>
    HtmlAnchor linkToSmsSendPage = div.getChildElements().iterator().next().getChildElements().iterator().next()

    HtmlPage smsSendPage = linkToSmsSendPage.click()
    HtmlPage sendPage = smsSendPage.getFrames().get(0).getEnclosedPage()
    return sendPage
  }

  private def performLogin(WebClient webClient) {
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
  }

  private WebClient createWebClient() {
//def webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_7_0 , "proxy1.pnet.ch", 3128)
    def webClient = new WebClient(BrowserVersion.FIREFOX_2)
    webClient.setThrowExceptionOnScriptError(false)
    return webClient
  }
}