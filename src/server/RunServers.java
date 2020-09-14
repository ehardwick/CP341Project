package server;

import java.util.ArrayList;
import java.util.List;

public class RunServers {

  private final static int[] PORT_NUMBERS = {8888, 8000};

  public static void main(String[] args) {
    List<Server> servers = new ArrayList<>();
    for(int portNumber : PORT_NUMBERS) {
      servers.add(new Server(portNumber));
    }
    servers.forEach(server -> server.start());
  }

}
