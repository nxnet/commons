package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.Iterator;
import java.util.Stack;

import org.apache.maven.model.Dependency;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.graph.DependencyVisitor;

import io.nxnet.commons.mvnutils.pom.resolver.TreeNode;

public class DependencyCollector implements DependencyVisitor
{
    private TreeNodeImpl<Dependency> root;
    
    private Stack<ChildrenCounter> childrenCounters = new Stack<>();
    
    public boolean visitEnter(DependencyNode node)
    {
        // Wrap argument
        DependencyNodeWrapper wrapper = new DependencyNodeWrapper(node);
        
        // Extract node child number
        Integer children = wrapper.getChildren() != null ? wrapper.getChildren().size() : 0;
                
        // Set maven dependency node
        Artifact artifact = wrapper.getArtifact();
        Dependency dependency = new DependencyWrapper(new Dependency());
        dependency.setGroupId(artifact.getGroupId());
        dependency.setArtifactId(artifact.getArtifactId());
        dependency.setVersion(wrapper.getManagedVersion());
        dependency.setClassifier(artifact.getClassifier());
        dependency.setType(artifact.getExtension());
        dependency.setScope(wrapper.getManagedScope());
        dependency.setOptional(wrapper.isOptional());
        
        // Put maven dependency node into tree
        if (this.root == null)
        {
            this.root = new TreeNodeImpl<Dependency>(dependency);
            this.childrenCounters.push(new ChildrenCounter(children));
        }
        else
        {
            // Find applicable node in tree
            Iterator<TreeNode<Dependency>> treeIter =  this.root.iterator();
            TreeNodeImpl<Dependency> treeNode = null;
            while (treeIter.hasNext())
            {
                treeNode = (TreeNodeImpl<Dependency>)treeIter.next();
            }
            treeNode = (TreeNodeImpl<Dependency>) this.goUpTheTreeIfNeeded(treeNode);
            
            // Put dependency as child node to applicable node
            if (treeNode != null)
            {
                treeNode.addChildNode(new TreeNodeImpl<Dependency>(dependency, treeNode));
                this.childrenCounters.push(new ChildrenCounter(children));
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

    private TreeNode<Dependency> goUpTheTreeIfNeeded(TreeNode<Dependency> treeNode)
    {
        if (treeNode == null)
        {
            return null;
        }
        
        ChildrenCounter childCounter = this.childrenCounters.peek();
        if (childCounter.getValue() > 0)
        {
            childCounter.decrement();
            return treeNode;
        }
        else
        {
            this.childrenCounters.pop();
            return this.goUpTheTreeIfNeeded(treeNode.getParentNode());
        }
    }

    private class ChildrenCounter
    {
        private int children;
        
        private ChildrenCounter(int children)
        {
            if (children < 0)
            {
                throw new IllegalArgumentException("Child number can't be negative");
            }
            
            this.children = children;
        }
        
        public int getValue()
        {
            return this.children;
        }

        public int decrement()
        {
            if (this.children <= 0)
            {
                throw new IllegalStateException("Can't decrement non positive values");
            }
            
            return --this.children;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            return Integer.toString(children);
        }
        
    }

}
