package io.nxnet.commons.mvnutils.pom.resolver.impl;

import org.eclipse.aether.repository.RemoteRepository;

import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinition;
import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinitionFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RemoteRepositoryFactory;

public class EtkcRemoteRepositoryFactory implements RemoteRepositoryFactory
{
    private ProxyDefinitionFactory proxyDefinitionFactory;
    
    public EtkcRemoteRepositoryFactory()
    {
        this.proxyDefinitionFactory = new LocalhostProxyDefinitionFactory();
    }
    
    public RemoteRepository getRemoteRepository()
    {
        // Set repo location
        RemoteRepository.Builder repositoryBuilder = new RemoteRepository.Builder(
                "etkc", "default", "http://ehrzgux504.etk.extern.eu.ericsson.se/content/groups/parent/");
        
        // Set repo proxy
        ProxyDefinition proxyDefinition = this.proxyDefinitionFactory.getProxyDefinition();
        if (proxyDefinition != null)
        {
            repositoryBuilder.setProxy(proxyDefinition.getProxy());
        }
        
        return repositoryBuilder.build();
    }

}
