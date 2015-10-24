package com.daedafusion.hibernate.dao;

import com.daedafusion.hibernate.HibernateSessionFactory;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Session;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Created by mphilpot on 7/3/14.
 */
public abstract class AbstractDAO<T, ID extends Serializable> implements GenericDAO<T, ID>
{
    private static final Logger log = Logger.getLogger(AbstractDAO.class);

    private Class<T> persistentClass;
    private Session  session;

    protected AbstractDAO()
    {
        persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void setSession(Session s)
    {
        session = s;
    }

    protected Session getSession()
    {
        if(session == null)
        {
            session = HibernateSessionFactory.getInstance().getSession();
        }

        return session;
    }

    protected Class<T> getPersistentClass()
    {
        return persistentClass;
    }


    @Override
    public T findById(ID id)
    {
        return (T) getSession().load(getPersistentClass(), id);
    }

    @Override
    public T findById(ID id, boolean lock)
    {
        if(lock)
        {
            return (T) getSession().load(getPersistentClass(), id, LockOptions.UPGRADE);
        }
        else
        {
            return findById(id);
        }
    }

    @Override
    public Collection<T> findAll()
    {
        Criteria crit = getSession().createCriteria(getPersistentClass());

        // When FetchType.EAGER is set, the outer join produces multiple instances of the same database object.  We want to
        // dedup while retaining order
        return new LinkedHashSet<T>(crit.list());
    }

    @Override
    public T makePersistent(T entity)
    {
        getSession().saveOrUpdate(entity);
        return entity;
    }

    @Override
    public void makeTransient(T entity)
    {
        getSession().delete(entity);
    }
}
