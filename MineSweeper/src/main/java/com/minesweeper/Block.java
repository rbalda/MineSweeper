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
    private LinkedList<Block> adjacents;


    public Block(Point point) {
        allocation = point;
        adjacents = new LinkedList<Block>();
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setValue() {
        this.value = adjacents.size();
    }


}
