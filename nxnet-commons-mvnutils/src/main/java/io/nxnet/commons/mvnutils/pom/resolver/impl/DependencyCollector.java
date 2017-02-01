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
        Artifact nodeArtifact = node.getArtifact();
        
        Dependency dependency = new Dependency();
        dependency.setGroupId(nodeArtifact.getGroupId());
        dependency.setArtifactId(nodeArtifact.getArtifactId());
        dependency.setVersion(nodeArtifact.getVersion());
        dependency.setClassifier(nodeArtifact.getClassifier());
        dependency.setType(nodeArtifact.getExtension());
        
        if (this.root == null)
        {
            this.root = new TreeNodeImpl<Dependency>(dependency);
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
                leaf.addChildNode(new TreeNodeImpl<Dependency>(dependency, leaf));
            }
            else
            {
                ((TreeNodeImpl<Dependency>)leaf.getParentNode()).addChildNode(
                        new TreeNodeImpl<Dependency>(dependency, leaf.getParentNode()));
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
