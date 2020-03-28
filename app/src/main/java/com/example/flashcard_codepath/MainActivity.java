package com.example.flashcard_codepath;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.plattysoft.leonids.ParticleSystem;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_CARD_REQUEST_CODE = 0;
    List<Flashcard> allFlashcards;
    int currentCardDisplayedIndex = 0;
    private Flashcard cardToEdit;
    private FlashcardDatabase flashcardDatabase;

    private final CountDownTimer countDownTimer = new CountDownTimer(16000, 1000) {
        public void onTick(long millisUntilFinished) {
            ((TextView) findViewById(R.id.timer)).setText("" + millisUntilFinished / 1000);
        }

        public void onFinish() {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(this);
        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.flashcard_answer)).setText(allFlashcards.get(0).getAnswer());
            ((Button) findViewById(R.id.ans1)).setText(allFlashcards.get(0).getWrongAnswer1());
            ((Button) findViewById(R.id.ans2)).setText(allFlashcards.get(0).getWrongAnswer2());
            ((Button) findViewById(R.id.ans3)).setText(allFlashcards.get(0).getAnswer());

        }
        //add card
        findViewById(R.id.flashcard_question).setOnClickListener(new View.OnClickListener() {
            //show the flashcard answer and hide the flashcard question
            @Override
            public void onClick(View v) {
                findViewById(R.id.flashcard_answer).setVisibility(View.VISIBLE);
                findViewById(R.id.flashcard_question).setVisibility(View.INVISIBLE);
                View answerSideView = findViewById(R.id.flashcard_answer);

                // get the center for the clipping circle
                int cx = answerSideView.getWidth() / 2;
                int cy = answerSideView.getHeight() / 2;

//              get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

//              create the animator for this view (the start radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius);

//              hide the question and show the answer to prepare for playing the animation!
                final View questionSideView = findViewById(R.id.flashcard_question);
                questionSideView.animate()
                        .rotationY(90)
                        .setDuration(200)
                        .withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        questionSideView.setVisibility(View.INVISIBLE);
                                        findViewById(R.id.flashcard_answer).setVisibility(View.VISIBLE);
                                        // second quarter turn
                                        findViewById(R.id.flashcard_answer).setRotationY(-90);
                                        findViewById(R.id.flashcard_answer).animate()
                                                .rotationY(0)
                                                .setDuration(200)
                                                .start();
                                    }
                                }
                        ).start();
                questionSideView.setVisibility(View.INVISIBLE);
                answerSideView.setVisibility(View.VISIBLE);

                anim.setDuration(3000);
                anim.start();
            }
        });

        findViewById(R.id.nextCardButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call for animation resources
                final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);
                //findViewById(R.id.flashcard_question).startAnimation(leftOutAnim);
                // 3 poss answers for question would go here as well.
                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // this method is called when the animation first starts
                        findViewById(R.id.flashcard_answer).setVisibility(View.INVISIBLE);
                        findViewById(R.id.flashcard_question).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // this method is called when the animation is finished playing
                        ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                        ((TextView) findViewById(R.id.flashcard_answer)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
                        findViewById(R.id.flashcard_question).startAnimation(rightInAnim);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });
                findViewById(R.id.flashcard_question).startAnimation(leftOutAnim);

                findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (allFlashcards.size() <= 1) {
                            Toast.makeText(MainActivity.this, "Cannot delete last remaining flashcard!", Toast.LENGTH_LONG).show();

                        } else {
                            flashcardDatabase.deleteCard(((TextView) findViewById(R.id.flashcard_question)).getText().toString());

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
                        new ParticleSystem(MainActivity.this, 100, R.drawable.cofetti, 3000)
                                .setSpeedRange(0.2f, 0.5f)
                                .oneShot(findViewById(R.id.ans3), 100);

                    }
                });
                findViewById(R.id.addImageButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                        startActivityForResult(intent, ADD_CARD_REQUEST_CODE);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                    }
                });

                findViewById(R.id.nextCardButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showNextCard();
                    }
                });

            }
        });
    }

    private void startTimer() {
        countDownTimer.cancel();
        countDownTimer.start();
    }

    public void showNextCard() {
        // advance our pointer index so we can show the next card
        currentCardDisplayedIndex++;

        // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
        if (currentCardDisplayedIndex > allFlashcards.size() - 1) {
            currentCardDisplayedIndex = 0;
        }

        // set the question and answer TextViews with data from the database
        ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
        ((Button) findViewById(R.id.ans1)).setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer1());
        ((Button) findViewById(R.id.ans2)).setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer2());
        ((Button) findViewById(R.id.ans3)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());

        resetState();
    }


    // returns a random number between minNumber and maxNumber, inclusive.
    // for example, if i called getRandomNumber(1, 3), there's an equal chance of it returning either 1, 2, or 3.
    public int getRandomNumber(int minNumber, int maxNumber) {
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
            ((TextView) findViewById(R.id.flashcard_question)).setText(newQuestion);
            ((TextView) findViewById(R.id.flashcard_answer)).setText(newAnswer);
            findViewById(R.id.flashcard_answer).setVisibility(View.INVISIBLE);
            findViewById(R.id.flashcard_question).setVisibility(View.VISIBLE);
            ((Button) findViewById(R.id.ans1)).setText(newOptionOne);
            ((Button) findViewById(R.id.ans2)).setText(newOptionTwo);
            ((Button) findViewById(R.id.ans3)).setText(newAnswer);
            flashcardDatabase.insertCard(new Flashcard(newQuestion, newAnswer, newOptionOne, newOptionTwo));
            allFlashcards = flashcardDatabase.getAllCards();
            resetState();
        }

    }


    private void resetState() {
        ((Button) findViewById(R.id.ans1)).setTextColor(Color.BLACK);
        ((Button) findViewById(R.id.ans2)).setTextColor(Color.BLACK);
        ((Button) findViewById(R.id.ans3)).setTextColor(Color.BLACK);
    }
}