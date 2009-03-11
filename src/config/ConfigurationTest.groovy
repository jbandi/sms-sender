import config.Configuration


def config = new Configuration()
config.init("../configuration.txt")
println config.contacts

