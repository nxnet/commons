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
        this.repositorySystemFactory = ServiceLocator.getInstance().getService(RepositorySystemFactory.class);
        this.repositorySystemSessionFactory = ServiceLocator.getInstance().getService(RepositorySystemSessionFactory.class);
        this.remoteRepositoryFactory = ServiceLocator.getInstance().getService(RemoteRepositoryFactory.class);
        this.remoteRepositoryManagerFactory = ServiceLocator.getInstance().getService(RemoteRepositoryManagerFactory.class);
    }

    @Test
    public void testA() throws Exception
    {   
        ModelResolver modelResolver = this.modelResolverBuilder
            .setRepositorySystem(this.repositorySystemFactory.getRepositorySystem())
            .setRepositorySystemSession(this.repositorySystemSessionFactory.getRepositorySystemSession())
            .addRemoteRepository(this.remoteRepositoryFactory.getRemoteRepository())
            .setRemoteRepositoryManager(this.remoteRepositoryManagerFactory.getRemoteRepositoryManager())
            .setRepositoryMerging(RepositoryMerging.REQUEST_DOMINANT)
            .setRequestTrace(new RequestTrace(null))
            .build();
        
        DefaultModelBuildingRequest modelBuildingRequest = new DefaultModelBuildingRequest();
        modelBuildingRequest.setPomFile(new File("src/test/resources/pom.xml"));
        modelBuildingRequest.setModelResolver(modelResolver);
        modelBuildingRequest.setSystemProperties(System.getProperties());
        
        DefaultModelBuilder modelBuilder = new DefaultModelBuilderFactory().newInstance();
        Model model = modelBuilder.build(modelBuildingRequest).getEffectiveModel();
        System.out.println(model);
    }
}
