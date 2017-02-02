package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.HashMap;
import java.util.Map;

import io.nxnet.commons.mvnutils.pom.resolver.DependencyResolver;
import io.nxnet.commons.mvnutils.pom.resolver.LocalRepositoryFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ModelFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinitionFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RemoteRepositoryFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RemoteRepositoryManagerFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RepositorySystemFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RepositorySystemSessionFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceLocator;

public class DefaultServiceLocator extends ServiceLocator
{
    private static final Map<Class<?>, Object> services = initServices();

    @Override
    public <S> S getService(Class<S> clazz)
    {
        S service = (S)services.get(clazz);
        if (service == null)
        {
            throw new IllegalStateException("Unknown service of type " + clazz);
        }
        
        return service;
    }

    private static Map<Class<?>, Object> initServices()
    {
        Map<Class<?>, Object> services = new HashMap<Class<?>, Object>();
        services.put(DependencyResolver.class, new DefaultDependencyResolver());
        services.put(LocalRepositoryFactory.class, new DefaultLocalRepositoryFactory());
        services.put(ModelFactory.class, new DefaultModelFactory());
        services.put(ProxyDefinitionFactory.class, new LocalhostProxyDefinitionFactory());
        services.put(RemoteRepositoryFactory.class, new EtkcRemoteRepositoryFactory());
        services.put(RemoteRepositoryManagerFactory.class, new DefaultRemoteRepositoryManagerFactory());
        services.put(RepositorySystemFactory.class, new DefaultRepositorySystemFactory());
        services.put(RepositorySystemSessionFactory.class, new DefaultRepositorySystemSessionFactory());
        
        return services;
    }
}
