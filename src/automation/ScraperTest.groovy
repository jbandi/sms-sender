package automation

import config.Configuration


public class ScraperTest extends GroovyTestCase{

  void test_scraping_remaining_free_sms_should_return_valid_string(){
    def config = new Configuration()
    config.init("configuration.txt")

    def sender = new Sender(config)
    def resultString = sender.getRemainingFreeSms()

    assertTrue(resultString.startsWith("This month, you can still send"))
    resultString.endsWith("free SMS.")
  }
}