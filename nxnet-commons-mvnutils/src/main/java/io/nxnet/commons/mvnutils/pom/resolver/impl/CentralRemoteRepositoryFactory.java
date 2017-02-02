package io.nxnet.commons.mvnutils.pom.resolver.impl;

import org.eclipse.aether.repository.RemoteRepository;

import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinition;
import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinitionFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RemoteRepositoryFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceLocator;

public class CentralRemoteRepositoryFactory implements RemoteRepositoryFactory
{
    private ProxyDefinitionFactory proxyDefinitionFactory;
    
    public CentralRemoteRepositoryFactory()
    {
        this.proxyDefinitionFactory = ServiceLocator.getInstance().getService(ProxyDefinitionFactory.class);
    }
    
    public RemoteRepository getRemoteRepository()
    {
        // Set repo location
        RemoteRepository.Builder repositoryBuilder = new RemoteRepository.Builder(
                "central", "default", "http://central.maven.org/maven2/");
        
        // Set repo proxy
        ProxyDefinition proxyDefinition = this.proxyDefinitionFactory.getProxyDefinition();
        if (proxyDefinition != null)
        {
            repositoryBuilder.setProxy(proxyDefinition.getProxy());
        }
        
        return repositoryBuilder.build();
    }

    /**
     * @return the proxyDefinitionFactory
     */
    public ProxyDefinitionFactory getProxyDefinitionFactory()
    {
        return proxyDefinitionFactory;
    }

    /**
     * @param proxyDefinitionFactory the proxyDefinitionFactory to set
     */
    public void setProxyDefinitionFactory(ProxyDefinitionFactory proxyDefinitionFactory)
    {
        this.proxyDefinitionFactory = proxyDefinitionFactory;
    }

}
