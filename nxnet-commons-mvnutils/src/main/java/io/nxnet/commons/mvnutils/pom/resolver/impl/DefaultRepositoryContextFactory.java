package io.nxnet.commons.mvnutils.pom.resolver.impl;

import org.eclipse.aether.examples.util.Booter;
import org.eclipse.aether.internal.impl.DefaultRemoteRepositoryManager;

import io.nxnet.commons.mvnutils.pom.resolver.RepositoryContext;
import io.nxnet.commons.mvnutils.pom.resolver.RepositoryContextFactory;

public class DefaultRepositoryContextFactory implements RepositoryContextFactory
{
    public RepositoryContext getRepositoryContext()
    {
        DefaultRepositoryContext repositoryContext = new DefaultRepositoryContext();
        repositoryContext.setLocalRepository(null);
        repositoryContext.setRemoteRepositories(Booter.newRepositories());
        repositoryContext.setRemoteRepositoryManager(new DefaultRemoteRepositoryManager());
        repositoryContext.setRepositorySystem(Booter.newRepositorySystem());
        repositoryContext.setRepositorySystemSession(
                Booter.newRepositorySystemSession(repositoryContext.getRepositorySystem()));
        return repositoryContext;
    }
}
