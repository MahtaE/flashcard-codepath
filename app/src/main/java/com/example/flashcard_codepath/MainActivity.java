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

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            String newQuestion = data.getExtras().getString("question");
            String newAnswer = data.getExtras().getString("answer");
            ((TextView)findViewById(R.id.questionText)).setText(newQuestion);
            ((TextView)findViewById(R.id.ans3)).setText(newAnswer);
        }
    }

    private void resetState() {
        ((Button)findViewById(R.id.ans1)).setTextColor(Color.BLACK);
        ((Button)findViewById(R.id.ans2)).setTextColor(Color.BLACK);
        ((Button)findViewById(R.id.ans3)).setTextColor(Color.BLACK);
    }
}
