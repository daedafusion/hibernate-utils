package com.daedafusion.hibernate.daos.impl;

import com.daedafusion.hibernate.dao.AbstractDAO;
import com.daedafusion.hibernate.daos.UserDAO;
import com.daedafusion.hibernate.entities.User;
import org.apache.log4j.Logger;
import org.hibernate.Query;

/**
 * Created by mphilpot on 7/3/14.
 */
public class UserDAOHibernate extends AbstractDAO<User, Long> implements UserDAO
{
    private static final Logger log = Logger.getLogger(UserDAOHibernate.class);

    @Override
    public User findByEmail(String email)
    {
        Query q = getSession().createQuery("from User u where u.email = :email");
        q.setString("email", email);
        return (User) q.uniqueResult();
    }
}
