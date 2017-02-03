package io.nxnet.commons.mvnutils.pom.resolver;

import org.eclipse.aether.collection.DependencySelector;

public interface DependencySelectorFactory extends Initializable
{
    DependencySelector getDependencySelector();
}
