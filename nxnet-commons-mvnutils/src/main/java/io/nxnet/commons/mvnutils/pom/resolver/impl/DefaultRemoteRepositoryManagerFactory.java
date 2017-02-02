package io.nxnet.commons.mvnutils.pom.resolver.impl;

import org.eclipse.aether.impl.RemoteRepositoryManager;
import org.eclipse.aether.internal.impl.DefaultRemoteRepositoryManager;

import io.nxnet.commons.mvnutils.pom.resolver.RemoteRepositoryManagerFactory;

public class DefaultRemoteRepositoryManagerFactory implements RemoteRepositoryManagerFactory
{
    private RemoteRepositoryManager instance;
    
    public RemoteRepositoryManager getRemoteRepositoryManager()
    {
        if (this.instance == null)
        {
            this.instance = initRemoteRepositoryManager();
        }
        
        return this.instance;
    }

    private RemoteRepositoryManager initRemoteRepositoryManager()
    {
        return new DefaultRemoteRepositoryManager();
    }
}
