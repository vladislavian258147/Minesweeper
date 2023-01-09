package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Field extends AppCompatActivity {
    int fieldX;
    int fieldY;
    int fieldMines;
    @SuppressLint("ResourceType")
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

        ImageView[][] tle = new ImageView[fieldY][fieldX];
        TableRow[] tableRow = new TableRow[fieldY];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(50, 50);
        TableLayout tLayout = (TableLayout) findViewById(R.id.tableLayout);
        for (int Y = 0; Y < fieldY; Y++) {
            tableRow[Y] = new TableRow(this);

            for (int X = 0; X < fieldX; X++) {
                tle[Y][X] = new ImageView(this);

                tle[Y][X].setLayoutParams(params);
                tle[Y][X].setMaxHeight(50);
                tle[Y][X].setMaxWidth(50);
                tle[Y][X].setImageResource(R.drawable.filled);
                tableRow[Y].addView(tle[Y][X]);
            }
            tLayout.addView(tableRow[Y], new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }



    }

}