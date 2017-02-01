package io.nxnet.commons.mvnutils.pom.resolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.repository.Proxy;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactDescriptorRequest;
import org.eclipse.aether.resolution.ArtifactDescriptorResult;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator;
import org.junit.Test;

import io.nxnet.commons.mvnutils.pom.resolver.impl.Booter;

public class PocTest
{
    private Proxy proxy;
    
    private Map<Proxy, String> proxies; 

    public PocTest()
    {
        this.proxy = new Proxy("http", "localhost", 3128);
        this.proxies = new HashMap<Proxy, String>();
        this.proxies.put(this.proxy, "localhost|127.0.0.1");
    }

    @Test
    public void testA() throws Exception
    {
        RepositorySystem repoSystem = Booter.newRepositorySystem();
        
        RepositorySystemSession session = Booter.newRepositorySystemSession(repoSystem, this.proxies);
 
        Dependency dependency =
            new Dependency( new DefaultArtifact( "org.apache.maven:maven-profile:2.2.1" ), "compile" );
        RemoteRepository central = new RemoteRepository.Builder( "central", "default", "http://repo1.maven.org/maven2/" ).build();
 
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
        RepositorySystem system = Booter.newRepositorySystem();

        // Repository system session
        RepositorySystemSession session = Booter.newRepositorySystemSession(system, proxies);

        // Artifact
        Artifact artifact = new DefaultArtifact(
                "hr.ericsson.m2mse.security:m2mse-security-rest-impl:pom:3.1.0-SNAPSHOT");

        // Remote repositories
        List<RemoteRepository> remoteRepositories = Booter.newRepositories(this.proxy);
        
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
