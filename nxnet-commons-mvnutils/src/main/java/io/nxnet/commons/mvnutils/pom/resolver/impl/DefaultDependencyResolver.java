package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactDescriptorException;
import org.eclipse.aether.resolution.ArtifactDescriptorRequest;
import org.eclipse.aether.resolution.ArtifactDescriptorResult;

import io.nxnet.commons.mvnutils.pom.resolver.DependencyException;
import io.nxnet.commons.mvnutils.pom.resolver.DependencyResolver;
import io.nxnet.commons.mvnutils.pom.resolver.RemoteRepositoryFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RepositorySystemFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RepositorySystemSessionFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceLocator;
import io.nxnet.commons.mvnutils.pom.resolver.TreeNode;

public class DefaultDependencyResolver implements DependencyResolver
{
    private RepositorySystemFactory repositorySystemFactory;
    
    private RepositorySystemSessionFactory repositorySystemSessionFactory;
    
    private RemoteRepositoryFactory remoteRepositoryFactory;
    
    public DefaultDependencyResolver()
    {
        this.repositorySystemFactory = new DefaultRepositorySystemFactory();
        this.repositorySystemSessionFactory = new DefaultRepositorySystemSessionFactory();
        this.remoteRepositoryFactory = new CentralRemoteRepositoryFactory();
    }
    
    public void init()
    {
        this.repositorySystemFactory = ServiceLocator.getInstance().getService(RepositorySystemFactory.class);
        this.repositorySystemSessionFactory = ServiceLocator.getInstance().getService(RepositorySystemSessionFactory.class);
        this.remoteRepositoryFactory = ServiceLocator.getInstance().getService(RemoteRepositoryFactory.class);
    }

    public TreeNode<Dependency> getDependencyTree(String artifactCoordinates) throws DependencyException
    {
        RepositorySystem system = this.repositorySystemFactory.getRepositorySystem();

        RepositorySystemSession session = this.repositorySystemSessionFactory.getRepositorySystemSession();

        // Remote repositories
        List<RemoteRepository> remoteRepositories = Arrays.asList(
                this.remoteRepositoryFactory.getRemoteRepository());
        
        // Artifact
        Artifact artifact = new DefaultArtifact(artifactCoordinates);

        // Artifact descriptor
        ArtifactDescriptorRequest descriptorRequest = new ArtifactDescriptorRequest();
        descriptorRequest.setArtifact(artifact);
        descriptorRequest.setRepositories(remoteRepositories);
        ArtifactDescriptorResult descriptorResult = null;
        try
        {
            descriptorResult = system.readArtifactDescriptor( session, descriptorRequest );
        }
        catch (ArtifactDescriptorException e)
        {
            throw new DependencyException("Error resolving artifact descriptor", e);
        }

        CollectRequest collectRequest = new CollectRequest();
        //collectRequest.setRoot(new org.eclipse.aether.graph.Dependency(artifact, ""));
        //collectRequest.setRepositories(this.repositoryContext.getRemoteRepositories());
        collectRequest.setRootArtifact( descriptorResult.getArtifact() );
        collectRequest.setDependencies( descriptorResult.getDependencies() );
        collectRequest.setManagedDependencies( descriptorResult.getManagedDependencies() );
        collectRequest.setRepositories( descriptorRequest.getRepositories() );

        CollectResult collectResult = null;
        try
        {
            collectResult = system.collectDependencies(session, collectRequest);
        }
        catch (DependencyCollectionException e)
        {
            throw new DependencyException("Error resolving dependencies", e);
        }

        DependencyCollector dependencyCollector = new DependencyCollector();
        collectResult.getRoot().accept(dependencyCollector);
        return dependencyCollector.getRoot();
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

}
