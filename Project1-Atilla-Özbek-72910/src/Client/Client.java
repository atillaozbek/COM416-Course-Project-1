package Client;

import Client.Authenticaton.AuthenticationClient;
import Client.Request.Request;

import java.io.*;
import java.net.Socket;
public class Client {

    private static final int PORT = 9975;
    protected static String token;

    public static void main(String[] args) throws IOException {
        Socket client = new Socket("localhost", PORT);
        BufferedReader clientIn = new BufferedReader(new InputStreamReader(client.getInputStream()));
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter clientOut = new PrintWriter(client.getOutputStream(), true);
        DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());

        AuthenticationClient authenticationClient = new AuthenticationClient(client, clientIn, clientOut, keyboard, dataInputStream,dataOutputStream);
        // Token is returned if valid
        token = authenticationClient.authenticate();
        if(token.compareTo("invalid") != 0){
            Request req = new Request(client, clientIn, clientOut, keyboard, dataInputStream,dataOutputStream);
            req.get();
        }else{
            System.out.println("Authantication failed...");
            System.out.println("Connection is being closed...");
        }

    }
}
