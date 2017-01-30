package hr.ericsson.m2mse.testutils.pom.resolver.impl;

import java.io.File;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuilder;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.project.ProjectBuildingRequest.RepositoryMerging;
import org.eclipse.aether.RequestTrace;

import hr.ericsson.m2mse.testutils.pom.resolver.ModelException;
import hr.ericsson.m2mse.testutils.pom.resolver.ModelFactory;
import hr.ericsson.m2mse.testutils.pom.resolver.RepositoryContext;
import hr.ericsson.m2mse.testutils.pom.resolver.RepositoryContextFactory;

public class DefaultModelFactory implements ModelFactory
{   
    protected RepositoryContextFactory repositoryContextFactory;
    
    public DefaultModelFactory()
    {
        // Repository context factory
        this.repositoryContextFactory = ServiceLoader.getService(RepositoryContextFactory.class);
    }

    public Model getModel(File pom) throws ModelException
    {
        // Repository context
        RepositoryContext repositoryContext = this.repositoryContextFactory.getRepositoryContext();
        
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
            model = modelBuilder.build(modelBuildingRequest).getEffectiveModel();
        }
        catch (ModelBuildingException e)
        {
            throw new ModelException("Error building pom model", e);
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

}
