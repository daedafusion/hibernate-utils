package com.daedafusion.hibernate;

import com.daedafusion.hibernate.daos.DAOFactory;
import com.daedafusion.hibernate.daos.UserDAO;
import com.daedafusion.hibernate.entities.Group;
import com.daedafusion.hibernate.entities.User;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by mphilpot on 7/3/14.
 */
public class HibernateTest
{
    private static final Logger log = Logger.getLogger(HibernateTest.class);

    @Test
    public void main()
    {
        Transaction tm = TransactionFactory.getInstance().get();

        tm.begin();

        UserDAO dao = DAOFactory.instance().getUserDAO();

        User u = new User();
        u.setUsername("mphilpot");
        u.setEmail("mark.philpot@daedafusion.com");

        Group g = new Group();
        g.setName("admin");

        u.getGroups().add(g);

        u = dao.makePersistent(u);

        tm.commit();

        assertThat(u.getId(), is(notNullValue()));
        assertThat(g.getId(), is(notNullValue()));
    }

    @Test
    public void query()
    {
        Transaction tm = TransactionFactory.getInstance().get();

        tm.begin();

        UserDAO dao = DAOFactory.instance().getUserDAO();

        Collection<User> users = dao.findAll();

        assertThat(users.size(), is(1));
        assertThat(users.iterator().next().getEmail(), is("mark.philpot@daedafusion.com"));

        tm.commit();
    }
}
