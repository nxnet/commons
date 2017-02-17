package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.io.File;
import java.util.List;

import org.apache.maven.model.building.DefaultModelBuilder;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.project.ProjectBuildingRequest.RepositoryMerging;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.RequestTrace;
import org.eclipse.aether.impl.RemoteRepositoryManager;
import org.eclipse.aether.repository.RemoteRepository;

import io.nxnet.commons.mvnutils.pom.resolver.DependencyException;
import io.nxnet.commons.mvnutils.pom.resolver.DependencyResolver;
import io.nxnet.commons.mvnutils.pom.resolver.Model;
import io.nxnet.commons.mvnutils.pom.resolver.ModelException;
import io.nxnet.commons.mvnutils.pom.resolver.ModelFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RemoteRepositoryFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RemoteRepositoryManagerFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RepositorySystemFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RepositorySystemSessionFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceRegistry;

public class DefaultModelFactory implements ModelFactory
{   
    protected DependencyResolver dependencyResolver;
    
    protected RepositorySystemFactory repositorySystemFactory;
    
    protected RepositorySystemSessionFactory repositorySystemSessionFactory;
    
    protected RemoteRepositoryFactory remoteRepositoryFactory;
    
    protected RemoteRepositoryManagerFactory remoteRepositoryManagerFactory;
    
    public DefaultModelFactory()
    {   
        this.dependencyResolver = new DefaultDependencyResolver();
        this.repositorySystemFactory = new DefaultRepositorySystemFactory();
        this.repositorySystemSessionFactory = new DefaultRepositorySystemSessionFactory();
        this.remoteRepositoryFactory = new CentralRemoteRepositoryFactory();
        this.remoteRepositoryManagerFactory = new DefaultRemoteRepositoryManagerFactory();
    }

    public void init(ServiceRegistry serviceLocator)
    {   
        this.dependencyResolver = serviceLocator.getService(DependencyResolver.class);
        this.repositorySystemFactory = serviceLocator.getService(RepositorySystemFactory.class);
        this.repositorySystemSessionFactory = serviceLocator.getService(RepositorySystemSessionFactory.class);
        this.remoteRepositoryFactory = serviceLocator.getService(RemoteRepositoryFactory.class);
        this.remoteRepositoryManagerFactory = serviceLocator.getService(RemoteRepositoryManagerFactory.class);
    }

    public Model getModel(File pom) throws ModelException
    {
        RepositorySystem repositorySystem = this.repositorySystemFactory
                .getRepositorySystem();
        
        RepositorySystemSession repositorySystemSession = this.repositorySystemSessionFactory
                .getRepositorySystemSession();
        
        List<RemoteRepository> remoteRepositories = this.remoteRepositoryFactory
                .getRemoteRepositories();
        
        RemoteRepositoryManager remoteRepositoryManager = this.remoteRepositoryManagerFactory
                .getRemoteRepositoryManager();
        
        // Model resolver (repository aware)
        ModelResolver modelResolver = new DefaultModelResolverBuilder()
                .setRepositorySystem(repositorySystem)
                .setRepositorySystemSession(repositorySystemSession)
                .setRemoteRepositories(remoteRepositories)
                .setRemoteRepositoryManager(remoteRepositoryManager)
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
            model.setDependencyTree(this.dependencyResolver.getDependencyTree(
                    model.getGroupId() + ":"+ model.getArtifactId() + ":"+ model.getVersion()));
        }
        catch (DependencyException e)
        {
            throw new ModelException("Error resolving dependency tree for pom model", e);
        }
        
        return model;
    }

    /**
     * @return the dependencyResolver
     */
    public DependencyResolver getDependencyResolver()
    {
        return dependencyResolver;
    }

    /**
     * @param dependencyResolver the dependencyResolver to set
     */
    public void setDependencyResolver(DependencyResolver dependencyResolver)
    {
        this.dependencyResolver = dependencyResolver;
    }

    /**
     * @return the repositorySystemFactory
     */
    public RepositorySystemFactory getRepositorySystemFactory()
    {
        return repositorySystemFactory;
    }

    /**
     * @param repositorySystemFactory the repositorySystemFactory to set
     */
    public void setRepositorySystemFactory(RepositorySystemFactory repositorySystemFactory)
    {
        this.repositorySystemFactory = repositorySystemFactory;
    }

    /**
     * @return the repositorySystemSessionFactory
     */
    public RepositorySystemSessionFactory getRepositorySystemSessionFactory()
    {
        return repositorySystemSessionFactory;
    }

    /**
     * @param repositorySystemSessionFactory the repositorySystemSessionFactory to set
     */
    public void setRepositorySystemSessionFactory(RepositorySystemSessionFactory repositorySystemSessionFactory)
    {
        this.repositorySystemSessionFactory = repositorySystemSessionFactory;
    }

    /**
     * @return the remoteRepositoryFactory
     */
    public RemoteRepositoryFactory getRemoteRepositoryFactory()
    {
        return remoteRepositoryFactory;
    }

    /**
     * @param remoteRepositoryFactory the remoteRepositoryFactory to set
     */
    public void setRemoteRepositoryFactory(RemoteRepositoryFactory remoteRepositoryFactory)
    {
        this.remoteRepositoryFactory = remoteRepositoryFactory;
    }

    /**
     * @return the remoteRepositoryManagerFactory
     */
    public RemoteRepositoryManagerFactory getRemoteRepositoryManagerFactory()
    {
        return remoteRepositoryManagerFactory;
    }

    /**
     * @param remoteRepositoryManagerFactory the remoteRepositoryManagerFactory to set
     */
    public void setRemoteRepositoryManagerFactory(RemoteRepositoryManagerFactory remoteRepositoryManagerFactory)
    {
        this.remoteRepositoryManagerFactory = remoteRepositoryManagerFactory;
    }

}
