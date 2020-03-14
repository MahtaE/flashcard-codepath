package com.example.flashcard_codepath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        findViewById(R.id.cancelImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.saveImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                flashcardDatabase = new FlashcardDatabase(getApplicationContext());
                // Get data from edit texts
                String newQuestion = ((EditText)findViewById(R.id.editTextQuestion)).getText().toString();
                String newAnswer = ((EditText)findViewById(R.id.editTextAnswer)).getText().toString();
                Intent data = new Intent();
                data.putExtra("question", newQuestion);
                data.putExtra("answer", newAnswer);
                setResult(RESULT_OK, data);
                finish();
            }
        });

    }
    FlashcardDatabase flashcardDatabase;
}
