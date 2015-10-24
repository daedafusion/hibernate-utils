package com.daedafusion.hibernate.impl;

import com.daedafusion.hibernate.HibernateSessionFactory;
import com.daedafusion.hibernate.Transaction;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

/**
 * Created by mphilpot on 7/3/14.
 */
public class TransactionImpl implements Transaction
{
    private static final Logger log = Logger.getLogger(TransactionImpl.class);

    @Override
    public void begin()
    {
        HibernateSessionFactory.getInstance().getSession().beginTransaction();
    }

    @Override
    public void commit()
    {
        HibernateSessionFactory.getInstance().getSession().getTransaction().commit();
    }

    @Override
    public void rollback()
    {
        HibernateSessionFactory.getInstance().getSession().getTransaction().rollback();
    }

    @Override
    public void flush()
    {
        HibernateSessionFactory.getInstance().getSession().flush();
    }

    @Override
    public void clear()
    {
        HibernateSessionFactory.getInstance().getSession().clear();
    }

    @Override
    public <T> T materialize(T entity)
    {
        if(entity == null)
            return null;

        Hibernate.initialize(entity);

        if(entity instanceof HibernateProxy)
        {
            entity = (T) ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
        }

        return entity;
    }

    @Override
    public boolean isActive()
    {
        return HibernateSessionFactory.getInstance().getSession().getTransaction().isActive();
    }
}
