package client;

import java.util.Date;
import java.util.Optional;
import util.Request;
import util.Request.MessageType;
import util.Response;

public class test {

  public static void main(String[] args) {
    Client client = new Client();
    client.start();
    
    Request request = new Request.Builder()
        .withMessageType(MessageType.GET)
        .withHeader("test")
        .withId(1)
        .withTime(new Date())
        .build();
    
    Optional<Response> response = client.newRequest(request);
    
    System.out.println("responsethatishere: " + response);

  }

}
