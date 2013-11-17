package com.minesweeper;

import android.graphics.Point;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by ReneAlexander on 06/11/13.
 */
public class Block {
    private Point allocation;
    private int value;
    private LinkedList<Block> adjacent;
    private int row;
    private int column;

    public Block(Point point) {
        allocation = point;
        adjacent = new LinkedList<Block>();
    }

    public Block(int row, int column) {
        this.row = row;
        this.column = column;
        adjacent = new LinkedList<Block>();
        this.value = adjacent.size();
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setValue() {
        if (this.value != -1)
            this.value = adjacent.size();
    }

    public int getValue() {
        return value;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void addAdjacent(Block block) {
        adjacent.add(block);
        this.value = adjacent.size();
    }
}
