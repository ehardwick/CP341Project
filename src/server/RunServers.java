package server;

import java.util.ArrayList;
import java.util.List;

public class RunServers {

  private final static int[] PORT_NUMBERS = {8880, 8000};

  public static void main(String[] args) {
    List<Thread> serverThreads = new ArrayList<>();
    for (int portNumber : PORT_NUMBERS) {
      serverThreads.add(new ServerThread(portNumber));
    }
    serverThreads.forEach(serverThread -> serverThread.start());
//    serverThreads.get(0).stop();
  }
  
//  private class KillThread extends TimerTask{
//    
//  }

  private static class ServerThread extends Thread {
    private int portNumber;

    public ServerThread(int portNumber) {
      this.portNumber = portNumber;
    }

    public void run() {
      Server server = new Server(portNumber);
      server.start();
    }

  }
}
