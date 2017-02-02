package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.project.ProjectBuildingRequest.RepositoryMerging;
import org.apache.maven.project.ProjectModelResolver;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.RequestTrace;
import org.eclipse.aether.impl.RemoteRepositoryManager;
import org.eclipse.aether.repository.RemoteRepository;

import io.nxnet.commons.mvnutils.pom.resolver.ModelResolverBuilder;

public final class DefaultModelResolverBuilder implements ModelResolverBuilder
{
    private RepositorySystem repositorySystem;
    
    private RepositorySystemSession repositorySystemSession;
    
    private List<RemoteRepository> remoteRepositories;
    
    private RemoteRepositoryManager remoteRepositoryManager;
    
    private RepositoryMerging repositoryMerging;
    
    private RequestTrace requestTrace;
    
    public ModelResolver build()
    {
        ProjectModelResolver modelResolver = new ProjectModelResolver(
                this.repositorySystemSession, 
                this.requestTrace, 
                this.repositorySystem, 
                this.remoteRepositoryManager, 
                this.remoteRepositories, 
                this.repositoryMerging, 
                null);
        return modelResolver;
    }

    public ModelResolverBuilder setRepositorySystem(RepositorySystem repositorySystem)
    {
        this.repositorySystem = repositorySystem;
        return this;
    }

    public ModelResolverBuilder setRepositorySystemSession(RepositorySystemSession repositorySystemSession)
    {
        this.repositorySystemSession = repositorySystemSession;
        return this;
    }

    public ModelResolverBuilder setRemoteRepositories(List<RemoteRepository> remoteRepositories)
    {
        this.remoteRepositories = remoteRepositories;
        return this;
    }

    public ModelResolverBuilder addRemoteRepository(RemoteRepository remoteRepository)
    {
        if (this.remoteRepositories == null)
        {
            this.remoteRepositories = new ArrayList<RemoteRepository>();
        }
        this.remoteRepositories.add(remoteRepository);
        return this;
    }

    public ModelResolverBuilder setRemoteRepositoryManager(RemoteRepositoryManager remoteRepositoryManager)
    {
        this.remoteRepositoryManager = remoteRepositoryManager;
        return this;
    }

    public ModelResolverBuilder setRepositoryMerging(RepositoryMerging repositoryMerging)
    {
        this.repositoryMerging = repositoryMerging;
        return this;
    }

    public ModelResolverBuilder setRequestTrace(RequestTrace requestTrace)
    {
        this.requestTrace = requestTrace;
        return this;
    }

}
