package com.minesweeper;

import android.graphics.Point;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Contains Block Logic
 *
 * @author Jimmy Banchón
 * @author Rene Balda
 *
 */
public class Block {
    /**
     * Value of the block
     */
    private int value;
    /**
     * Adjacent List that contains only the adjacent blocks with mines
     */
    private LinkedList<Block> adjacent;
    /**
     * Row position on the array
     */
    private int row;
    /**
     * Column position on the array
     */
    private int column;

    /**
     * Constructor
     * @param row
     * @param column
     */
    public Block(int row, int column) {
        this.row = row;
        this.column = column;
        adjacent = new LinkedList<Block>();
        this.value = adjacent.size();
    }

    /**
     * Setter of value
     * @param value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Setter of value (if not param gets the size of adjacent list as value)
     */
    public void setValue() {
        if (this.value != -1)
            this.value = adjacent.size();
    }

    /**
     * Getter of value
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * Getter of row
     * @return
     */
    public int getRow() {
        return row;
    }

    /**
     * Setter of row
     * @param row
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Getter of column
     * @return
     */
    public int getColumn() {
        return column;
    }

    /**
     * Setter of column
     * @param column
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Function addAdjacent to add Blocks received on the list
     * @param block
     */
    public void addAdjacent(Block block) {
        adjacent.add(block);
        this.value = adjacent.size();
    }
}
