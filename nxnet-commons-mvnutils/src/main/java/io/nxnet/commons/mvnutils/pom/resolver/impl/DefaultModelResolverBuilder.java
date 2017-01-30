package io.nxnet.commons.mvnutils.pom.resolver.impl;

import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.project.ProjectBuildingRequest.RepositoryMerging;
import org.apache.maven.project.ProjectModelResolver;
import org.eclipse.aether.RequestTrace;

import io.nxnet.commons.mvnutils.pom.resolver.ModelResolverBuilder;
import io.nxnet.commons.mvnutils.pom.resolver.RepositoryContext;

public final class DefaultModelResolverBuilder implements ModelResolverBuilder
{
    private RepositoryContext repositoryContext;

    private RepositoryMerging repositoryMerging;
    
    private RequestTrace requestTrace;
    
    public ModelResolver build()
    {
        ProjectModelResolver modelResolver = new ProjectModelResolver(
                this.repositoryContext.getRepositorySystemSession(), 
                this.requestTrace, 
                this.repositoryContext.getRepositorySystem(), 
                this.repositoryContext.getRemoteRepositoryManager(), 
                this.repositoryContext.getRemoteRepositories(), 
                this.repositoryMerging, 
                null);
        return modelResolver;
    }

    public ModelResolverBuilder setRepositoryContext(RepositoryContext repositoryContext)
    {
        this.repositoryContext = repositoryContext;
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
