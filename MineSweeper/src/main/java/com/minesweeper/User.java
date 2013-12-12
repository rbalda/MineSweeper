package com.minesweeper;

/**
 * Contains User Attributes for the DB
 *
 * @author Jimmy Banchón
 * @author Rene Balda
 *
 */
public class User implements Comparable<User>{
    /**
     * Id of the user
     */
    private int id;
    /**
     * Finish time of the game
     */
    private int time;
    /**
     * Initials of the User
     */
    private String initials;
    /**
     * Level played for the user
     */
    private String level;

    /**
     * Getter of level
     * @return
     */
    public String getLevel() {
        return level;
    }

    /**
     * Setter of level
     * @param level
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * getter of ID
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Setter of ID
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter of time
     * @return
     */
    public int getTime() {
        return time;
    }

    /**
     * Setter of time
     * @param time
     */
    public void setTime(int time) {
        this.time = time;
    }

    /***
     * Getter of initials
     * @return
     */
    public String getInitials() {
        return initials;
    }

    /**
     * To String function
     * @return
     */
    @Override
    public String toString() {
        return initials +"\t\t\t"+ time;
    }

    /**
     * Setter of initials
     * @param initials
     */
    public void setInitials(String initials) {
        this.initials = initials;
    }

    /**
     * Compare To Function to Sort the users
     * @param another
     * @return
     */
    @Override
    public int compareTo(User another) {
        if(time<another.time)
            return -1;
        else
            return 1;
    }
}
