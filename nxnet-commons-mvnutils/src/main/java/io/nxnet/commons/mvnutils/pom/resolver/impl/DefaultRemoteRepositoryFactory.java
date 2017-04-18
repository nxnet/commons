package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.Arrays;
import java.util.List;

import org.eclipse.aether.repository.RemoteRepository;

import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinition;
import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinitionFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RemoteRepositoryFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceRegistry;

public class DefaultRemoteRepositoryFactory implements RemoteRepositoryFactory
{
    private String repoId;

    private String repoType;

    private String repoUrl;

    private ProxyDefinitionFactory proxyDefinitionFactory;
    
    public DefaultRemoteRepositoryFactory(String repoId, String repoType, String repoUrl)
    {
        this(repoId, repoType, repoUrl, new LocalhostProxyDefinitionFactory());
    }

    public DefaultRemoteRepositoryFactory(String repoId, String repoType, String repoUrl,
            ProxyDefinitionFactory proxyDefinitionFactory)
    {
        this.repoId = repoId;
        this.repoType = repoType;
        this.repoUrl = repoUrl;
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
        RemoteRepository.Builder repositoryBuilder = new RemoteRepository.Builder(this.repoId, this.repoType,
                this.repoUrl);
        
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
