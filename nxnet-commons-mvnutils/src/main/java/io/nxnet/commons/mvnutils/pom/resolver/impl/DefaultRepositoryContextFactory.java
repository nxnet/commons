package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.aether.internal.impl.DefaultRemoteRepositoryManager;
import org.eclipse.aether.repository.Proxy;

import io.nxnet.commons.mvnutils.pom.resolver.RepositoryContext;
import io.nxnet.commons.mvnutils.pom.resolver.RepositoryContextFactory;

public class DefaultRepositoryContextFactory implements RepositoryContextFactory
{
    public RepositoryContext getRepositoryContext()
    {
        return getRepositoryContext(new HashMap<Proxy, String>());
    }

    public RepositoryContext getRepositoryContext(Map<Proxy, String> proxies)
    {
        DefaultRepositoryContext repositoryContext = new DefaultRepositoryContext();
        repositoryContext.setLocalRepository(null);
        repositoryContext.setRemoteRepositories(Booter.newRepositories(pickOneProxy(proxies)));
        repositoryContext.setRemoteRepositoryManager(new DefaultRemoteRepositoryManager());
        repositoryContext.setRepositorySystem(Booter.newRepositorySystem());
        repositoryContext.setRepositorySystemSession(
                Booter.newRepositorySystemSession(repositoryContext.getRepositorySystem(), proxies));
        return repositoryContext;
    }
    
    private Proxy pickOneProxy(Map<Proxy, String> proxies)
    {
        Set<Proxy> _proxies = proxies.keySet();
        if (_proxies == null || _proxies.isEmpty())
        {
            return null;
        }
        return _proxies.iterator().next();
    }
}
