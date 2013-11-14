package com.minesweeper;

/**
 * Created by ReneAlexander on 13/11/13.
 */
public class LevelData {
    private int rows;
    private int column;
    private int minesNumber;

    public LevelData(int rows, int column, int minesNumber) {
        this.rows = rows;
        this.column = column;
        this.minesNumber = minesNumber;
    }

    public int getRows() {
        return rows;
    }

    public int getColumn() {
        return column;
    }

    public int getMinesNumber() {
        return minesNumber;
    }
}
