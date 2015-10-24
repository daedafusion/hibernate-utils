package com.daedafusion.hibernate.daos;

import com.daedafusion.hibernate.daos.impl.HibernateDAOFactory;
import org.apache.log4j.Logger;

/**
 * Created by mphilpot on 7/3/14.
 */
public abstract class DAOFactory
{
    private static final Logger log = Logger.getLogger(DAOFactory.class);

    public static final Class HIBERNATE = HibernateDAOFactory.class;

    public static DAOFactory instance()
    {
        // Could look up in configuration, but just return Hibernate for now
        try
        {
            return (DAOFactory) HIBERNATE.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new RuntimeException("Error creating DAOFactory impl");
        }
    }

    public abstract UserDAO getUserDAO();
    public abstract GroupDAO getGroupDAO();
}
