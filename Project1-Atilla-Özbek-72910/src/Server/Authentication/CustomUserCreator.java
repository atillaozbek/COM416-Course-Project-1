package Server.Authentication;

import java.util.ArrayList;

public class CustomUserCreator {

    ArrayList<User> userList = new ArrayList<User>();

    public CustomUserCreator() {

    }

    public void initialize() {

        String username1 = "aozbek19";
        String password1 = "1565ad";
        ArrayList<String> questions1 = new ArrayList<>();
        ArrayList<String> answers1 = new ArrayList<>();
        String question1_1 = "What is your favorite meal?";
        String answer1_1 = "Manti";
        String question1_2 = "In which city you were born?";
        String answer1_2 = "Istanbul";
        String question1_3 = "What is your favorite color?";
        String answer1_3 = "Green";
        String question1_4 = "Which team do you support?";
        String answer1_4 = "Besiktas";
        questions1.add(question1_1);
        questions1.add(question1_2);
        questions1.add(question1_3);
        questions1.add(question1_4);
        answers1.add(answer1_1);
        answers1.add(answer1_2);
        answers1.add(answer1_3);
        answers1.add(answer1_4);

        User user1 = new User(username1, password1, questions1, answers1);

        String username2 = "goktugbasaran";
        String password2 = "220897";
        ArrayList<String> questions2 = new ArrayList<>();
        ArrayList<String> answers2 = new ArrayList<>();
        String question2_1 = "What is your favorite meal?";
        String answer2_1 = "Hamburger";
        String question2_2 = "In which city you were born?";
        String answer2_2 = "Izmir";
        String question2_3 = "What is your favorite color?";
        String answer2_3 = "Blue";
        String question2_4 = "Which team do you support?";
        String answer2_4 = "Fenerbahce";
        questions2.add(question2_1);
        questions2.add(question2_2);
        questions2.add(question2_3);
        questions2.add(question2_4);
        answers2.add(answer2_1);
        answers2.add(answer2_2);
        answers2.add(answer2_3);
        answers2.add(answer2_4);

        User user2 = new User(username2, password2, questions2, answers2);

        String username3 = "2";
        String password3 = "2";
        ArrayList<String> questions3 = new ArrayList<>();
        ArrayList<String> answers3 = new ArrayList<>();
        String question3_1 = "What is your favorite meal?";
        String answer3_1 = "2";
        String question3_2 = "In which city you were born?";
        String answer3_2 = "2";
        String question3_3 = "What is your favorite color?";
        String answer3_3 = "2";
        String question3_4 = "Which team do you support?";
        String answer3_4 = "2";
        questions3.add(question3_1);
        questions3.add(question3_2);
        questions3.add(question3_3);
        questions3.add(question3_4);
        answers3.add(answer3_1);
        answers3.add(answer3_2);
        answers3.add(answer3_3);
        answers3.add(answer3_4);

        User user3 = new User(username3, password3, questions3, answers3);

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

}

