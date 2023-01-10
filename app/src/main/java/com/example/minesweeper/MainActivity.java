package com.example.minesweeper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
        EditText editTextY = (EditText) findViewById(R.id.editTextY);
        EditText editTextMines = (EditText) findViewById(R.id.editTextMines);

        if (editTextY.getText().toString().matches("")) {
            Toast.makeText(this, "Одно из полей не заполнено", Toast.LENGTH_SHORT).show();
            return;
        }

        if (editTextMines.getText().toString().matches("")) {
            Toast.makeText(this, "Одно из полей не заполнено", Toast.LENGTH_SHORT).show();
            return;
        }

        int fieldY = Integer.parseInt(editTextY.getText().toString());
        int fieldMines = Integer.parseInt(editTextMines.getText().toString());

        if (fieldMines <= 10 || fieldMines >= 500 || fieldY <= 0 || fieldY >= 100 || (fieldMines * 5) >= (fieldY * fieldY * 4)) {
            new AlertDialog.Builder(this)
                    .setTitle("Warning!")
                    .setMessage("Поля заполнены неправильно! Минимальные значения: 8х8, 10 мин; Максимальные значения: 60х60, 250 мин. Мин на поле не может быть больше 3/4 самого поля")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }




        ImageView effect = (ImageView) findViewById(R.id.effect);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim);
        Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        anim.setFillAfter(true);
        Intent i = new Intent(this, Field.class);
        i.putExtra("fieldX", fieldY);
        i.putExtra("fieldY", fieldY);
        i.putExtra("mines", fieldMines);
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