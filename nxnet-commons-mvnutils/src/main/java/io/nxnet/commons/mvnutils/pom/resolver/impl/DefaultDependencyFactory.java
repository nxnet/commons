package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.maven.model.Dependency;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.Proxy;
import org.eclipse.aether.util.repository.DefaultProxySelector;

import io.nxnet.commons.mvnutils.pom.resolver.DependencyException;
import io.nxnet.commons.mvnutils.pom.resolver.DependencyFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RepositoryContext;
import io.nxnet.commons.mvnutils.pom.resolver.RepositoryContextFactory;
import io.nxnet.commons.mvnutils.pom.resolver.TreeNode;

public class DefaultDependencyFactory implements DependencyFactory
{
    private RepositoryContextFactory repositoryContextFactory;

    protected Map<Proxy, String> proxies;
    
    public DefaultDependencyFactory()
    {
        this(new DefaultRepositoryContextFactory());
    }

    public DefaultDependencyFactory(RepositoryContextFactory repositoryContextFactory)
    {
        // Repository context factory
        this.repositoryContextFactory = repositoryContextFactory;

        // Proxies
        this.proxies = new HashMap<Proxy, String>();
    }

    public TreeNode<Dependency> getDependencies(String artifact) throws DependencyException
    {
        // Repository context
        RepositoryContext repositoryContext = this.repositoryContextFactory
                .getRepositoryContext(this.proxies);

        // Dependency resolver
        DefaultDependencyResolver dependencyResolver = new DefaultDependencyResolver();
        dependencyResolver.setRepositoryContext(repositoryContext);
        return dependencyResolver.getDependencyTree(artifact);
    }

    /**
     * @return the repositoryContextFactory
     */
    public RepositoryContextFactory getRepositoryContextFactory()
    {
        return repositoryContextFactory;
    }

    /**
     * @param repositoryContextFactory the repositoryContextFactory to set
     */
    public void setRepositoryContextFactory(RepositoryContextFactory repositoryContextFactory)
    {
        this.repositoryContextFactory = repositoryContextFactory;
    }

    public void addProxy(String type, String host, int port, String nonProxyHosts)
    {
        this.proxies.put(new Proxy(type, host, port), nonProxyHosts);
    }
}
