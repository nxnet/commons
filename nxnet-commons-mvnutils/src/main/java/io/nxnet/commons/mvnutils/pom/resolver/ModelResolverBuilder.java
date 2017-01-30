package io.nxnet.commons.mvnutils.pom.resolver;

import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.project.ProjectBuildingRequest.RepositoryMerging;
import org.eclipse.aether.RequestTrace;

public interface ModelResolverBuilder
{
    ModelResolver build();

    ModelResolverBuilder setRepositoryContext(RepositoryContext repositoryContext);
    
    ModelResolverBuilder setRepositoryMerging(RepositoryMerging repositoryMerging);

    ModelResolverBuilder setRequestTrace(RequestTrace requestTrace);

}