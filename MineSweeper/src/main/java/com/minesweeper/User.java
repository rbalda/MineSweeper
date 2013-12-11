package com.minesweeper;

/**
 * Created by ReneAlexander on 07/12/13.
 */
public class User implements Comparable<User>{
    private int id;
    private int time;
    private String initials;
    private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getInitials() {
        return initials;
    }

    @Override
    public String toString() {
        return initials +"\t\t\t"+ time;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    @Override
    public int compareTo(User another) {
        if(time<another.time)
            return -1;
        else
            return 1;
    }
}
