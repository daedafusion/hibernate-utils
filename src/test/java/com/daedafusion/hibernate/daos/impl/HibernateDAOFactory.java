package com.daedafusion.hibernate.daos.impl;

import com.daedafusion.hibernate.dao.AbstractDAO;
import com.daedafusion.hibernate.daos.DAOFactory;
import com.daedafusion.hibernate.daos.GroupDAO;
import com.daedafusion.hibernate.daos.UserDAO;
import com.daedafusion.hibernate.entities.Group;
import org.apache.log4j.Logger;

/**
 * Created by mphilpot on 7/3/14.
 */
public class HibernateDAOFactory extends DAOFactory
{
    private static final Logger log = Logger.getLogger(HibernateDAOFactory.class);

    @Override
    public UserDAO getUserDAO()
    {
        return (UserDAO) instantiateDAO(UserDAOHibernate.class);
    }

    @Override
    public GroupDAO getGroupDAO()
    {
        return (GroupDAO) instantiateDAO(GroupDAOHibernate.class);
    }

    private AbstractDAO instantiateDAO(Class daoClass)
    {
        try
        {
            return (AbstractDAO)daoClass.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new RuntimeException("Can not instantiate DAO", e);
        }
    }

    public static class GroupDAOHibernate extends AbstractDAO<Group, Long> implements GroupDAO {}
}
