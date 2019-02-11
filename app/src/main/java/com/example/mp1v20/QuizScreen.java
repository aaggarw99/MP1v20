package com.example.mp1v20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Map;
import java.util.*;
import android.widget.Toast;
import java.lang.*;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;



public class QuizScreen extends AppCompatActivity {

    private static final long COUNTDOWN_START = 5000;
    static final int PICK_CONTACT = 1;

    // instance variables
    int score = 0;

    //  UI
    ImageView image;
    TextView scoreTextField;
    TextView timerTextField;
    Button quit;
    Button ans1;
    Button ans2;
    Button ans3;
    Button ans4;

    CountDownTimer countdown;
    boolean timerRunning;
    long timeLeft = COUNTDOWN_START;


    String[] members = new String[]{"Aayush Tyagi", "Abhinav Koppu", "Aditya Yadav", "Ajay Merchia",
            "Alice Zhao", "Amy Shen", "Anand Chandra", "Andres Medrano", "Angela Dong", "Anika Bagga",
            "Anmol Parande", "Austin Davis", "Ayush Kumar", "Brandon David", "Candice Ye", "Carol Wang",
            "Cody Hsieh", "Daniel Andrews", "Daniel Jing", "Eric Kong", "Ethan Wong", "Fang Shuo", "Izzie Lau",
            "Jaiveer Singh", "Japjot Singh", "Jeffery Zhang", "Joey Hejna", "Julie Deng", "Justin Kim",
            "Kaden Dippe", "Kanyes Thaker", "Kayli Jiang", "Kiana Go", "Leon Kwak", "Levi Walsh",
            "Louie Mcconnell", "Max Miranda", "Michelle Mao", "Mohit Katyal", "Mudabbir Khan",
            "Natasha Wong", "Nikhar Arora", "Noah Pepper", "Paul Shao", "Radhika Dhomse", "Sai Yandapalli",
            "Saman Virai", "Sarah Tang", "Sharie Wang", "Shiv Kushwah", "Shomil Jain", "Shreya Reddy",
            "Shubha Jagannatha", "Shubham Gupta", "Srujay Korlakunta", "Stephen Jayakar", "Suyash Gupta",
            "Tiger Chen", "Vaibhav Gattani", "Victor Sun", "Vidya Ravikumar", "Vineeth Yeevani", "Wilbur Shi",
            "William Lu", "Will Oakley", "Xin Yi Chen", "Young Lin"};


    Member[] mems = new Member[members.length];

    // HashMap that links members names (String) to their respective drawable id (Integer)
    Map<String, Integer> members_pictures = new HashMap<>();

    // Holds all questions
    ArrayList<Question> questionsAL;

