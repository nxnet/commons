package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.Iterator;

import org.apache.maven.model.Dependency;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.graph.DependencyVisitor;

import io.nxnet.commons.mvnutils.pom.resolver.TreeNode;

public class DependencyCollector implements DependencyVisitor
{
    private TreeNodeImpl<Dependency> root;

    public boolean visitEnter(DependencyNode node)
    {
        DependencyNodeWrapper wrapper = new DependencyNodeWrapper(node);
        
        Artifact artifact = wrapper.getArtifact();
        
        Dependency mavenDependency = new Dependency();
        mavenDependency.setGroupId(artifact.getGroupId());
        mavenDependency.setArtifactId(artifact.getArtifactId());
        mavenDependency.setVersion(wrapper.getManagedVersion());
        mavenDependency.setClassifier(artifact.getClassifier());
        mavenDependency.setType(artifact.getExtension());
        mavenDependency.setScope(wrapper.getManagedScope());
        mavenDependency.setOptional(wrapper.isOptional());
        
        if (this.root == null)
        {
            this.root = new TreeNodeImpl<Dependency>(mavenDependency);
        }
        else
        {
            Iterator<TreeNode<Dependency>> treeIter =  this.root.iterator();
            TreeNodeImpl<Dependency> leaf = null;
            while (treeIter.hasNext())
            {
                leaf = (TreeNodeImpl<Dependency>)treeIter.next();
            }
            
            if (leaf.getParentNode() == null || node.getChildren().size() > 0)
            {
                leaf.addChildNode(new TreeNodeImpl<Dependency>(mavenDependency, leaf));
            }
            else
            {
                ((TreeNodeImpl<Dependency>)leaf.getParentNode()).addChildNode(
                        new TreeNodeImpl<Dependency>(mavenDependency, leaf.getParentNode()));
            }
        }
        
        return true;
    }

    public boolean visitLeave(DependencyNode node)
    {
        return true;
    }

    /**
     * @return the root
     */
    public TreeNodeImpl<Dependency> getRoot()
    {
        return root;
    }

    /**
     * @param root the root to set
     */
    public void setRoot(TreeNodeImpl<Dependency> root)
    {
        this.root = root;
    }

}
