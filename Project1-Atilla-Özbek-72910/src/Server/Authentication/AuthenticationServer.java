package Server.Authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
public class AuthenticationServer {
    private ArrayList<User> userList = new ArrayList<>();
    private Socket client;
    private BufferedReader serverIn;
    private PrintWriter serverOut;
    private boolean authenticationStatus = false;

    public AuthenticationServer(Socket client, BufferedReader serverIn, PrintWriter serverOut) {
        this.client = client;
        this.serverIn = serverIn;
        this.serverOut = serverOut;
    }

    public boolean execute() throws IOException {
        CustomUserCreator customUserCreator = new CustomUserCreator();
        customUserCreator.initialize();
        userList = customUserCreator.getUserList();

        System.out.println("Checking username & password...");


        // Checks entered username and password whether they are valid or not. If they
        // are valid, return the associated user, else return null.

        User user = checkUsernameAndPassword();

        // Status of authentication. Initially false, will be true and the end of the
        // authentication questions.

        boolean isAuthenticationSuccessful = false;

        // If user exists with entered username and password, continue authentication
        // part, if not close the server socket.

        if (user != null) {
            System.out.println("Checking questions...");
            isAuthenticationSuccessful = authenticateClient(user);
            return isAuthenticationSuccessful;
        }
        return false;
    }

    public User checkUsernameAndPassword()
            throws IOException {

        // Asks for username and password to the connected user and receive username and
        // password from the client

        serverOut.println("Enter your username: ");
        String username = serverIn.readLine();
        serverOut.println("Enter your password: ");
        String password = serverIn.readLine();

        // Search for username in the user list

        for (User user : userList) {
            String currentUsername = user.getUsername();
            // If there is username that is matched with the entered one, checks the
            // password

            if (username.compareTo(currentUsername) == 0) {
                String currentPassword = user.getPassword();
                // If password check is correct, informs the client for successful login

                if (password.compareTo(currentPassword) == 0) {
                    System.out.println("New user successfully login!");
                    serverOut.println("Username and Password are valid");
                    serverOut.println("True");
                    return user;

                    // If password check is not correct, informs the client for invalid username or
                    // password and close the sockets for that client.

                } else {
                    serverOut.println("Invalid username or password. You have been disconnected");
                    serverOut.println("False");
                    serverIn.close();
                    serverOut.close();
                    client.close();
                    System.out.println("The user who enters \"" + username
                            + "\" for username has been disconnected due to entering invalid password.");
                    return null;
                }
            }
        }

        // If there is no such a username, informs the client for invalid username or
        // password and close the sockets for that client.

        serverOut.println("Invalid username or password. You have been disconnected");
        serverOut.println("False");
        serverIn.close();
        serverOut.close();
        client.close();
        System.out.println("The user who enters \"" + username
                + "\" for username has been disconnected due to entering invalid username.");
        return null;
    }

    public boolean authenticateClient(User user)
            throws IOException {
        ArrayList<String> userQuestions = user.getAuthenticationQuestions();
        ArrayList<String> userAnswers = user.getAuthenticationAnswers();

        // Status of authentication. Initially true, will be false if one of the
        // authentication questions are answered wrongly, remain true if not.

        boolean isAnswerCorrect = true;

        // The index of current question in the questions list.

        int index = 0;
        String currentAnswer;
        String currentQuestion;

        // Sends an info to the user about total number of questions

        serverOut.write(userQuestions.size());

        // As long as questions are answered correctly and there is question to be
        // answered, continue

        while (isAnswerCorrect && index < userQuestions.size()) {
            int questionNumber = index + 1;
            System.out.println("The user with username  \"" + user.getUsername()
                    + "\" is answering the authentication question number " + questionNumber + "...");
            currentQuestion = userQuestions.get(index);

            // Sends current question to the client each time
            serverOut.println(currentQuestion);
            if ((currentAnswer = serverIn.readLine()) != null) {

                // If the answer is not null and is correct, informs user about that.
                if (currentAnswer.compareTo(userAnswers.get(index)) == 0) {
                    serverOut.println("Correct answer.");

                    // Else, informs user about wrong answer and close sockets.
                } else {
                    isAnswerCorrect = false;
                    serverOut.println("Invalid answer.");
                    System.out.println("The user with username  \"" + user.getUsername()
                            + "\" has been disconnected due to giving wrong answer for authentication question number "
                            + questionNumber + ".\n");
                    serverIn.close();
                    serverOut.close();
                    client.close();
                }
            }
            index++;
        }
        if (isAnswerCorrect) {
            serverOut.println("Authentication is successful");
            authenticationStatus = true;
            //String token = random.nextInt(1000) + user.getUsername();
        }
        return isAnswerCorrect;
    }


}
