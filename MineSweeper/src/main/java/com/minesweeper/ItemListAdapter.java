package com.minesweeper;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Jimmy on 10/12/13.
 */
public class ItemListAdapter extends BaseAdapter {

    private Activity activity;
    private List<User> listUser;

    public ItemListAdapter(Activity activity, List<User> listUser){
        this.activity = activity;
        this.listUser = listUser;
    }

    public int getCount() {
        return listUser.size();
    }


    public Object getItem(int i) {
        return listUser.get(i).toString();
    }


    public long getItemId(int i) {
        return i;
    }


    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
