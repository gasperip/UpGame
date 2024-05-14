package com.example.upgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class GameOver extends AppCompatActivity {

    TextView tvPoints;
    TextView tvHighest;
    SharedPreferences sharedPreferences;
    ImageView ivNewHighest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        tvPoints = findViewById(R.id.tvPoints);
        tvHighest = findViewById(R.id.tvHighest);
        ivNewHighest = findViewById(R.id.ivNewHighest);
        int points = getIntent().getExtras().getInt("points");
        tvPoints.setText(""+ points);
        sharedPreferences = getSharedPreferences("my_pref",0);
        int hightest = sharedPreferences.getInt("highest",0);
        if(points > hightest){
            ivNewHighest.setVisibility(View.VISIBLE);
            hightest = points;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("highest", hightest);
            editor.commit();
        }
        tvHighest.setText("" + hightest);
    }

    public void restart(View view){
        Intent intent = new Intent(GameOver.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void stop(View view){
        finish();
    }
}
