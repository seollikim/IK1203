package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {
    Integer timeout;
    Integer limit;
    boolean shutdown;
    
    public TCPClient(boolean shutdown, Integer timeout, Integer limit) {
        this.timeout = timeout;
        this.limit = limit;
        this.shutdown = shutdown;
    }

    public byte[] askServer(String hostname, int port, byte [] toServerBytes) throws IOException {
        Socket socket1 = new Socket(hostname,port); //creating a socket and connect
        InputStream input = socket1.getInputStream();
        OutputStream output = socket1.getOutputStream();

        ByteArrayOutputStream out = new ByteArrayOutputStream(); // output from the server that we want to get (recieve)
        byte [] array = new byte [2000]; //store data inside here, when reading from server
       

        output.write(toServerBytes); //sending bytes 
        try{

        

            if (shutdown == true) {
                socket1.shutdownOutput();
            }

            if (timeout != null ) {
                socket1.setSoTimeout(timeout);
            }

        }
        catch (SocketException se) {

        }
        catch (SocketTimeoutException ste) {
            socket1.close();
            return out.toByteArray();
        }
        
        try{ 
            int length = input.read(array);
            while (length != -1) {
                out.write(array, 0, length); // start of data: 0
                length = input.read(array);

                if (limit != null && limit >= length) {
                    socket1.setReceiveBufferSize(limit);
                    }
        
                }

            }
        catch (SocketTimeoutException ste) {
            socket1.close();
            return out.toByteArray();
        }
        

        input.close();
        output.close();
        socket1.close();
        return out.toByteArray();
    }


   /*  public byte[] askServer(String hostname, int port) throws IOException {
        Socket socket1 = new Socket(hostname,port); //creating a socket and connect
        InputStream input = socket1.getInputStream();
        OutputStream output = socket1.getOutputStream(); 

        ByteArrayOutputStream out = new ByteArrayOutputStream(); // output from the server that we want to get (recieve)
        byte [] array = new byte [200]; //store data inside here, when reading from server
        
        try{
            int length = input.read(array);
            while (length != -1) {
                out.write(array, 0, length); // start of data: 0
                length = input.read(array);
        }
        
    }
    catch (SocketTimeoutException ste) {
    
    }
        input.close();
        output.close();
        socket1.close();
        return out.toByteArray();
    }*/
}
