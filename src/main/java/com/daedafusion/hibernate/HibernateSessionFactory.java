package com.daedafusion.hibernate;

import javassist.util.proxy.ProxyFactory;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.util.*;
import org.hibernate.service.ServiceRegistry;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mphilpot on 7/3/14.
 */
public class HibernateSessionFactory
{
    private static final Logger                  log         = Logger.getLogger(HibernateSessionFactory.class);

    private static       HibernateSessionFactory ourInstance = new HibernateSessionFactory();

    public static HibernateSessionFactory getInstance()
    {
        return ourInstance;
    }

    private SessionFactory sessionFactory;

    private HibernateSessionFactory()
    {
        try
        {
            InputStream resource = com.daedafusion.configuration.Configuration.getInstance().getResource("hibernate.cfg.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(resource);

            // In scoped classloaders, HibernateSessionFactory's classloader should be scoped.
            // We need to override this so Hibernate doesn't first try the current thread's default classloader first
            ClassLoaderHelper.overridenClassLoader = HibernateSessionFactory.class.getClassLoader();

            ProxyFactory.classLoaderProvider = new ProxyFactory.ClassLoaderProvider() {
                @Override
                public ClassLoader get(ProxyFactory pf)
                {
                    return HibernateSessionFactory.class.getClassLoader();
                }
            };

            Configuration config = new Configuration();
            config.configure(doc);

            // Add resources
            String resourceKeys = com.daedafusion.configuration.Configuration.getInstance().getString("hibernateMappingResources", null);

            if(resourceKeys != null)
            {
                String[] keys = resourceKeys.split(",");

                for(String k : keys)
                {
                    InputStream r = com.daedafusion.configuration.Configuration.getInstance().getResource(k);
                    DocumentBuilder b = factory.newDocumentBuilder();
                    Document d = b.parse(r);

                    config.addDocument(d);
                }
            }

            // Override from distributed config for now, eventually look to Service Discovery
            config.setProperty("hibernate.connection.url",
                    com.daedafusion.configuration.Configuration.getInstance().getString("databaseJdbc", "localhost"));
            config.setProperty("hibernate.connection.username",
                    com.daedafusion.configuration.Configuration.getInstance().getString("databaseUsername", "root"));
            config.setProperty("hibernate.connection.password",
                    com.daedafusion.configuration.Configuration.getInstance().getString("databasePassword", ""));

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();

            sessionFactory = config.buildSessionFactory(serviceRegistry);
        }
        catch (ParserConfigurationException | SAXException | IOException e)
        {
            log.error("Error initializing Hibernate configuration", e);
        }
    }

    public Session getSession()
    {
        return sessionFactory.getCurrentSession();
    }

    public void shutdown()
    {
        sessionFactory.close();
    }


}
