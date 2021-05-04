package Server.Threading;

import Server.Authentication.AuthenticationServer;
import com.google.gson.*;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ServerClientThread extends Thread{
    Socket serverClient;
    int clientNo;
    String clientMessage = "", serverMessage = "";
    BufferedReader inStream;
    PrintWriter outStream;
    DataOutputStream dataOutStream;
    DataInputStream dataInputStream;
    private AuthenticationServer authenticate;
    private List<String> tokenList;

    private String APIKEY="I0oVvzXX0GMCuQtbXcEaRGFfyd7Ouv2ukmbbszU3";
    private String APOD_BASE = "https://api.nasa.gov/planetary/apod?api_key=";
    private String INSIGHT_BASE ="https://api.nasa.gov/insight_weather/?api_key=";

    public ServerClientThread (Socket inSocket, int counter) {
        serverClient = inSocket;
        clientNo = counter;
    }
    private boolean tokenValid(String token){
        for(String t:tokenList) {
            if(token.compareTo(t) == 0){
                return true;
            }
        }
        return false;
    }

    public void run() {
        try {
            tokenList = new ArrayList<String>();
            inStream = new BufferedReader(new InputStreamReader(serverClient.getInputStream()));
            outStream = new PrintWriter(serverClient.getOutputStream(), true);
            authenticate = new AuthenticationServer(serverClient, inStream, outStream);
            dataInputStream = new DataInputStream(serverClient.getInputStream());
            dataOutStream = new DataOutputStream(serverClient.getOutputStream());

            boolean success = authenticate.execute();

            if(success){
                // random token generate
                String token = UUID.randomUUID().toString();
                tokenList.add(token);
                outStream.println(token);
                // we send it to client
                // client sends token when requests.

                // check if token is valid.
                String clientRequest = inStream.readLine();
                HttpClient httpClient = HttpClient.newHttpClient();
                String request_body = clientRequest.split("&")[0];
                String clientToken = clientRequest.split("&")[1].split("=")[1];
                System.out.println("Incoming request: "+ clientRequest);
                System.out.println("Token: " + clientToken);
                if(tokenValid(clientToken)){
                    outStream.println("valid");
                    if(request_body.compareTo("apodRequest") == 0){
                        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(APOD_BASE.concat(APIKEY))).build();
                        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                        // String json cevirme
                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        JsonElement json = gson.fromJson(response.body(), JsonElement.class);
                        //outStream.println(json);
                        JsonObject jo = json.getAsJsonObject();
                        String image_url = jo.get("url").getAsString();
                        URL url = new URL(image_url);
                        InputStream is = url.openStream();
                        int bytes = 0;
                        byte[] b = new byte[4096];
                        while( (bytes = is.read(b)) != -1){
                            System.out.println("Image is being sent via byte array..."+bytes);
                            dataOutStream.write(b,0, b.length);
                            dataOutStream.flush();
                        }
                        outStream.println("Finished");

                        System.out.println(json);
                    }
                    else if(request_body.compareTo("insightRequest")==0){
                        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(INSIGHT_BASE.concat(APIKEY).concat("&feedtype=json&ver=1.0"))).build();
                        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                        Gson gson = new GsonBuilder().setPrettyPrinting().create();
                        JsonElement json = gson.fromJson(response.body(), JsonElement.class);
                        JsonObject jo = json.getAsJsonObject();

                        JsonArray sol_keys = jo.get("sol_keys").getAsJsonArray();
                        JsonObject insightResponse = new JsonObject();
                        int sol_index = new Random().nextInt(5);
                        String key = sol_keys.get(sol_index).getAsString();
                        JsonObject sol = jo.get(key).getAsJsonObject();
                        String atm_pre = sol.get("PRE").getAsJsonObject().get("av").getAsString();

                        JsonObject sol_properties = new JsonObject();
                        sol_properties.addProperty("Prev", atm_pre);

                        JsonObject sol_return = new JsonObject();
                        sol_return.add(key, sol_properties);
                        outStream.println(sol_return);
                        System.out.println(sol_return);

                  /*
                  for(int i = 0; i<sol_keys.size(); i++){
                      if(i == 0 || i == 3 ||  i == sol_keys.size()-1){
                          String key = sol_keys.get(i).getAsString();
                          JsonObject sol = jo.get(key).getAsJsonObject();

                          String wind_speed = sol.get("HWS").getAsJsonObject().get("av").getAsString();
                          String atm_temp = sol.get("AT").getAsJsonObject().get("av").getAsString();


                          JsonObject sol_properties = new JsonObject();
                          sol_properties.addProperty("HWS", wind_speed);
                          sol_properties.addProperty("AT", atm_temp);


                          insightResponse.add(key, sol_properties);

                      }
                  }
                  System.out.println(insightResponse);
                  outStream.println(insightResponse);
                  */

                    }
                }
                else{
                    System.out.println("Token is not valid");
                    outStream.println("invalid");
                }

            }
        } catch (Exception ex) {
            System.out.println("Streams cannot be created");
            ex.printStackTrace();
        } finally {
            System.out.println("Client no:" + clientNo + " has been disconnected! ");
        }
    }
}
