import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) {
    try{
        ServerSocket serverSocket = new ServerSocket(4221); // this creates a socket on the server, and then the server is listening on  port 4122
        serverSocket.setReuseAddress(true);
        Socket clientSocket = serverSocket.accept(); // now the server will wait for the client socket to request a connection, when the client request the server
        // form a connection and returns the socket, remember this is a blocking operation.
        System.out.println("accepted connection");

        clientSocket.getOutputStream().write("HTTP/1.1 200 OK\r\n\r\n".getBytes());

    }
    catch(IOException e){
        System.err.println("exception occurred in server socket" +  e.getMessage());
    }
  }
}
