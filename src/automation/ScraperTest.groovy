import automation.Scraper
import config.Configuration
import automation.Sender

def config = new Configuration()
config.init("configuration.txt")

//def scraper = new Scraper(config)
//scraper.getRemainingUnits()

def sender = new Sender(config)
println sender.getRemainingFreeSms()