package com.minesweeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ReneAlexander on 09/12/13.
 */
public class UserDataSource {
    private SQLiteDatabase database;
    private DataBaseCreator dbCreator;
    private String[] user = { DataBaseCreator.COLUMN_ID,
            DataBaseCreator.COLUMN_USER, DataBaseCreator.COLUMN_TIME, DataBaseCreator.COLUMN_LEVEL};

    public UserDataSource(Context context){
        dbCreator = new DataBaseCreator(context);
    }

    public void open(){
        database = dbCreator.getWritableDatabase();
    }

    public void close(){
        dbCreator.close();
    }

    //public User createUser(String name,int time) {
    public User createUser(String name,int time,String level) {
        ContentValues values = new ContentValues();
        values.put(DataBaseCreator.COLUMN_USER, name);
        values.put(DataBaseCreator.COLUMN_TIME,(long)time);
        values.put(DataBaseCreator.COLUMN_LEVEL,level);
        long insertId = database.insert(DataBaseCreator.TABLE_USER, null,
                values);
        Cursor cursor = database.query(DataBaseCreator.TABLE_USER,
                user, DataBaseCreator.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        User temp = cursorToUser(cursor);
        cursor.close();
        return temp;
    }

    public void deleteUser(User u) {
        long id = u.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(DataBaseCreator.TABLE_USER, DataBaseCreator.COLUMN_ID
                + " = " + id, null);
    }

    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<User>();

        Cursor cursor = database.query(DataBaseCreator.TABLE_USER,
                user, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User u = cursorToUser(cursor);
            usersList.add(u);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return usersList;
    }

    private User cursorToUser(Cursor cursor) {
        User temp = new User();
        temp.setId((int)cursor.getLong(0));
        temp.setInitials(cursor.getString(1));
        temp.setTime((int)cursor.getLong(2));
        temp.setLevel(cursor.getString(3));
        return temp;
    }


}
