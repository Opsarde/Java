package project1;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        try {
//            FileWriter outputFile = new FileWriter(args[2], true); 
            PrintWriter outputFile = new PrintWriter(new FileWriter(args[2]));
            Socket s = new Socket(args[0], 3500);
            //Socket established 
            //Begin reading file data
            BufferedReader file = new BufferedReader(new FileReader(args[1]));
            
            //reading file process
            String message = "";
            String temp = "";
            while ((temp = file.readLine()) != null) {
                message += temp;
            }
            //now message is in string, ready to send.
            PrintWriter sendString = new PrintWriter(s.getOutputStream(), true);
            BufferedReader receivedString = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String b = "";
            long initTime = System.currentTimeMillis();
            sendString.println(message);
            b = receivedString.readLine();
            long finalTime = System.currentTimeMillis();
            //Print system message
            System.out.println("Message successfully sent.");
            System.out.println("Time: " + (finalTime - initTime));
            outputFile.println(b);
            //Closing/flushing streams
            sendString.close();
            receivedString.close();
            s.close();
            file.close();
            outputFile.close();
        } catch(Exception e) {
            System.out.println(e);
        }
    }
}
