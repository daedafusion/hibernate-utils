package com.daedafusion.hibernate.listener;

import com.daedafusion.hibernate.HibernateSessionFactory;
import org.apache.log4j.Logger;
import org.hibernate.StaleObjectStateException;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by mphilpot on 7/3/14.
 */
public class HibernateSessionFilter implements Filter
{
    private static final Logger log = Logger.getLogger(HibernateSessionFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        HibernateSessionFactory.getInstance();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        // Start session
        HibernateSessionFactory.getInstance().getSession();

        try
        {
            chain.doFilter(request, response);
        }
        catch(StaleObjectStateException e)
        {
            HibernateSessionFactory.getInstance().getSession().getTransaction().rollback();

            throw e;
        }
        catch(Exception t)
        {
            try
            {
                if(HibernateSessionFactory.getInstance().getSession().getTransaction().isActive())
                {
                    HibernateSessionFactory.getInstance().getSession().getTransaction().rollback();
                }
            }
            catch(Exception e)
            {
                log.error("Could not rollback transaction after exception", e);
            }

            throw t;
        }
        finally
        {
            HibernateSessionFactory.getInstance().getSession().close();
        }


    }

    @Override
    public void destroy()
    {
        HibernateSessionFactory.getInstance().shutdown();
    }
}
