package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.nxnet.commons.mvnutils.pom.resolver.TreeNode;

public class TreeNodeImpl<E> implements TreeNode<E>
{
    private E element;
    
    private TreeNode<E> parentNode;
    
    private List<TreeNode<E>> childNodes;
    
    public TreeNodeImpl()
    {
        this(null);
    }

    public TreeNodeImpl(E element)
    {
        this(element, null);
    }

    public TreeNodeImpl(E element, TreeNode<E> parentNode)
    {
        this(element, parentNode, new ArrayList<TreeNode<E>>());
    }

    public TreeNodeImpl(E element, TreeNode<E> parentNode, List<TreeNode<E>> childNodes)
    {
        this.element = element;
        this.parentNode = parentNode;
        if (childNodes == null)
        {
            this.childNodes = new ArrayList<TreeNode<E>>();
        }
        this.childNodes = childNodes;
    }

    /**
     * @return the element
     */
    public E getElement()
    {
        return element;
    }

    /**
     * @param element the element to set
     */
    public void setElement(E element)
    {
        this.element = element;
    }

    /**
     * @return the parentNode
     */
    public TreeNode<E> getParentNode()
    {
        return parentNode;
    }

    /**
     * @param parentNode the parentNode to set
     */
    public void setParentNode(TreeNode<E> parentNode)
    {
        this.parentNode = parentNode;
    }

    /**
     * @return the childNodes
     */
    public List<TreeNode<E>> getChildNodes()
    {
        return childNodes;
    }

    /**
     * @param childNodes the childNodes to set
     */
    public void setChildNodes(List<TreeNode<E>> childNodes)
    {
        this.childNodes = childNodes;
    }

    /**
     * @param childNodes the childNodes to set
     */
    public void addChildNode(TreeNode<E> childNode)
    {
        if (this.childNodes == null)
        {
            this.childNodes = new ArrayList<TreeNode<E>>();
        }
        this.childNodes.add(childNode);
    }

    /**
     * @return the level
     */
    public int getLevel()
    {
        int level = 0;
        TreeNode<E> parentNode = this.getParentNode();
        while (parentNode != null)
        {
            level++;
            parentNode = parentNode.getParentNode();
        }
        return level;
    }

    /**
     * @return the position
     */
    public int getPosition()
    {
        int position = 0;
        TreeNode<E> parentNode = this.getParentNode();
        if (parentNode != null)
        {
            position = parentNode.getChildNodes().indexOf(this);
        }
        return position;
    }

    public Iterator<TreeNode<E>> iterator()
    {
        return new NodeIterator(this);
    }
    
    private class NodeIterator implements Iterator<TreeNode<E>>
    {   
        private int cursor;

        private int lastValidCursorPosition;
        
        private TreeNode<E> node;
        
        List<TreeNode<E>> childNodes;
        
        private Iterator<TreeNode<E>> grandchildrenIter = null;
        
        public NodeIterator(TreeNode<E> node)
        {
            this.node = node;
            this.childNodes = node.getChildNodes();
            this.cursor = -1;
            this.lastValidCursorPosition = this.childNodes != null ? this.childNodes.size() -1 : -1;
        }
        
        public boolean hasNext()
        {
            return this.cursorAtStartPosition() || this.cursorNotAtEndPosition() 
                    || this.cursorAtEndPositionButWeHaveGrandchildren(); 
        }
        
        public TreeNode<E> next()
        {
            TreeNode<E> nextNode = null;
            if (cursor == -1)
            {
                nextNode = node;
                moveCursor();
            }
            else if (cursor >= 0 && cursor <= lastValidCursorPosition)
            {
                // If we have no more grandchildren for current child
                if (!grandchildrenIter.hasNext())
                {
                    // Move to next child
                    moveCursor();
                }
                
                // Fetch next child or its grandchild
                if (grandchildrenIter != null)
                {
                    nextNode = grandchildrenIter.next();
                }
            }
            else 
            {
                throw new IllegalStateException("Invalid cursor position: " + cursor);
            }
            
            return nextNode;
        }

        public void remove()
        {
            throw new UnsupportedOperationException();
        }
        
        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString()
        {
            return "NodeIterator [cursor=" + cursor + ", lastValidCursorPosition=" + lastValidCursorPosition + ", node="
                    + node + "]";
        }

        private void moveCursor()
        {
            cursor++;
            if (cursor <= lastValidCursorPosition)
            {
                this.grandchildrenIter = this.childNodes.get(cursor).iterator();
            }
        }

        private boolean cursorAtStartPosition()
        {
            return this.cursor == -1;
        }

        private boolean cursorNotAtEndPosition()
        {
            return this.cursor < this.lastValidCursorPosition;
        }

        private boolean cursorAtEndPositionButWeHaveGrandchildren()
        {
            return this.cursor == this.lastValidCursorPosition 
                    && this.grandchildrenIter != null && this.grandchildrenIter.hasNext();
        }

    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        //return "TreeNodeImpl [element=" + element + "]";
        StringBuilder toStringBuilder = new StringBuilder()
                .append("<<<<<<<<<<<<<<<<<<<<<<<<<<<<< TreeNodeImpl >>>>>>>>>>>>>>>>>>>>>>>>>>>>>").append("\n");
        Iterator<TreeNode<E>> iter = this.iterator();
        while (iter.hasNext())
        {
            toStringBuilder.append(iter.next().getElement()).append("\n");
        }
        toStringBuilder.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>> TreeNodeImpl <<<<<<<<<<<<<<<<<<<<<<<<<<<<<").append("\n");
        return toStringBuilder.toString();
    }

}
