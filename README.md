# spl-net
This was the third assignemnt in the course SPL201 - java server for Book Club

For running, do the following steps:

1) ~/bin/mvn compile
2) mvn exec:java -Dexec.mainClass="bgu.spl.net.impl.stomp.StompServer" -Dexec.args="<port> <serverType>"
3) wait for clients to work with the server!
