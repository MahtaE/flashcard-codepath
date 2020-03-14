package com.example.flashcard_codepath;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    List<Flashcard>allFlashcards;
    int currentCardDisplayedindex=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FlashcardDatabase flashcardDatabase = new FlashcardDatabase(this);
        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.questionText)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.flashcard_answer)).setText(allFlashcards.get(0).getAnswer());
        }
        findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashcardDatabase.deleteCard(((TextView) findViewById(R.id.questionText)).getText().toString());
            }
        });

        findViewById(R.id.ans1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetState();
                ((Button) findViewById(R.id.ans1)).setTextColor(getResources().getColor(R.color.wrongAns));

            }
        });

        findViewById(R.id.ans2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetState();
                ((Button) findViewById(R.id.ans2)).setTextColor(getResources().getColor(R.color.wrongAns));
            }
        });
        findViewById(R.id.ans3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetState();
                ((Button) findViewById(R.id.ans3)).setTextColor(getResources().getColor(R.color.correctAns));
            }
        });
        findViewById(R.id.addImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                startActivityForResult(intent, ADD_CARD_REQUEST_CODE);
            }
        });
        // returns a random number between minNumber and maxNumber, inclusive.
        // for example, if i called getRandomNumber(1, 3), there's an equal chance of it returning either 1, 2, or 3.
        //public int getRandomNumber(int minNumber, int maxNumber) {
        //Random rand = new Random();
        //return rand.nextInt((maxNumber - minNumber) + 1) + minNumber;
        //}
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CARD_REQUEST_CODE) {       //100 = ADD_CARD_REQUEST_CODE
            String newQuestion = data.getExtras().getString("question");
            String newAnswer = data.getExtras().getString("answer");
            ((TextView)findViewById(R.id.questionText)).setText(newQuestion);
            ((TextView)findViewById(R.id.ans3)).setText(newAnswer);
            //flashcardDatabase.insertCard(new Flashcard(question, answer));
            //allFlashcards = flashcardDatabase.getAllCards();

            Intent myIntent = new Intent(MainActivity.this, AddCardActivity.class);
            MainActivity.this.startActivityForResult(myIntent, ADD_CARD_REQUEST_CODE);
        }

    }

    private void resetState() {
        ((Button)findViewById(R.id.ans1)).setTextColor(Color.BLACK);
        ((Button)findViewById(R.id.ans2)).setTextColor(Color.BLACK);
        ((Button)findViewById(R.id.ans3)).setTextColor(Color.BLACK);
    }
}
