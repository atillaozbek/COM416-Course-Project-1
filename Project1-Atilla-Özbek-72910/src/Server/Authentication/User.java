package Server.Authentication;

import java.util.ArrayList;

public class User {
    String username;
    String password;
    String token = "";
    ArrayList<String> authenticationQuestions = new ArrayList<>();
    ArrayList<String> authenticationAnswers = new ArrayList<>();

    public User(String username, String password, ArrayList<String> authenticationQuestions,
                ArrayList<String> authenticationAnswers) {
        this.username = username;
        this.password = password;
        this.authenticationQuestions = authenticationQuestions;
        this.authenticationAnswers = authenticationAnswers;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public ArrayList<String> getAuthenticationQuestions() {
        return authenticationQuestions;
    }

    public ArrayList<String> getAuthenticationAnswers() {
        return authenticationAnswers;
    }
}
