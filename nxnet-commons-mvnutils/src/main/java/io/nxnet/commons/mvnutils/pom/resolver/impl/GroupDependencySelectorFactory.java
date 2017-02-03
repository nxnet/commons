package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.aether.collection.DependencySelector;

import io.nxnet.commons.mvnutils.pom.resolver.DependencySelectorFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceRegistry;

public class GroupDependencySelectorFactory implements DependencySelectorFactory
{
    private Set<String> groups;
    
    public GroupDependencySelectorFactory(String ...groups)
    {
        this.groups = new HashSet<String>(Arrays.asList(groups));
    }
    
    public void init(ServiceRegistry serviceLocator)
    {
        // Do nothing
    }

    public DependencySelector getDependencySelector()
    {
        GroupDependencySelector dependencySelector = new GroupDependencySelector(this.groups);
        return dependencySelector;
    }

}
