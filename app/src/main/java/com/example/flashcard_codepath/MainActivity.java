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
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_CARD_REQUEST_CODE = 0;
    List<Flashcard> allFlashcards;
    int currentCardDisplayedIndex = 0;
    private Flashcard cardToEdit;
    private FlashcardDatabase flashcardDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(this);
        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.questionText)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.flashcard_answer)).setText(allFlashcards.get(0).getAnswer());
            ((Button) findViewById(R.id.ans1)).setText(allFlashcards.get(0).getWrongAnswer1());
            ((Button) findViewById(R.id.ans2)).setText(allFlashcards.get(0).getWrongAnswer2());
            ((Button) findViewById(R.id.ans3)).setText(allFlashcards.get(0).getAnswer());

        }
        findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allFlashcards.size() <= 1) {
                    Toast.makeText(MainActivity.this, "Cannot delete last remaining flashcard!", Toast.LENGTH_LONG).show();

                } else {
                    flashcardDatabase.deleteCard(((TextView) findViewById(R.id.questionText)).getText().toString());

                    allFlashcards = flashcardDatabase.getAllCards();
                    showNextCard();
                }

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

        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextCard();
            }
        });

    }

    public void showNextCard() {
        // advance our pointer index so we can show the next card
        currentCardDisplayedIndex++;

        // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
        if (currentCardDisplayedIndex > allFlashcards.size() - 1) {
            currentCardDisplayedIndex = 0;
        }

        // set the question and answer TextViews with data from the database
        ((TextView) findViewById(R.id.questionText)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
        ((Button) findViewById(R.id.ans1)).setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer1());
        ((Button) findViewById(R.id.ans2)).setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer2());
        ((Button) findViewById(R.id.ans3)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());

        resetState();
    }

    // returns a random number between minNumber and maxNumber, inclusive.
    // for example, if i called getRandomNumber(1, 3), there's an equal chance of it returning either 1, 2, or 3.
    public int getRandomNumber ( int minNumber, int maxNumber){
        Random rand = new Random();
        return rand.nextInt((maxNumber - minNumber) + 1) + minNumber;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_CARD_REQUEST_CODE && resultCode == RESULT_OK) {
            String newQuestion = data.getStringExtra("question");
            String newAnswer = data.getStringExtra("answer");
            String newOptionOne = data.getStringExtra("optionOne");
            String newOptionTwo = data.getStringExtra("optionTwo");
            ((TextView) findViewById(R.id.questionText)).setText(newQuestion);
            ((Button) findViewById(R.id.ans1)).setText(newOptionOne);
            ((Button) findViewById(R.id.ans2)).setText(newOptionTwo);
            ((Button) findViewById(R.id.ans3)).setText(newAnswer);
            flashcardDatabase.insertCard(new Flashcard(newQuestion, newAnswer, newOptionOne, newOptionTwo));
            allFlashcards = flashcardDatabase.getAllCards();
            resetState();
        }

    }



    private void resetState() {
        ((Button)findViewById(R.id.ans1)).setTextColor(Color.BLACK);
        ((Button)findViewById(R.id.ans2)).setTextColor(Color.BLACK);
        ((Button)findViewById(R.id.ans3)).setTextColor(Color.BLACK);
    }
}
