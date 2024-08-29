import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    try{
        System.out.println("logs will be shown below:");
        ServerSocket serverSocket = new ServerSocket(4221); // this creates a socket on the server, and then the server is listening on  port 4122
        serverSocket.setReuseAddress(true);

        Socket clientSocket = serverSocket.accept(); // now the server will wait for the client socket to request a connection, when the client request the server
        // form a connection and returns the socket, remember this is a blocking operation.
        System.out.println("accepted connection");

        InputStream inputStream = clientSocket.getInputStream(); // it is the stream of data that client sent us as input to the server
        OutputStream outputStream = clientSocket.getOutputStream();  // it is the stream of data that client will receive as output from the server

        String request = getRequestFromClient(inputStream);

        if(request != null) {
//            ExtractUrlPath(request, outputStream);
            ResponseWithBody(request, outputStream);
        }

        // where \r\n is CRLF i.e. carriage return(\r) line feed(\n)
        // where \r means the cursor moves to the starting of the current line
        // where \n means that the cursor moves to the next line without moving to the start of the next line
        // when we do \r\n then the cursor first moves to the start of the current line then moves to the next, so as result moves to the start of the next line
    }
    catch(IOException e){
        System.err.println("exception occurred in server socket: " +  e.getMessage());
    }
  }

  private static String getRequestFromClient(InputStream inputStream){
      try{
          InputStreamReader inputStreamReader = new InputStreamReader(inputStream); // this is used to convert bytes of stream into charactesr
          BufferedReader bufferedReader = new BufferedReader(inputStreamReader); // this is used to convert bytes of stream into characters but using buffers, it means
          // that instead of performing reading operation for each character, instead we save those characters into a internal buffer, then read a group of character, due
          // to this we have to do much less reading operation and thus improving performance.

          String request = bufferedReader.readLine(); // used to read the entire line from the input stream at once.
          return request;
      }
      catch (IOException e){
          System.err.println("Exception occurred while getting request from client: " + e.getMessage());
          return null;
      }
  }

  private static void ExtractUrlPath(String request, OutputStream outputStream) {
      try{
          String urlPath = request.split(" ")[1];
          if(urlPath.equals("/")){
              System.out.println("HTTP/1.1 200 OK\r\n\r\n");
              outputStream.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
          }
          else{
              System.out.println("HTTP/1.1 404 Not Found\r\n\r\n");
              outputStream.write(("HTTP/1.1 404 Not Found\r\n\r\n").getBytes());
          }
      }
      catch (IOException e){
          System.err.println("exception occurred in extracting url path from the request: " + e.getMessage());
      }
  }

  private static void ResponseWithBody(String request, OutputStream outputStream){
      try{
          String requestTarget = request.split(" ")[1];
          String[] requestTargetChunks = requestTarget.split("/");
          String requestTargetUrlLast = requestTargetChunks[requestTargetChunks.length - 1];
          outputStream.write(("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: "
                  + requestTargetUrlLast.length() + "\r\n\r\n" + requestTargetUrlLast).getBytes());
      }
      catch (IOException e){
          System.err.println("Exception occurred in ResponseWithBody: " + e.getMessage());
      }
  }

}
