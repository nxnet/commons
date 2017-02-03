package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.aether.repository.RemoteRepository;

import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinition;
import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinitionFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RemoteRepositoryFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceRegistry;

public class LocalhostRemoteRepositoryFactory implements RemoteRepositoryFactory
{
    private ProxyDefinitionFactory proxyDefinitionFactory;
    
    public LocalhostRemoteRepositoryFactory()
    {
        this.proxyDefinitionFactory = new LocalhostProxyDefinitionFactory();
    }
    
    public void init(ServiceRegistry serviceLocator)
    {
        this.proxyDefinitionFactory = serviceLocator.getService(ProxyDefinitionFactory.class);
    }
    
    public RemoteRepository getRemoteRepository()
    {
        // Set repo location
        RemoteRepository.Builder repositoryBuilder = new RemoteRepository.Builder(
                "local", "default", getLocalRepoLocation().toExternalForm());
        
        // Set repo proxy
        ProxyDefinition proxyDefinition = this.proxyDefinitionFactory.getProxyDefinition();
        if (proxyDefinition != null)
        {
            repositoryBuilder.setProxy(proxyDefinition.getProxy());
        }
        
        return repositoryBuilder.build();
    }
    
    private URL getLocalRepoLocation()
    {
        String localRepoPath = System.getProperty("user.home") + "/.m2/repository/";
        try
        {
            return new URL("file://" + localRepoPath);
        }
        catch (MalformedURLException e)
        {
            throw new IllegalStateException(e);
        }
    }

}
