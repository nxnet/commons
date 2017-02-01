package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.model.building.DefaultModelBuilder;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.project.ProjectBuildingRequest.RepositoryMerging;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.RequestTrace;
import org.eclipse.aether.repository.Proxy;
import org.eclipse.aether.util.repository.DefaultProxySelector;

import io.nxnet.commons.mvnutils.pom.resolver.Model;
import io.nxnet.commons.mvnutils.pom.resolver.ModelException;
import io.nxnet.commons.mvnutils.pom.resolver.ModelFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RepositoryContext;
import io.nxnet.commons.mvnutils.pom.resolver.RepositoryContextFactory;

public class DefaultModelFactory implements ModelFactory
{   
    protected RepositoryContextFactory repositoryContextFactory;
    
    protected Map<Proxy, String> proxies;
    
    public DefaultModelFactory()
    {
        // Repository context factory
        this.repositoryContextFactory = new DefaultRepositoryContextFactory();
        
        // Proxies
        this.proxies = new HashMap<Proxy, String>();
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
        org.apache.maven.model.Model model = null;
        try
        {
            model = modelBuilder.build(modelBuildingRequest).getEffectiveModel();
        }
        catch (ModelBuildingException e)
        {
            throw new ModelException("Error building pom model", e);
        }
        return new Model(model);
    }

    public RepositoryContextFactory getRepositoryContextFactory()
    {
        return repositoryContextFactory;
    }

    public void setRepositoryContextFactory(RepositoryContextFactory repositoryContextFactory)
    {
        this.repositoryContextFactory = repositoryContextFactory;
    }

    public void addProxy(String type, String host, int port, String nonProxyHosts)
    {
        this.proxies.put(new Proxy(type, host, port), nonProxyHosts);
    }
}
