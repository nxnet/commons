package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.aether.repository.RemoteRepository;

import io.nxnet.commons.mvnutils.pom.resolver.RemoteRepositoryFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceRegistry;

public class CompositeRemoteRepositoryFactory implements RemoteRepositoryFactory
{
    private RemoteRepositoryFactory[] remoteRepositoryFactories;
    
    public CompositeRemoteRepositoryFactory(RemoteRepositoryFactory...factories)
    {
        this.remoteRepositoryFactories = factories;
    }

    public void init(ServiceRegistry serviceLocator)
    {
        // Do nothing
    }
    
    public List<RemoteRepository> getRemoteRepositories()
    {
        List<RemoteRepository> remoteRepositories = new ArrayList<RemoteRepository>();
        for (RemoteRepositoryFactory remoteRepositoryFactory : this.remoteRepositoryFactories)
        {
            remoteRepositories.addAll(remoteRepositoryFactory.getRemoteRepositories());
        }
        
        return remoteRepositories;
    }

}
