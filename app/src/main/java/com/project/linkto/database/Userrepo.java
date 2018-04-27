package com.project.linkto.database;

/**
 * Created by bbouzaiene on 27/04/2018.
 */

import android.content.Context;

import com.project.linkto.bean.Userbd;

import java.sql.SQLException;
import java.util.List;



/**
 * Created by bbouzaiene on 14/03/2017.
 */

public class Userrepo implements Crud {

    private DatabaseHelper helper;

    public Userrepo(Context context) {
        DatabaseManager.init(context);
        helper = DatabaseManager.getInstance().getHelper();
    }

    @Override
    public int create(Object item) {
        int index = -1;
        Userbd object = (Userbd) item;
        try {
            index = helper.getUserDao().create(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public int update(Object item) {
        int index = -1;
        Userbd object = (Userbd) item;

        try {
            helper.getUserDao().update(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return index;
    }

    @Override
    public int delete(Object item) {
        int index = -1;
        Userbd object = (Userbd) item;
        try {
            helper.getUserDao().delete(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return index;
    }

    @Override
    public Object findById(int id) {
        Userbd user = null;
        try {
            user = helper.getUserDao().queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<?> findAll() {
        List<Userbd> items = null;
        try {
            items = helper.getUserDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    @Override
    public void clearAll() {
        try {
            helper.getUserDao().delete((List<Userbd>) findAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

