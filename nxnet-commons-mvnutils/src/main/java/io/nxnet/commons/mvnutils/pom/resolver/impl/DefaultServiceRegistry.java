package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.HashMap;
import java.util.Map;

import io.nxnet.commons.mvnutils.pom.resolver.DependencyResolver;
import io.nxnet.commons.mvnutils.pom.resolver.Initializable;
import io.nxnet.commons.mvnutils.pom.resolver.LocalRepositoryFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ModelFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinitionFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RemoteRepositoryFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RemoteRepositoryManagerFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RepositorySystemFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RepositorySystemSessionFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceRegistry;

public class DefaultServiceRegistry extends ServiceRegistry
{
    private Map<Class<?>, Initializable> services;
    
    private boolean servicesInitialized = false;
    
    public DefaultServiceRegistry()
    {
        services = new HashMap<Class<?>, Initializable>();
        services.put(DependencyResolver.class, new DefaultDependencyResolver());
        services.put(LocalRepositoryFactory.class, new DefaultLocalRepositoryFactory());
        services.put(ModelFactory.class, new DefaultModelFactory());
        services.put(ProxyDefinitionFactory.class, new NullProxyDefinitionFactory());
        services.put(RemoteRepositoryFactory.class, new CentralRemoteRepositoryFactory());
        services.put(RemoteRepositoryManagerFactory.class, new DefaultRemoteRepositoryManagerFactory());
        services.put(RepositorySystemFactory.class, new DefaultRepositorySystemFactory());
        services.put(RepositorySystemSessionFactory.class, new DefaultRepositorySystemSessionFactory());
    }

    @Override
    public <S> void registerService(Class<S> clazz, Initializable service)
    {
        services.put(clazz, service);
    }
    @Override
    public <S> S getService(Class<S> clazz)
    {
        if (!this.servicesInitialized)
        {
            this.servicesInitialized = true;
            this.initializeServices();
        }
        
        S service = (S)services.get(clazz);
        if (service == null)
        {
            throw new IllegalStateException("Unknown service of type " + clazz);
        }
        
        return service;
    }

    private void initializeServices()
    {
        for (Initializable service : services.values())
        {
            service.init(this);
        }
    }

}
