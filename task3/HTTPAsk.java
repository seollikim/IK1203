import java.net.*;
import java.io.*;

public class HTTPAsk {
    public static void main(String[] args) throws IOException {
        // Your code here
        int index = 0;
        int initPort = Integer.parseInt(args[index++]);
        ServerSocket socket2 = new ServerSocket(initPort);
    
        byte[] BadRequest = "HTTP/1.1 400 Bad Request\r\n\r\n".getBytes();
        byte[] NotFound = "HTTP/1.1 404 Not Found\r\n\r\n".getBytes();
        StringBuilder okStatus = new StringBuilder("HTTP/1.1 200 OK\r\n\r\n");

        String string = null;
        String hostname = null;
        Integer port = null;
        boolean shutdown = false;
        Integer limit = null;
        int timeout = 0;
        byte [] fromClient = new byte[0];
        boolean state = false;

        Socket connect = null; // connecting socket
        

        while (true) {
            try{
            System.out.println("Server request");
            connect = socket2.accept();
            System.out.println("Connected");

            InputStream in = connect.getInputStream();
            OutputStream output = new DataOutputStream(connect.getOutputStream());
            
            ByteArrayOutputStream request = new ByteArrayOutputStream();
            int reading = in.read();
            while(true) {
                request.write(reading);
               if(request.toString().endsWith("\r\n\r\n")) {
                   break;
               }
               reading = in.read();
            }
            String url = request.toString();
            //System.out.println(url);

            String[] parameter = url.split("[?\\&\\=\\\r\n\\ ]");

            if (parameter[0].equals("GET") && parameter[1].equals("/ask")) {
                for (int i = 0; i < parameter.length; i++) {
                    if (parameter[i].equals("hostname")) {
                        hostname = parameter[++i];
                    }
                    else if (parameter[i].equals("port")) {
                        port = Integer.parseInt(parameter[++i]);
                    }
                    else if (parameter[i].equals("string")) {
                        string = parameter[++i];
                    }
                    else if (parameter[i].equals("shutdown")) {
                        shutdown = Boolean.parseBoolean(parameter[++i]);
                    }
                    else if (parameter[i].equals("limit")) {
                        limit = Integer.parseInt(parameter[++i]);
                    }
                    else if (parameter[i].equals("timeout")) {
                        timeout = Integer.parseInt(parameter[++i]);
                    }
                    else if (parameter[i].equals("HTTP/1.1")) {
                        state = true;
                    }
                }       
            }

            if (string != null) {
                fromClient = string.getBytes();
            }

            if (parameter[1].equals("/ask") && hostname != null && port != null && state) {
                try {
                    TCPClient tcp = new TCPClient(shutdown, timeout, limit);
                    byte[] toClientBytes = tcp.askServer(hostname, port, fromClient);
                    String data = new String(toClientBytes);
                    okStatus.append(data);
                    output.write(okStatus.toString().getBytes());
                    System.out.println("OK");
                }
                catch(Exception e) {
                    output.write(NotFound);
                    System.out.println("NotFound");
                }
            }
            else{
                output.write(BadRequest);
                System.out.println("BadRequest");
            }
                System.out.println("Client done");
            connect.close();
        }
        catch(Exception ex) {
            System.err.println("Exception" + ex);
            System.exit(1);
        }
    }
    }

}