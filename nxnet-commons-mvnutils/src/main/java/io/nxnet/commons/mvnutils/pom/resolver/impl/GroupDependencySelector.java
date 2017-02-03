package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.collection.DependencyCollectionContext;
import org.eclipse.aether.collection.DependencySelector;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.Exclusion;

public class GroupDependencySelector implements DependencySelector
{

    private Set<String> groups = new HashSet<String>();
    
    private Set<Exclusion> exclusions = new HashSet<Exclusion>();
    
    public GroupDependencySelector(String... groups)
    {
        super();
        this.groups = new HashSet<String>(Arrays.asList(groups));
    }
    
    public GroupDependencySelector(Set<String> groups)
    {
        super();
        this.groups = groups;
    }

    public boolean selectDependency(Dependency dependency)
    {
        Artifact artifact = dependency.getArtifact();
        
        // If we have explicit exclude for the dependency, reject it
        for (Exclusion exclusion : exclusions)
        {
            if (exclusion.getArtifactId().equals(artifact.getArtifactId())
                    && exclusion.getGroupId().equals(artifact.getGroupId()))
            {
                return false;
            }
        }
        
        // If dependency groups starts with one of group patterns we have a match
        for (String group : this.groups)
        {
            if (artifact.getGroupId().startsWith(group))
            {
                return true;
            }
        }
        
        // If we got here, reject the dependency
        return false;
    }

    public DependencySelector deriveChildSelector(DependencyCollectionContext context)
    {
        Collection<Exclusion> exclusions = context.getDependency() != null ? context.getDependency().getExclusions()
                : null;
        if (exclusions != null && !exclusions.isEmpty())
        {
            GroupDependencySelector groupDependencySelector = new GroupDependencySelector(this.groups);
            groupDependencySelector.exclusions.addAll(exclusions);
            return groupDependencySelector;
        }
        
        return this;
    }

}