    // Question that we're inspecting
    Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);

        image = findViewById(R.id.headshot);
        scoreTextField = findViewById(R.id.score);
        quit = findViewById(R.id.quit);

        quit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                onBackPressed();
            }
        });

        ans1 = findViewById(R.id.ans1);
        ans2 = findViewById(R.id.ans2);
        ans3 = findViewById(R.id.ans3);
        ans4 = findViewById(R.id.ans4);
        timerTextField = findViewById(R.id.timer);

        image.bringToFront();


        // creates Member objects for all member names
        for (int i = 0; i < members.length; i++) {
            mems[i] = new Member(members[i]);
        }

        // shuffles members
        mems = shuffledMembers(mems);

        // generate questions for entire program
        // think about generators in java?
        questionsAL = genQuestions();

        // event listeners for all buttons
        ans1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (checkCorrect(ans1.getText().toString())) {
                    score++;
                    scoreTextField.setText(String.valueOf(score));
                    // maybe play sound
                } else {
                    Toast.makeText(QuizScreen.this,
                            "Incorrect! That's " + question.getAnswer() + "!", Toast.LENGTH_LONG).show();
                }
                updateQuestionAndOptions();
            }
        });

        ans2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (checkCorrect(ans2.getText().toString())) {
                    score++;
                    scoreTextField.setText(String.valueOf(score));
                    // maybe play sound
                } else {
                    Toast.makeText(QuizScreen.this,
                            "Incorrect! That's " + question.getAnswer() + "!", Toast.LENGTH_LONG).show();
                }
                updateQuestionAndOptions();

            }
        });

        ans3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (checkCorrect(ans3.getText().toString())) {
                    score++;
                    scoreTextField.setText(String.valueOf(score));
                    // maybe play sound
                } else {
                    Toast.makeText(QuizScreen.this,
                            "Incorrect! That's " + question.getAnswer() + "!", Toast.LENGTH_LONG).show();
                }
                updateQuestionAndOptions();
            }
        });

        ans4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if (checkCorrect(ans4.getText().toString())) {
                    score++;
                    scoreTextField.setText(String.valueOf(score));
                    // maybe play sound
                } else {
                    Toast.makeText(QuizScreen.this,
                            "Incorrect! That's " + question.getAnswer() + "!", Toast.LENGTH_LONG).show();
                }
                updateQuestionAndOptions();
            }
        });

        // click on image handling
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();

                Intent intent= new Intent(ContactsContract.Intents.Insert.ACTION);
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                intent.putExtra(ContactsContract.Intents.Insert.NAME, question.getAnswer());

                startActivity(intent);

            }
        });

        //This method will set the que and four options
        startTimer();
        updateQuestionAndOptions();

    }

    /* TIMER FUNCTIONALITY */

    private void startTimer() {
        countdown = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateCountDownTimer();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                Toast.makeText(QuizScreen.this,
                        "Time's up!", Toast.LENGTH_LONG).show();
                updateQuestionAndOptions();
            }
        }.start();

        timerRunning = true;
    }

    private void pauseTimer() {
        countdown.cancel();
        timerRunning = false;
    }

    private void resetTimer() {
        timeLeft = COUNTDOWN_START;
        updateCountDownTimer();
    }

    private void updateCountDownTimer() {
        int seconds = (int) (timeLeft / 1000) % 60;

        timerTextField.setText(String.valueOf(seconds));
    }

    /* CORE REFRESH FUNCTION */

    public void updateQuestionAndOptions() {

        resetTimer();
        countdown.start();

        // if questions runs out, shuffle members and generate new questions
        if (questionsAL.isEmpty()) {
            mems = shuffledMembers(mems);
            questionsAL = genQuestions();
        }

        // random number instantiation
        Random random = new Random();

        question = questionsAL.get(random.nextInt(questionsAL.size()));
        questionsAL.remove(question);

        // set image to person's face
        image.setImageResource(question.getImg());

        // set ans button texts
        ans1.setText(question.getOp1());
        ans2.setText(question.getOp2());
        ans3.setText(question.getOp3());
        ans4.setText(question.getOp4());

    }


    // handling for quit button and alert dialog
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        countdown.cancel();
                        QuizScreen.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        countdown.start();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /* QUESTION, CHOICES, SHUFFLING FUNCTIONS */

    public Member[] shuffledMembers(Member[] members) {
        List<Member> l = Arrays.asList(members);
        Collections.shuffle(l);
        Member[] shuffled_members = new Member[l.size()];
        shuffled_members = l.toArray(shuffled_members);

        return shuffled_members;
    }

    // generates a list of choices
    public String[] generateChoices(Member name) {
        ArrayList<String> choices = new ArrayList<>();
        Random random = new Random();
        int pIndex = random.nextInt(mems.length);

        while(choices.size() != 3) {
            if (!mems[pIndex].getName().equals(name.getName()) && !choices.contains(mems[pIndex].getName())) {
                choices.add(mems[pIndex].getName());
            }
            pIndex = random.nextInt(members.length);
        }
        choices.add(name.getName());

        Collections.shuffle(choices);
        return choices.toArray(new String[0]);
    }


    // returns arraylist of questions
    public ArrayList<Question> genQuestions() {
        ArrayList<Question> qAL = new ArrayList<>();
        for (int i = 0; i < mems.length; i++) {
            String[] options = generateChoices(mems[i]);
            Question q = new Question(mems[i].getImg(), options, mems[i].getName());
            qAL.add(q);
        }
        return qAL;

    }


    // checks if ans is the correct answer
    public boolean checkCorrect(String ans) {
        if (ans.equals(question.getAnswer())) {
            return true;
        }
        return false;
    }


    // Member Class
    private class Member {
        String name;
        int img;

        private Member(String n) {
            name = n;
            String stripped_name = n.replaceAll("\\s", "").toLowerCase();
            img = getResources().getIdentifier(stripped_name, "drawable", getPackageName());
        }

        private String getName() {
            return name;
        }

        private int getImg() {
            return img;
        }
    }
}
