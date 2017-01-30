package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.List;

import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.impl.RemoteRepositoryManager;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;

import io.nxnet.commons.mvnutils.pom.resolver.RepositoryContext;

public class DefaultRepositoryContext implements RepositoryContext
{
    private LocalRepository localRepository;
    
    private List<RemoteRepository> remoteRepositories;
    
    private RemoteRepositoryManager remoteRepositoryManager;
    
    private RepositorySystem repositorySystem;
    
    private RepositorySystemSession repositorySystemSession;

    public LocalRepository getLocalRepository()
    {
        return localRepository;
    }

    public void setLocalRepository(LocalRepository localRepository)
    {
        this.localRepository = localRepository;
    }

    public List<RemoteRepository> getRemoteRepositories()
    {
        return remoteRepositories;
    }

    public void setRemoteRepositories(List<RemoteRepository> remoteRepositories)
    {
        this.remoteRepositories = remoteRepositories;
    }

    public RepositorySystem getRepositorySystem()
    {
        return repositorySystem;
    }

    public void setRepositorySystem(RepositorySystem repositorySystem)
    {
        this.repositorySystem = repositorySystem;
    }

    public RepositorySystemSession getRepositorySystemSession()
    {
        return repositorySystemSession;
    }

    public void setRepositorySystemSession(RepositorySystemSession repositorySystemSession)
    {
        this.repositorySystemSession = repositorySystemSession;
    }

    public RemoteRepositoryManager getRemoteRepositoryManager()
    {
        return remoteRepositoryManager;
    }

    public void setRemoteRepositoryManager(RemoteRepositoryManager remoteRepositoryManager)
    {
        this.remoteRepositoryManager = remoteRepositoryManager;
    }

}
