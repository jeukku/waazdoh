ls
cp -uvR update/* .
java -Djava.net.preferIPv4Stack=true -Djava.util.logging.config.file=logging.properties -jar waazdoh-app-linux_32-with-dep.jar

