import java.net.*;
import java.io.*;

public class ConcHTTPAsk {

    public static void main(String[] args) throws IOException{

        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);

        try {
            while(true) {
                Socket connSocket = serverSocket.accept();
                MyRunnable connect = new MyRunnable(connSocket);
                new Thread(connect).start();
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }    
}
