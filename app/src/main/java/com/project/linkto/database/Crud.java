package com.project.linkto.database;

/**
 * Created by bbouzaiene on 14/03/2017.
 */

import java.util.List;

interface Crud
{
    public int create(Object item);
    public int update(Object item);
    public int delete(Object item);
    public Object findById(int id);
    public void clearAll();
    public List<?> findAll();
}