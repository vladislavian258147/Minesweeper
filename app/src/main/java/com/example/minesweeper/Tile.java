package com.example.minesweeper;

public class Tile {
    boolean Minen;
    int X, Y;
    int Contents;
    public Tile(boolean minen, int x, int y, int contents) {
        Minen = minen;
        X = x;
        Y = y;
        Contents = contents;
    }
}
