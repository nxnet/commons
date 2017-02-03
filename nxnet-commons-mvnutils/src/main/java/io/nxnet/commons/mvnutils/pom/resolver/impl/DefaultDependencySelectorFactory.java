package io.nxnet.commons.mvnutils.pom.resolver.impl;

import org.eclipse.aether.collection.DependencySelector;
import org.eclipse.aether.util.graph.selector.AndDependencySelector;
import org.eclipse.aether.util.graph.selector.ExclusionDependencySelector;
import org.eclipse.aether.util.graph.selector.OptionalDependencySelector;
import org.eclipse.aether.util.graph.selector.ScopeDependencySelector;

import io.nxnet.commons.mvnutils.pom.resolver.DependencySelectorFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceRegistry;

public class DefaultDependencySelectorFactory implements DependencySelectorFactory
{

    public void init(ServiceRegistry serviceLocator)
    {
        // do nothing
    }

    public DependencySelector getDependencySelector()
    {
        DependencySelector depFilter = new AndDependencySelector( 
                new ScopeDependencySelector( "test", "provided" ), 
                    new OptionalDependencySelector(), new ExclusionDependencySelector() );
        return depFilter;
    }

}
