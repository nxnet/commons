package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.model.building.DefaultModelBuilder;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.project.ProjectBuildingRequest.RepositoryMerging;
import org.eclipse.aether.RequestTrace;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.Proxy;

import io.nxnet.commons.mvnutils.pom.resolver.DependencyException;
import io.nxnet.commons.mvnutils.pom.resolver.DependencyFactory;
import io.nxnet.commons.mvnutils.pom.resolver.Model;
import io.nxnet.commons.mvnutils.pom.resolver.ModelException;
import io.nxnet.commons.mvnutils.pom.resolver.ModelFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RepositoryContext;
import io.nxnet.commons.mvnutils.pom.resolver.RepositoryContextFactory;

public class DefaultModelFactory implements ModelFactory
{   
    protected RepositoryContextFactory repositoryContextFactory;
    
    protected DependencyFactory dependencyFactory;
    
    protected Map<Proxy, String> proxies;
    
    public DefaultModelFactory()
    {
        // Proxies
        this.proxies = new HashMap<Proxy, String>();
        
        // Repository context factory
        this.repositoryContextFactory = new DefaultRepositoryContextFactory();
        
        // Dependency factory
        this.dependencyFactory = new DefaultDependencyFactory();
    }

    public Model getModel(File pom) throws ModelException
    {
        // Repository context
        RepositoryContext repositoryContext = this.repositoryContextFactory
                .getRepositoryContext(proxies);
        
        // Model resolver (repository aware)
        ModelResolver modelResolver = new DefaultModelResolverBuilder()
                .setRepositoryContext(repositoryContext)
                .setRepositoryMerging(RepositoryMerging.REQUEST_DOMINANT)
                .setRequestTrace(new RequestTrace(null))
                .build();
        
        // Model building request
        DefaultModelBuildingRequest modelBuildingRequest = new DefaultModelBuildingRequest();
        modelBuildingRequest.setModelResolver(modelResolver);
        modelBuildingRequest.setSystemProperties(System.getProperties());
        modelBuildingRequest.setPomFile(pom);
        
        // Model builder
        DefaultModelBuilder modelBuilder = new DefaultModelBuilderFactory().newInstance();
        
        // Model instance
        Model model = null;
        try
        {
            org.apache.maven.model.Model mavenModel = modelBuilder
                    .build(modelBuildingRequest).getEffectiveModel();
            model = new Model(mavenModel);
        }
        catch (ModelBuildingException e)
        {
            throw new ModelException("Error building pom model", e);
        }
        
        // Set dependency tree
        try
        {
            model.setDependencyTree(this.dependencyFactory.getDependencies(
                    model.getGroupId() + ":"+ model.getArtifactId() + ":"+ model.getVersion()));
        }
        catch (DependencyException e)
        {
            throw new ModelException("Error resolving dependency tree for pom model", e);
        }
        
        return model;
    }

    public RepositoryContextFactory getRepositoryContextFactory()
    {
        return repositoryContextFactory;
    }

    public void setRepositoryContextFactory(RepositoryContextFactory repositoryContextFactory)
    {
        this.repositoryContextFactory = repositoryContextFactory;
    }

    /**
     * @return the dependencyFactory
     */
    public DependencyFactory getDependencyFactory()
    {
        return dependencyFactory;
    }

    /**
     * @param dependencyFactory the dependencyFactory to set
     */
    public void setDependencyFactory(DependencyFactory dependencyFactory)
    {
        this.dependencyFactory = dependencyFactory;
    }

    public void addProxy(String type, String host, int port, String nonProxyHosts)
    {
        this.proxies.put(new Proxy(type, host, port), nonProxyHosts);
        ((DefaultDependencyFactory)this.dependencyFactory).addProxy(type, host, port, nonProxyHosts);
    }
}
