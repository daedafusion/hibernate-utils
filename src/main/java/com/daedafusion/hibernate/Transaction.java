package com.daedafusion.hibernate;

/**
 * Created by mphilpot on 7/3/14.
 */
public interface Transaction
{
    void begin();
    void commit();
    void rollback();
    void flush();
    void clear();

    <T> T materialize(T entity);

    boolean isActive();
}
