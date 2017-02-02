package io.nxnet.commons.mvnutils.pom.resolver;

import org.apache.maven.model.Dependency;

public interface DependencyResolver extends Initializable
{
    TreeNode<Dependency> getDependencyTree(String artifact) throws DependencyException;
}
