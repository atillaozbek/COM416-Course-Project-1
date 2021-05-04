package Server;

import Server.Threading.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.lang.*;

public class Main {

    private static int PORT = 9975;

    public static void main(String[] args) throws InterruptedException {
        try {
            ServerSocket server = new ServerSocket(PORT);
            int counter = 0;
            System.out.println("The server is ready and waiting for connection...");
            while (true) {
                Socket serverClient = server.accept();
                serverClient.setSoTimeout(200000);
                counter++;
                System.out.println(" >> " + "Client No:" + counter + " started!");
                ServerClientThread sct = new ServerClientThread(serverClient, counter);
                sct.start();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
