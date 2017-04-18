package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.Arrays;
import java.util.List;

import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;

import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinition;
import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinitionFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RemoteRepositoryFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceRegistry;

public class CentralRemoteRepositoryFactory implements RemoteRepositoryFactory
{
    private ProxyDefinitionFactory proxyDefinitionFactory;
    
    public CentralRemoteRepositoryFactory()
    {
        this.proxyDefinitionFactory = new LocalhostProxyDefinitionFactory();
    }

    public CentralRemoteRepositoryFactory(ProxyDefinitionFactory proxyDefinitionFactory)
    {
        this.proxyDefinitionFactory = proxyDefinitionFactory;
    }

    public void init(ServiceRegistry serviceLocator)
    {
        this.proxyDefinitionFactory = serviceLocator.getService(ProxyDefinitionFactory.class);
    }

    @Override
    public List<RemoteRepository> getRemoteRepositories()
    {
        // Set repo location
        RemoteRepository.Builder repositoryBuilder = new RemoteRepository.Builder(
                "central", "default", "http://central.maven.org/maven2/");
        repositoryBuilder.setSnapshotPolicy(new RepositoryPolicy(false, RepositoryPolicy.UPDATE_POLICY_NEVER, 
                RepositoryPolicy.CHECKSUM_POLICY_WARN));
        
        // Set repo proxy
        ProxyDefinition proxyDefinition = this.proxyDefinitionFactory.getProxyDefinition();
        if (proxyDefinition != null)
        {
            repositoryBuilder.setProxy(proxyDefinition.getProxy());
        }
        
        return Arrays.asList(repositoryBuilder.build());
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
