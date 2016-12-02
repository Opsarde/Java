package project1;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(3500);
            //Start to communicate
            Socket s = ss.accept();
            //inputstream
            BufferedReader receivedString = new BufferedReader(new InputStreamReader(s.getInputStream()));
            //outputstream
            PrintWriter sendString = new PrintWriter(s.getOutputStream(), true);
            String buffer = receivedString.readLine();
            System.out.println("file received.");         
            //sending message in the stream
            sendString.println(buffer);
            System.out.println("file sent.");
            receivedString.close();
            sendString.close();
            s.close();
            ss.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }

}
