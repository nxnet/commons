package io.nxnet.commons.mvnutils.pom.resolver;

import java.io.File;

import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuilder;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.resolution.ModelResolver;
import org.apache.maven.project.ProjectBuildingRequest.RepositoryMerging;
import org.eclipse.aether.RequestTrace;
import org.junit.Before;
import org.junit.Test;

import io.nxnet.commons.mvnutils.pom.resolver.impl.DefaultModelResolverBuilder;

public class ModelResolverBuilderTest
{
    private ModelResolverBuilder modelResolverBuilder;
    
    private RepositorySystemFactory repositorySystemFactory;
    
    private RepositorySystemSessionFactory repositorySystemSessionFactory;
    
    private RemoteRepositoryFactory remoteRepositoryFactory;
    
    private RemoteRepositoryManagerFactory remoteRepositoryManagerFactory;
    
    @Before
    public void setUp()
    {
        this.modelResolverBuilder = new DefaultModelResolverBuilder();
        this.repositorySystemFactory = ServiceRegistry.getInstance().getService(RepositorySystemFactory.class);
        this.repositorySystemSessionFactory = ServiceRegistry.getInstance().getService(RepositorySystemSessionFactory.class);
        this.remoteRepositoryFactory = ServiceRegistry.getInstance().getService(RemoteRepositoryFactory.class);
        this.remoteRepositoryManagerFactory = ServiceRegistry.getInstance().getService(RemoteRepositoryManagerFactory.class);
    }

    @Test
    public void testA() throws Exception
    {   
        ModelResolver modelResolver = this.modelResolverBuilder
            .setRepositorySystem(this.repositorySystemFactory.getRepositorySystem())
            .setRepositorySystemSession(this.repositorySystemSessionFactory.getRepositorySystemSession())
            .setRemoteRepositories(this.remoteRepositoryFactory.getRemoteRepositories())
            .setRemoteRepositoryManager(this.remoteRepositoryManagerFactory.getRemoteRepositoryManager())
            .setRepositoryMerging(RepositoryMerging.REQUEST_DOMINANT)
            .setRequestTrace(new RequestTrace(null))
            .build();
        
        DefaultModelBuildingRequest modelBuildingRequest = new DefaultModelBuildingRequest();
        modelBuildingRequest.setPomFile(new File("pom.xml"));
        modelBuildingRequest.setModelResolver(modelResolver);
        modelBuildingRequest.setSystemProperties(System.getProperties());
        
        DefaultModelBuilder modelBuilder = new DefaultModelBuilderFactory().newInstance();
        Model model = modelBuilder.build(modelBuildingRequest).getEffectiveModel();
        System.out.println(model);
    }
}
