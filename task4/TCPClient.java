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

    public byte[] askServer(String hostname, int port, byte[] toServerBytes) throws IOException {
        if (toServerBytes == null) {
            return askServer(hostname, port);
        } 
        else {
            ByteArrayOutputStream out = new ByteArrayOutputStream(); // output from the server that we want to get (recieve)
            Socket socket1 = new Socket(hostname, port); //creating a socket and connect
            socket1.getOutputStream().write(toServerBytes);

            if (shutdown) {
                socket1.shutdownOutput();
            }
            if (timeout != null) {
                socket1.setSoTimeout(timeout);
            }
            try {
                InputStream in = socket1.getInputStream();
                int t = in.read();
                int max = 0;
                while (t != -1) {
                    if (limit != null && max >= limit) {
                        break;
                    }
                    max++;
                    out.write(t);
                    t = in.read();
                }
                socket1.close();
                return out.toByteArray();
            } catch (SocketTimeoutException ste) {
                socket1.close();
                return out.toByteArray();
            }
        }
    }


    public byte[] askServer(String hostname, int port) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(); // output from the server that we want to get (recieve)
        Socket socket1 = new Socket(hostname, port); //creating a socket and connect
        InputStream input = socket1.getInputStream();
        int t = input.read();
        while(t != -1) {
            out.write(t);
            t = input.read();
        }
        socket1.close();
        return out.toByteArray();
    }
}
