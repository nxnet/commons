package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

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
        List<TreeNode<E>> siblings = this.getSiblings();
        return siblings != null && !siblings.isEmpty() ? siblings.indexOf(this) : 0;
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
        StringBuilder toStringBuilder = new StringBuilder();
        int longestLineSize = 0;
        
        Stack<String> prefixes = new Stack<>();
        TreeNode<E> node = null;
        Iterator<TreeNode<E>> iter = this.iterator();
        while (iter.hasNext())
        {
            // Extract nodes
            node = iter.next();
            
            // Calculate node prefix
            adjustPrefixes(prefixes, node);
            
            // Compose node print line
            int lineSize = 0;
            String nodeString = node.getElement().toString();
            for (String prefix : prefixes)
            {
                toStringBuilder.append(prefix);
                lineSize = lineSize + prefix.length(); 
            }
            toStringBuilder.append(node.isLastSibling() ? "\\- " : "+- ");
            lineSize = lineSize + 3;
            toStringBuilder.append(nodeString);
            lineSize = lineSize + nodeString.length();
            toStringBuilder.append("\n");
            
            // Memorize longest line size
            if (lineSize > longestLineSize)
            {
                longestLineSize = lineSize;
            }
        }
        
        // Set header and footer
        String prefix = "*";
        String sufix = "*";
        String token = "*";
        String headerText = prefix + " Tree node: " + this.getElement() + " " + sufix;
        if (headerText.length() > longestLineSize)
        {
            // recalculate longest line once again because of header text
            longestLineSize = headerText.length();
        }
        else
        {
            // rewrite header text
            headerText = headerText.substring(0, headerText.length() - sufix.length()) 
                    + repeat(" ", longestLineSize - headerText.length()) + sufix;
        }
        String lineDelimiter = prefix 
                + repeat(token, longestLineSize - (prefix.length() + sufix.length())) + sufix +"\n";
        toStringBuilder.insert(0, lineDelimiter + headerText + "\n" + lineDelimiter);
        toStringBuilder.append(lineDelimiter);
        
        return toStringBuilder.toString();
    }

    @Override
    public boolean isLastSibling()
    {
        return (this.getPosition() == (this.getSiblings().size() - 1));
    }

    @Override
    public List<TreeNode<E>> getSiblings()
    {
        List<TreeNode<E>> siblings = new ArrayList<>();
        if (this.getParentNode() != null)
        {
            siblings.addAll(this.getParentNode().getChildNodes());
        }
        return siblings;
    }

    private String repeat(String s, int n)
    {
        return new String(new char[n]).replace("\0", s);
    }
    
    private void adjustPrefixes(Stack<String> prefixes, TreeNode<E> node)
    {
        if (prefixes.size() < node.getLevel()) // We have less prefixes than required by node level
        {
            if (node.getParentNode() != null && node.getParentNode().isLastSibling())
            {
                prefixes.push("   ");
            }
            else
            {
                prefixes.push("|  ");
            }
            adjustPrefixes(prefixes, node);
        }
        else if (prefixes.size() > node.getLevel()) // We have more prefixes than required by node level
        {
            prefixes.pop();
            adjustPrefixes(prefixes, node);
        }
        else // Number of prefixes matches node level
        {
            // Prefixes are adjusted, return
            return;
        }
    }

}
