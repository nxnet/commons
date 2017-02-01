package io.nxnet.commons.mvnutils.pom.resolver;

import org.apache.maven.model.Dependency;

public interface DependencyFactory
{
    TreeNode<Dependency> getDependencies(String artifact) throws DependencyException;
}
