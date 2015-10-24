package com.daedafusion.hibernate;

import com.daedafusion.hibernate.impl.TransactionImpl;

/**
 * Created by mphilpot on 7/3/14.
 */
public class TransactionFactory
{
    private static TransactionFactory ourInstance = new TransactionFactory();

    public static TransactionFactory getInstance()
    {
        return ourInstance;
    }

    private TransactionFactory()
    {
    }

    public Transaction get()
    {
        return new TransactionImpl();
    }
}
