package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    
    public TCPClient() {
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        Socket socket1 = new Socket(hostname,port); //creating a socket and connect
        InputStream input = socket1.getInputStream();
        OutputStream output = socket1.getOutputStream();

        output.write(toServerBytes); //sending bytes 

        ByteArrayOutputStream out = new ByteArrayOutputStream(); // output from the server that we want to get (recieve)
        byte [] array = new byte [200]; //store data inside here, when reading from server
       
       
        int length = input.read(array);
        while (length != -1) {
            out.write(array, 0, length); // start of data: 0
            length = input.read(array);
        }

        input.close();
        output.close();
        socket1.close();
        return out.toByteArray();
    }

    public byte[] askServer(String hostname, int port) throws IOException {
        Socket socket1 = new Socket(hostname,port); //creating a socket and connect
        InputStream input = socket1.getInputStream();
        OutputStream output = socket1.getOutputStream(); 

        ByteArrayOutputStream out = new ByteArrayOutputStream(); // output from the server that we want to get (recieve)
        byte [] array = new byte [200]; //store data inside here, when reading from server
        int length = input.read(array);
        while (length != -1) {
            out.write(array, 0, length); // start of data: 0
            length = input.read(array);
        }

        input.close();
        output.close();
        socket1.close();
        return out.toByteArray();
    }
}

