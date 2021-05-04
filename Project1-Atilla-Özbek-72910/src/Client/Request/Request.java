package Client.Request;

import Client.Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Request extends Client {

    private Socket client;
    private BufferedReader clientIn;
    private BufferedReader keyboard;
    private PrintWriter clientOut;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;


    public Request(Socket client, BufferedReader clientIn, PrintWriter clientOut, BufferedReader keyboard, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.client = client;
        this.clientIn = clientIn;
        this.clientOut = clientOut;
        this.keyboard = keyboard;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }
    public void get() throws IOException {

        while (!client.isClosed()) {
            System.out.println("1) APOD");
            System.out.println("2) Insight");
            String response;

            String requestType = keyboard.readLine();
            if(requestType.compareTo("APOD") == 0 || requestType.compareTo("1") == 0){
                System.out.println("APOD Request is sent...");
                System.out.println("Token:" + token);
                clientOut.println("apodRequest&token="+token);

                String token_response = clientIn.readLine();
                if(token_response.compareTo("invalid")==0){
                    System.out.println("Token is not validated...");
                    client.close();
                    return;
                }
                System.out.println("Token is validated...");
                List<byte[]> image = new ArrayList<byte[]>();
                int total_bytes = 0;
                int counter = 0;
                while(counter < 30){
                    byte[] b = new byte[4096];
                    dataInputStream.read(b);
                    total_bytes += b.length;
                    image.add(b);
                    counter++;
                }
                System.out.println(total_bytes + " bytes are received...");
                System.out.println("Creating image...");
                byte[] total_image = new byte[total_bytes];
                int index = 0;
                for (byte[] b:image) {
                    int i;
                    for(i = 0;i <b.length;i++){
                        total_image[i+index] = b[i];
                    }
                    index += i;
                }
                byte [] data = total_image;
                try (OutputStream out = new BufferedOutputStream(new FileOutputStream("C:\\Users\\User\\OneDrive\\Masaüstü\\output.jpg"))) {
                    out.write(total_image);
                }
                System.out.println("Image created at: " + "C:\\Users\\User\\OneDrive\\Masaüstü\\output.jpg" );
                System.out.println("Connection is being closed...");
            }
            else if(requestType.compareTo("Insight") == 0 || requestType.compareTo("2") == 0){
                System.out.println("Mars Insight Request is sent...");
                System.out.println("Token:" + token);
                clientOut.println("insightRequest&token="+token);

                response = clientIn.readLine();
                if(response.compareTo("invalid")==0){
                    System.out.println("Token is not validated...");
                    client.close();
                    return;
                }
                System.out.println("Token is validated...");
                response = clientIn.readLine();
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonObject json = gson.fromJson(response, JsonObject.class);
                String prettyJsonString = gson.toJson(json);
                System.out.println(prettyJsonString);
                System.out.println("Connection is being closed...");
            }
            client.close();
        }

    }
}
