package com.daedafusion.hibernate.daos;

import com.daedafusion.hibernate.dao.GenericDAO;
import com.daedafusion.hibernate.entities.User;

/**
 * Created by mphilpot on 7/3/14.
 */
public interface UserDAO extends GenericDAO<User, Long>
{
    User findByEmail(String email);
}
