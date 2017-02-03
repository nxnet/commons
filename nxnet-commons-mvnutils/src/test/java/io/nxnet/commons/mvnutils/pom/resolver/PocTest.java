package io.nxnet.commons.mvnutils.pom.resolver;

import java.util.Arrays;
import java.util.List;

import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactDescriptorRequest;
import org.eclipse.aether.resolution.ArtifactDescriptorResult;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator;
import org.junit.Test;

public class PocTest
{
    private RepositorySystemFactory repositorySystemFactory;
    
    private RepositorySystemSessionFactory repositorySystemSessionFactory;
    
    private RemoteRepositoryFactory remoteRepositoryFactory;
    
    public PocTest()
    {
        this.repositorySystemFactory = ServiceLocator.getInstance().getService(RepositorySystemFactory.class);
        this.repositorySystemSessionFactory = ServiceLocator.getInstance().getService(RepositorySystemSessionFactory.class);
        this.remoteRepositoryFactory = ServiceLocator.getInstance().getService(RemoteRepositoryFactory.class);
    }

    @Test
    public void testA() throws Exception
    {
        RepositorySystem repoSystem = this.repositorySystemFactory.getRepositorySystem();
        
        RepositorySystemSession session = this.repositorySystemSessionFactory.getRepositorySystemSession();
 
        Dependency dependency =
            new Dependency( new DefaultArtifact( "org.apache.maven:maven-profile:2.2.1" ), "compile" );
        RemoteRepository central = this.remoteRepositoryFactory.getRemoteRepository();
 
        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot( dependency );
        collectRequest.addRepository( central );
        DependencyNode node = repoSystem.collectDependencies( session, collectRequest ).getRoot();
 
        DependencyRequest dependencyRequest = new DependencyRequest();
        dependencyRequest.setRoot( node );
 
        repoSystem.resolveDependencies( session, dependencyRequest  );
 
        PreorderNodeListGenerator nlg = new PreorderNodeListGenerator();
        node.accept( nlg );
        System.out.println( nlg.getClassPath() );
    }
    
    @Test
    public void resolveArtifact() throws Exception
    {
        System.out.println( "------------------------------------------------------------" );
        System.out.println( this.getClass().getSimpleName() );

        // Repository system
        RepositorySystem system = this.repositorySystemFactory.getRepositorySystem();

        // Repository system session
        RepositorySystemSession session = this.repositorySystemSessionFactory.getRepositorySystemSession();

        // Artifact
        Artifact artifact = new DefaultArtifact(
                "io.nxnet.commons:nxnet-commons-mvnutils:pom:0.1.0-RC8");

        // Remote repositories
        List<RemoteRepository> remoteRepositories = Arrays.asList(this.remoteRepositoryFactory.getRemoteRepository());
        
        // Artifact request 
        ArtifactRequest artifactRequest = new ArtifactRequest();
        artifactRequest.setArtifact(artifact);
        artifactRequest.setRepositories(remoteRepositories);

        // Artifact response
        ArtifactResult artifactResult = system.resolveArtifact(session, artifactRequest);
        artifact = artifactResult.getArtifact();
        System.out.println(">>> Artifact -> " + artifact);
        System.out.println(">>> Resolved -> " + artifact.getFile());
        System.out.println(">>> Proper.s -> " + artifact.getProperties());
        
        // Artifact descriptor request
        ArtifactDescriptorRequest artifactDescriptorRequest = new ArtifactDescriptorRequest();
        artifactDescriptorRequest.setArtifact(artifact);
        artifactDescriptorRequest.setRepositories(remoteRepositories);
        
        // Artifact descriptor response
        ArtifactDescriptorResult artifactDescriptorResult = system
                .readArtifactDescriptor(session, artifactDescriptorRequest);
        System.out.println(artifactDescriptorResult.getProperties());
        
    }
}
