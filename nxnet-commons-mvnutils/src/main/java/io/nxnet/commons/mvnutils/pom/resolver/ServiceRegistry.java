package io.nxnet.commons.mvnutils.pom.resolver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public abstract class ServiceRegistry
{
    private static ServiceRegistry _instance;

    public static ServiceRegistry getInstance()
    {
        if (_instance == null)
        {
            _instance = findLocator();
        }
        
        return _instance;
    }
    
    public abstract <S> S getService(Class<S> clazz);
    
    public abstract <S> void registerService(Class<S> clazz, Initializable service);
    
    private static synchronized ServiceRegistry findLocator()
    {
        ServiceLoader<ServiceRegistry> loader = ServiceLoader.load(ServiceRegistry.class);
        Iterator<ServiceRegistry> locatorIter = loader.iterator();
        List<ServiceRegistry> locators = new ArrayList<ServiceRegistry>();
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
            ServiceRegistry locator = null;
            String locatorClassName = System.getProperty("nxnet.commons.mvnutils.service.locator");
            for (ServiceRegistry candidate : locators)
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
