package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Field extends AppCompatActivity {
    int fieldX;
    int fieldY;
    int fieldMines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fieldX = extras.getInt("fieldX");
            fieldY = extras.getInt("fieldY");
            fieldMines = extras.getInt("mines");
        }
        setContentView(R.layout.field);

        for (int Y = 0; Y < fieldY; Y++) {
            for (int X = 0; X < fieldX; X++) {
                TableLayout layout = (TableLayout) findViewById(R.id.tableLayout);
                
                ImageView tle = new ImageView(this);

            }
        }



    }

}