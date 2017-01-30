package hr.ericsson.m2mse.testutils.pom.resolver.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServiceLoader
{
    public static <S> S getService(Class<S> clazz) 
    {
        if (clazz == null)
        {
            throw new IllegalArgumentException("clazz parameter can't be null");
        }
        
        java.util.ServiceLoader<S> serviceLoader = java.util.ServiceLoader.load(clazz);
        List<S> services = getServices(serviceLoader);
        int servicesFound = services.size();
        if (servicesFound == 0) // If no services was found
        {
            // throw new IllegalStateException(MessageFormat.format(
            //     "No service implementations found for {0}", clazz.getName()));
            return null; // It's ok to find nothing
        }
        else if (servicesFound == 1) // we have match
        {
            return services.get(0);
        }
        else // ambiguous resolution, more then one service implementations found
        {
            String prefferedServices = System.getProperty("etkc.preffered.services");
            if (prefferedServices != null)
            {
                S prefferedService = null;
                for (S service : services)
                {
                    if (prefferedServices.contains(service.getClass().getName()))
                    {
                        if (prefferedService != null)
                        {
                            throw new IllegalStateException(MessageFormat.format(
                                    "Ambiguous service resolution for {0}. Multiple services found: {1}", clazz.getName(), services.toArray()));
                        }
                        
                        prefferedService = service; 
                    }
                }
                
                return prefferedService;
            }
           
            throw new IllegalStateException(MessageFormat.format(
                    "Ambiguous service resolution for {0}. Multiple services found: {1}", clazz.getName(), services.toArray()));
        }
    }
    
    private static <S> List<S> getServices(java.util.ServiceLoader<S> factoryLoader)
    {
        List<S> factories = new ArrayList<S>();
        Iterator<S> factoryIterator = factoryLoader.iterator();
        while (factoryIterator.hasNext())
        {
            factories.add(factoryIterator.next());
        }
        return factories;
    }
}
