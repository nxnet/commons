package io.nxnet.commons.mvnutils.pom.resolver;

import java.util.List;

import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.project.ProjectBuildingRequest.RepositoryMerging;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.RequestTrace;
import org.eclipse.aether.impl.RemoteRepositoryManager;
import org.eclipse.aether.repository.RemoteRepository;

public interface ModelResolverBuilder
{
    ModelResolver build();

    ModelResolverBuilder setRepositorySystem(RepositorySystem repositorySystem);

    ModelResolverBuilder setRepositorySystemSession(RepositorySystemSession repositorySystemSession);

    ModelResolverBuilder setRemoteRepositories(List<RemoteRepository> remoteRepositories);

    ModelResolverBuilder addRemoteRepository(RemoteRepository remoteRepository);

    ModelResolverBuilder setRemoteRepositoryManager(RemoteRepositoryManager remoteRepositoryManager);

    ModelResolverBuilder setRepositoryMerging(RepositoryMerging repositoryMerging);

    ModelResolverBuilder setRequestTrace(RequestTrace requestTrace);

}