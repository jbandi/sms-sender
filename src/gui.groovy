import groovy.swing.SwingBuilder
import java.awt.BorderLayout as BL
import javax.swing.BorderFactory as BF
import automation.Reporter
import automation.Scraper
import automation.Sender
import config.Configuration


def config = new Configuration()
config.init("configuration.txt")
def reporter = new Reporter()
def scraper = new Scraper(config)
def sender = new Sender(config)


swing = new SwingBuilder()
frame = swing.frame(title: 'SMS-automation.Sender') {


    panel(layout: new BL(vgap: 10), border:BF.createEmptyBorder(6,6,6,6)) {
      panel(constraints: BL.NORTH, layout: new BL()) {
        panel(constraints: BL.NORTH, layout: new BL()) {
          hbox(constraints: BL.NORTH) {
            comboBox(items: config.contacts, actionPerformed: {event -> swing.number.text = event.source.selectedItem.number})
            label 'Nummer'
            textField(id: "number", columns: 10)
          }
          label(constraints: BL.WEST, 'Text')
          label(constraints: BL.EAST, id: "len", '0')
        }
        textArea(id: "text", constraints: BL.CENTER, columns: 10, rows: 3, caretUpdate: {event -> swing.len.text = event.source.text.length()})
        panel(constraints: BL.SOUTH, layout: new BL()) {
          button(constraints: BL.WEST, text: 'Get State', actionPerformed: {swing.log.text += scraper.getRemainingUnits()})
          button(constraints: BL.EAST, text: 'Send', actionPerformed: { sender.sendSms(swing.number.text, swing.text.text) })
        }
      }
      panel(constraints: BL.CENTER, layout: new BL()) {
        label(constraints: BL.NORTH, 'Log')
        textArea(constraints: BL.CENTER, id: "log", columns: 10, rows: 3)
      }
    }
  
}
frame.pack()
frame.show()

