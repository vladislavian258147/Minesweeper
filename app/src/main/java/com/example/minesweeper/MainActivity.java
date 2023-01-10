package com.example.minesweeper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_main);
    }

    public void beginGame(View view) {
        Button btn = (Button) view.findViewById(R.id.startButton);




        ImageView effect = (ImageView) findViewById(R.id.effect);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim);
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        anim.setFillAfter(true);
        Intent i = new Intent(this, Field.class);
        i.putExtra("fieldX", 16);
        i.putExtra("fieldY", 16);
        i.putExtra("mines", 40);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                startActivity(i);
                effect.startAnimation(fade);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        effect.startAnimation(anim);
        btn.startAnimation(fade);

    }

}