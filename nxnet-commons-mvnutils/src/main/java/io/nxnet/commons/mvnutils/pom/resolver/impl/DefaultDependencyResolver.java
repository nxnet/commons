package io.nxnet.commons.mvnutils.pom.resolver.impl;

import org.apache.maven.model.Dependency;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.resolution.ArtifactDescriptorException;
import org.eclipse.aether.resolution.ArtifactDescriptorRequest;
import org.eclipse.aether.resolution.ArtifactDescriptorResult;

import io.nxnet.commons.mvnutils.pom.resolver.DependencyException;
import io.nxnet.commons.mvnutils.pom.resolver.DependencyResolver;
import io.nxnet.commons.mvnutils.pom.resolver.RepositoryContext;
import io.nxnet.commons.mvnutils.pom.resolver.TreeNode;

public class DefaultDependencyResolver implements DependencyResolver
{
    private RepositoryContext repositoryContext;

    public DefaultDependencyResolver()
    {
        super();
    }

    public DefaultDependencyResolver(RepositoryContext repositoryContext)
    {
        this.repositoryContext = repositoryContext;
    }

    public TreeNode<Dependency> getDependencyTree(String artifactCoordinates) throws DependencyException
    {
        RepositorySystem system = this.repositoryContext.getRepositorySystem();

        RepositorySystemSession session = this.repositoryContext.getRepositorySystemSession();

        // Artifact
        Artifact artifact = new DefaultArtifact(artifactCoordinates);

        // Artifact descriptor
        ArtifactDescriptorRequest descriptorRequest = new ArtifactDescriptorRequest();
        descriptorRequest.setArtifact( artifact );
        descriptorRequest.setRepositories(this.repositoryContext.getRemoteRepositories());
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
     * @return the repositoryContext
     */
    public RepositoryContext getRepositoryContext()
    {
        return repositoryContext;
    }

    /**
     * @param repositoryContext the repositoryContext to set
     */
    public void setRepositoryContext(RepositoryContext repositoryContext)
    {
        this.repositoryContext = repositoryContext;
    }

}
