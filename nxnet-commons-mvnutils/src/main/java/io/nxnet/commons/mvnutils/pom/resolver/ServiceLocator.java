package io.nxnet.commons.mvnutils.pom.resolver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public abstract class ServiceLocator
{
    private static ServiceLocator _instance;

    public static ServiceLocator getInstance()
    {
        if (_instance == null)
        {
            _instance = findLocator();
        }
        
        return _instance;
    }
    
    public abstract <S> S getService(Class<S> clazz);
    
    private static synchronized ServiceLocator findLocator()
    {
        ServiceLoader<ServiceLocator> loader = ServiceLoader.load(ServiceLocator.class);
        Iterator<ServiceLocator> locatorIter = loader.iterator();
        List<ServiceLocator> locators = new ArrayList<ServiceLocator>();
        while (locatorIter.hasNext())
        {
            locators.add(locatorIter.next());
        }
        
        if (locators.isEmpty())
        {
            throw new IllegalStateException("No service locators found!");
        }
        else if (locators.size() == 1)
        {
            return locators.get(0);
        }
        else
        {
            ServiceLocator locator = null;
            String locatorClassName = System.getProperty("nxnet.commons.mvnutils.service.locator");
            for (ServiceLocator candidate : locators)
            {
                if (candidate.getClass().getName().equals(locatorClassName))
                {
                    locator = candidate;
                    break;
                }
            }
            
            if (locator == null)
            {
                throw new IllegalStateException("Umbiguous locator resolution, multiple locator instances fould!");
            }
            
            return locator;
        }
    }
}
