package automation


class Reporter {
  public void report(String ... messages) {
    messages.each { println it};
  }
}