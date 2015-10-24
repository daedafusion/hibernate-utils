package com.daedafusion.hibernate.dao;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by mphilpot on 7/3/14.
 */
public interface GenericDAO<T, ID extends Serializable>
{
    T findById(ID id);
    T findById(ID id, boolean lock);

    Collection<T> findAll();

    T makePersistent(T entity);
    void makeTransient(T entity);
}
