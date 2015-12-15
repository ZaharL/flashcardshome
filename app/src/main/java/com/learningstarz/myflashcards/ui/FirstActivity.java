package com.learningstarz.myflashcards.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.learningstarz.myflashcards.R;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initTextViews();
    }

    private void initTextViews() {
        TextView tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvSignUp.setText(Html.fromHtml(getString(R.string.sign_up_now)));
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        TextView tvQuestion = (TextView) findViewById(R.id.tvAccountHaveQuestion);
        tvQuestion.setText(Html.fromHtml(getString(R.string.no_account)));

        TextView tvContactUs = (TextView) findViewById(R.id.tvContactUs);
        tvContactUs.setText(Html.fromHtml(getString(R.string.contact_us)));

    }

}
