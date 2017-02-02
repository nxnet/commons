package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.graph.DependencyVisitor;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.util.artifact.ArtifactIdUtils;
import org.eclipse.aether.util.graph.manager.DependencyManagerUtils;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;
import org.eclipse.aether.version.Version;
import org.eclipse.aether.version.VersionConstraint;

public class DependencyNodeWrapper
{
    private DependencyNode delegee;

    public DependencyNodeWrapper(DependencyNode node)
    {
        this.delegee = node;
    }

    public String getManagedScope()
    {
        String managedScope = null;
        Dependency dependency = this.delegee.getDependency();
        if (dependency != null && dependency.getScope() != null && dependency.getScope().length() > 0)
        {
            managedScope = dependency.getScope();
        }
        else
        {
            managedScope = DependencyManagerUtils.getPremanagedScope(this.delegee);
        }
        
        return managedScope;
    }

    public String getManagedVersion()
    {
        String managedVersion = null;
        Artifact artifact = this.delegee.getArtifact();
        if (artifact != null && artifact.getBaseVersion() != null && artifact.getBaseVersion().length() > 0)
        {
            managedVersion = artifact.getBaseVersion();
        }
        else
        {
            managedVersion = DependencyManagerUtils.getPremanagedVersion(this.delegee);
        }
        
        return managedVersion;
    }
    
    public boolean isOptional()
    {
        return this.getDependency() != null ? this.getDependency().isOptional() : false;
    }
    
    /**
     * @return
     * @see org.eclipse.aether.graph.DependencyNode#getChildren()
     */
    public List<DependencyNode> getChildren()
    {
        return delegee.getChildren();
    }

    /**
     * @param children
     * @see org.eclipse.aether.graph.DependencyNode#setChildren(java.util.List)
     */
    public void setChildren(List<DependencyNode> children)
    {
        delegee.setChildren(children);
    }

    /**
     * @return
     * @see org.eclipse.aether.graph.DependencyNode#getDependency()
     */
    public Dependency getDependency()
    {
        return delegee.getDependency();
    }

    /**
     * @return
     * @see org.eclipse.aether.graph.DependencyNode#getArtifact()
     */
    public Artifact getArtifact()
    {
        return delegee.getArtifact();
    }

    /**
     * @param artifact
     * @see org.eclipse.aether.graph.DependencyNode#setArtifact(org.eclipse.aether.artifact.Artifact)
     */
    public void setArtifact(Artifact artifact)
    {
        delegee.setArtifact(artifact);
    }

    /**
     * @return
     * @see org.eclipse.aether.graph.DependencyNode#getRelocations()
     */
    public List<? extends Artifact> getRelocations()
    {
        return delegee.getRelocations();
    }

    /**
     * @return
     * @see org.eclipse.aether.graph.DependencyNode#getAliases()
     */
    public Collection<? extends Artifact> getAliases()
    {
        return delegee.getAliases();
    }

    /**
     * @return
     * @see org.eclipse.aether.graph.DependencyNode#getVersionConstraint()
     */
    public VersionConstraint getVersionConstraint()
    {
        return delegee.getVersionConstraint();
    }

    /**
     * @return
     * @see org.eclipse.aether.graph.DependencyNode#getVersion()
     */
    public Version getVersion()
    {
        return delegee.getVersion();
    }

    /**
     * @param scope
     * @see org.eclipse.aether.graph.DependencyNode#setScope(java.lang.String)
     */
    public void setScope(String scope)
    {
        delegee.setScope(scope);
    }

    /**
     * @param optional
     * @see org.eclipse.aether.graph.DependencyNode#setOptional(java.lang.Boolean)
     */
    public void setOptional(Boolean optional)
    {
        delegee.setOptional(optional);
    }

    /**
     * @return
     * @see org.eclipse.aether.graph.DependencyNode#getManagedBits()
     */
    public int getManagedBits()
    {
        return delegee.getManagedBits();
    }

    /**
     * @return
     * @see org.eclipse.aether.graph.DependencyNode#getRepositories()
     */
    public List<RemoteRepository> getRepositories()
    {
        return delegee.getRepositories();
    }

    /**
     * @return
     * @see org.eclipse.aether.graph.DependencyNode#getRequestContext()
     */
    public String getRequestContext()
    {
        return delegee.getRequestContext();
    }

    /**
     * @param context
     * @see org.eclipse.aether.graph.DependencyNode#setRequestContext(java.lang.String)
     */
    public void setRequestContext(String context)
    {
        delegee.setRequestContext(context);
    }

    /**
     * @return
     * @see org.eclipse.aether.graph.DependencyNode#getData()
     */
    public Map<?, ?> getData()
    {
        return delegee.getData();
    }

    /**
     * @param data
     * @see org.eclipse.aether.graph.DependencyNode#setData(java.util.Map)
     */
    public void setData(Map<Object, Object> data)
    {
        delegee.setData(data);
    }

    /**
     * @param key
     * @param value
     * @see org.eclipse.aether.graph.DependencyNode#setData(java.lang.Object, java.lang.Object)
     */
    public void setData(Object key, Object value)
    {
        delegee.setData(key, value);
    }

    /**
     * @param visitor
     * @return
     * @see org.eclipse.aether.graph.DependencyNode#accept(org.eclipse.aether.graph.DependencyVisitor)
     */
    public boolean accept(DependencyVisitor visitor)
    {
        return delegee.accept(visitor);
    }
    
}
