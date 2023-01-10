package com.example.minesweeper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Field extends AppCompatActivity {
    int fieldX;
    int fieldY;
    int fieldMines;
    Boolean loseFlag = false;
    int winCount;
    int minesFlaged;
    Boolean[][] tiles;
    Boolean firstMove = false;
    TextView minesLeast;
    TextView resultOfGameText;
    Vibrator vibro;

    @SuppressLint({"ResourceType", "SetTextI18n"})
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
        winCount = fieldY * fieldX - fieldMines;
        minesFlaged = fieldMines;

        setContentView(R.layout.field);
        resultOfGameText = (TextView) findViewById(R.id.resultOfGame);
        vibro = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        minesLeast = (TextView) findViewById(R.id.minesTextLeast);

        minesLeast.setText("Мины: " + fieldMines);
        ImageView[][] tle = new ImageView[fieldY][fieldX];
        TableRow[] tableRow = new TableRow[fieldY];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(70, 70);
        TableLayout tLayout = (TableLayout) findViewById(R.id.tableLayout);
        for (int Y = 0; Y < fieldY; Y++) {
            tableRow[Y] = new TableRow(this);
            tableRow[Y].setId(Y + (fieldY * fieldX + 1));
            tableRow[Y].setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tableRow[Y].setVisibility(View.VISIBLE);
            for (int X = 0; X < fieldX; X++) {
                tle[Y][X] = new ImageView(this);
                tle[Y][X].setId(X+(Y*fieldX));
                tle[Y][X].requestLayout();
                tle[Y][X].setImageResource(R.drawable.filled);
                tle[Y][X].setVisibility(View.VISIBLE);
                int finalY = Y;
                int finalX = X;
                tle[Y][X].setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        open(tle[finalY][finalX], finalX, finalY);
                    }
                });
                tle[Y][X].setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View view) {
                        flag(tle[finalY][finalX], finalX, finalY);
                        return true;
                    }
                });
                tableRow[Y].addView(tle[Y][X]);
                tle[Y][X].getLayoutParams().width = 75;
                tle[Y][X].getLayoutParams().height = 75;
            }
            tLayout.addView(tableRow[Y]);
            tLayout.setVisibility(View.VISIBLE);
        }

    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    public void open(ImageView pressedButton, int X, int Y) {
        if (pressedButton.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.filled).getConstantState() || loseFlag || winCount == 0) { return;}
        int minesCount = 0;
        if (!firstMove) {
            tiles = new Boolean[fieldY][fieldX];
            for (int i = 0; i < fieldY; i++) {
                for (int j = 0; j < fieldX; j++) {
                    tiles[i][j] = false;
                }
            }

            createLogicField(fieldX, fieldY, fieldMines, Y * fieldY + X);
            firstMove = true;
        }
        if (tiles[Y][X]) {
            pressedButton.setImageResource(R.drawable.mine);
            loseFlag = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                resultOfGameText.setVisibility(View.VISIBLE);
                vibro.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                resultOfGameText.setVisibility(View.VISIBLE);
                vibro.vibrate(500);
            }
            resultOfGameText.setText("You lose!");
            return;
        }

        else {
            if (Y == 0) for (int i = Y; i <= Y + 1; i++) {
                if (X == 0) for (int j = X; j <= X + 1; j++) {
                    if (tiles[i][j]) minesCount++;
                }
                else
                if (X == fieldX - 1) for (int j = X - 1; j <= X; j++) {
                    if (tiles[i][j]) minesCount++;
                }
                else for (int j = X - 1; j <= X + 1; j++) if (tiles[i][j]) minesCount++;
            }
            else
            if (Y == fieldY - 1) for (int i = Y - 1; i <= Y; i++) {
                if (X == 0) for (int j = X; j <= X + 1; j++) {
                    if (tiles[i][j]) minesCount++;
                }
                else
                if (X == fieldX - 1) for (int j = X - 1; j <= X; j++) {
                    if (tiles[i][j]) minesCount++;
                }
                else for (int j = X - 1; j <= X + 1; j++) if (tiles[i][j]) minesCount++;
            }
            else for (int i = Y - 1; i <= Y + 1; i++) {
                if (X == 0) for (int j = X; j <= X + 1; j++) {
                    if (tiles[i][j]) minesCount++;
                }
                else
                if (X == fieldX - 1) for (int j = X - 1; j <= X; j++) {
                    if (tiles[i][j]) minesCount++;
                }
                else for (int j = X - 1; j <= X + 1; j++) if (tiles[i][j]) minesCount++;
            }
        }


        switch (minesCount) {
            case 0: pressedButton.setImageResource(R.drawable.empty); winCount--;
            int currentButton = pressedButton.getId();
                int tempY = (int)(currentButton/fieldY);
                int tempX = currentButton % fieldY;
                if (tempY == 0) for (int i = currentButton; i <= currentButton + fieldY; i+= fieldY) {
                    if (tempX == 0) for (int j = 0; j <= 1; j++) {
                        ImageView nextButton = (ImageView) findViewById(i + j);
                        int currentTempY = (int)((i + j)/fieldY);
                        int currentTempX = (i + j) % fieldY;
                        open(nextButton, currentTempX, currentTempY);
                    }
                    else
                    if (tempX == fieldX - 1) for (int j = -1; j <= 0; j++) {
                        ImageView nextButton = (ImageView) findViewById(i + j);
                        int currentTempY = (int)((i + j)/fieldY);
                        int currentTempX = (i + j) % fieldY;
                        open(nextButton, currentTempX, currentTempY);
                    }
                    else for (int j = -1; j <= 1; j++) {
                        ImageView nextButton = (ImageView) findViewById(i + j);
                        int currentTempY = (int)((i + j)/fieldY);
                        int currentTempX = (i + j) % fieldY;
                        open(nextButton, currentTempX, currentTempY);
                    }
                }
                else
                if (tempY == fieldY - 1) for (int i = currentButton - fieldY; i <= currentButton; i+= fieldY) {
                    if (tempX == 0) for (int j = 0; j <= 1; j++) {
                        ImageView nextButton = (ImageView) findViewById(i + j);
                        int currentTempY = (int)((i + j)/fieldY);
                        int currentTempX = (i + j) % fieldY;
                        open(nextButton, currentTempX, currentTempY);
                    }
                    else
                    if (tempX == fieldX - 1) for (int j = -1; j <= 0; j++) {
                        ImageView nextButton = (ImageView) findViewById(i + j);
                        int currentTempY = (int)((i + j)/fieldY);
                        int currentTempX = (i + j) % fieldY;
                        open(nextButton, currentTempX, currentTempY);
                    }
                    else for (int j = -1; j <= 1; j++) {
                        ImageView nextButton = (ImageView) findViewById(i + j);
                        int currentTempY = (int)((i + j)/fieldY);
                        int currentTempX = (i + j) % fieldY;
                        open(nextButton, currentTempX, currentTempY);
                    }
                }
                else for (int i = currentButton - fieldY; i <= currentButton + fieldY; i+= fieldY) {
                    if (tempX == 0) for (int j = 0; j <= 1; j++) {
                        ImageView nextButton = (ImageView) findViewById(i + j);
                        int currentTempY = (int)((i + j)/fieldY);
                        int currentTempX = (i + j) % fieldY;
                        open(nextButton, currentTempX, currentTempY);
                    }
                    else
                    if (tempX == fieldX - 1) for (int j = -1; j <= 0; j++) {
                        ImageView nextButton = (ImageView) findViewById(i + j);
                        int currentTempY = (int)((i + j)/fieldY);
                        int currentTempX = (i + j) % fieldY;
                        open(nextButton, currentTempX, currentTempY);
                    }
                    else for (int j = -1; j <= 1; j++) {
                        ImageView nextButton = (ImageView) findViewById(i + j);
                        int currentTempY = (int)((i + j)/fieldY);
                        int currentTempX = (i + j) % fieldY;
                        open(nextButton, currentTempX, currentTempY);
                    }
                }

            break;
            case 1: pressedButton.setImageResource(R.drawable._1); winCount--; break;
            case 2: pressedButton.setImageResource(R.drawable._2); winCount--; break;
            case 3: pressedButton.setImageResource(R.drawable._3); winCount--; break;
            case 4: pressedButton.setImageResource(R.drawable._4); winCount--; break;
            case 5: pressedButton.setImageResource(R.drawable._5); winCount--; break;
            case 6: pressedButton.setImageResource(R.drawable._6); winCount--; break;
            case 7: pressedButton.setImageResource(R.drawable._7); winCount--; break;
            case 8: pressedButton.setImageResource(R.drawable._8); winCount--; break;
        }

        if (winCount == 0) {
            resultOfGameText.setText("Yow win!");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                resultOfGameText.setVisibility(View.VISIBLE);
                vibro.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            }
            else {
            vibro.vibrate(500);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    public void flag(ImageView pressedButton, int X, int Y) {
        if (pressedButton.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.filled).getConstantState()) { pressedButton.setImageResource(R.drawable.flag); if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibro.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else {
            vibro.vibrate(100);
        } minesLeast.setText("Мины: " + --minesFlaged); return;}
        if (pressedButton.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.flag).getConstantState()) { pressedButton.setImageResource(R.drawable.filled); if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibro.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else {
            vibro.vibrate(100);
        }minesLeast.setText("Мины: " + ++minesFlaged);}
    }

    public void createLogicField(int fieldX, int fieldY, int fieldMines, int startTile) {
        int[] randoms = new int[fieldMines];
        Random random = new Random(System.nanoTime());
        for (int i = 0; i < fieldMines; i++) {
            randoms[i] = random.nextInt(fieldX*fieldY);
            if (i >= 1) for (int j = 0; j < i; j++) {
                while (randoms[i] == randoms[j] || randoms[i] >= startTile - 1 && randoms[i] <= startTile + 1 || randoms[i] >= startTile - fieldY - 1 && randoms[i] <= startTile - fieldY + 1 || randoms[i] >= startTile + fieldY - 1 && randoms[i] <= startTile + fieldY + 1) {
                    randoms[i] = random.nextInt(fieldX*fieldY);
                }
            }
            int mineY = (int)(randoms[i]/fieldY);
            int mineX = randoms[i] % fieldY;
            tiles[mineY][mineX] = true;

        }
    }


    public void restartGame(View view) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}