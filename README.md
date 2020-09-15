# CP341Project
### how the project it set up
The classes are divided into four packages: client, server, ui, and util.

Client: This package holds the information used by the chat client to communicate with the server. This and server are the only packages with that use sockets.

Server: This package is the program that acts as the server for the project. This package is responsible for storing the information like chat logs.

UI: This package holds the information for everything about the chat client except its communication with the server. LocalStorage.java uses code in the Client package in order to send the messages to the server.

Util: This packages holds the code for representing the different objects that are used for the chat application, these are things like Messages and Users.

### What the project can do
The client can send messages to other users and when a server is killed the client switches to use the other server and no information is lost.
### How to run
For a full runthrough of the application.
1. Run Server.java with the argument 8888
2. Run Server.java with the argument 8000
3. Run RunClientMainMethod and login as Alice
4. Run RunClientMainMethod and login as Bob
5. Select one of the two message threads and send a new message to the other user.
6. kill the server running on 8888
7. Nothing changes because the messages you just sent between the two users exist on the new server
### Notes:
1. Currently there are only two users (Alice, Bob), trying to log in as someone else will not work.
2. We focused on the distributed systems aspect of the project and as such did not have time to polish some of the chat client. Because of this, in order to create a new message thread, the client must make the thread and then clients must exit and reopen the program (don't need to stop the servers). It is important to note that this purely a glitch on the client side for displaying the new message thread and the once the client is reopened the new thread works to send messages and when one of the servers is killed (like the threads that are already present) the thread (and its messages) are not lost.