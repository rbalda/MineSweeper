package com.minesweeper;

/**
 * Contains Level Data information
 *
 * @author Jimmy Banchón
 * @author Rene Balda
 *
 */
public class LevelData {
    /**
     * Rows of the level
     */
    private int rows;
    /**
     * Columns of the level
     */
    private int column;
    /**
     * Total number of mines of the level
     */
    private int minesNumber;

    /**
     * Constructor
     * @param rows
     * @param column
     * @param minesNumber
     */
    public LevelData(int rows, int column, int minesNumber) {
        this.rows = rows;
        this.column = column;
        this.minesNumber = minesNumber;
    }

    /**
     * Get the rows of this level
     * @return
     */
    public int getRows() {
        return rows;
    }

    /**
     * Get the columns of this level
     * @return
     */
    public int getColumn() {
        return column;
    }

    /**
     * Get the number of mines of this level
     * @return
     */
    public int getMinesNumber() {
        return minesNumber;
    }
}
