package io.nxnet.commons.mvnutils.pom.resolver;

import java.util.Map;

import org.eclipse.aether.repository.Proxy;

public interface RepositoryContextFactory
{
    RepositoryContext getRepositoryContext();
    
    RepositoryContext getRepositoryContext(Map<Proxy, String> proxies);
}
