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

import io.nxnet.commons.mvnutils.pom.resolver.ModelResolverBuilder;
import io.nxnet.commons.mvnutils.pom.resolver.RepositoryContext;
import io.nxnet.commons.mvnutils.pom.resolver.impl.DefaultModelResolverBuilder;
import io.nxnet.commons.mvnutils.pom.resolver.impl.DefaultRepositoryContextFactory;

public class ModelResolverBuilderTest
{
    private ModelResolverBuilder modelResolverBuilder;
    
    private RepositoryContext repositoryContext;
    
    @Before
    public void setUp()
    {
        this.modelResolverBuilder = new DefaultModelResolverBuilder();
        
        this.repositoryContext = new DefaultRepositoryContextFactory().getRepositoryContext();
    }

    @Test
    public void testA() throws Exception
    {   
        ModelResolver modelResolver = this.modelResolverBuilder
            .setRepositoryContext(this.repositoryContext)
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
