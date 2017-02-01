package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import io.nxnet.commons.mvnutils.pom.resolver.TreeNode;

public class TreeNodeImplTest
{
    private TreeNodeImpl<String> root;

    @Before
    public void setUp()
    {
        this.root = new TreeNodeImpl<String>("root");
    }

    /**
     *          root
     *         /  |  \
     *        1A  1B 1C
     */
    @Test
    public void iteratorTestA()
    {
        this.root.addChildNode(new TreeNodeImpl<String>("1A", this.root));
        this.root.addChildNode(new TreeNodeImpl<String>("1B", this.root));
        this.root.addChildNode(new TreeNodeImpl<String>("1C", this.root));
        TreeNode<String> node = null;
        String nodeName = null;
        int nodeNumber = 0;
        int assertsNumber = 0;
        for (Iterator<TreeNode<String>> nodeIter = this.root.iterator(); nodeIter.hasNext() ; )
        {
            nodeNumber++;
            node = nodeIter.next();
            nodeName = node.getElement();
            if ("root".equals(nodeName))
            {
                assertsNumber++;
                assertEquals("Unexpected node name", "root", node.getElement());
                assertEquals("Unexpected node level", 0, node.getLevel());
                assertEquals("Unexpected node position", 0, node.getPosition());
                assertNull("Unexpected node parent", node.getParentNode());
                assertNotNull("Unexpected node children", node.getChildNodes());
                assertEquals("Unexpected number of node children", 3, node.getChildNodes().size());
            }
            if ("1A".equals(nodeName))
            {
                assertsNumber++;
                assertEquals("Unexpected node name", "1A", node.getElement());
                assertEquals("Unexpected node level", 1, node.getLevel());
                assertEquals("Unexpected node position", 0, node.getPosition());
                assertNotNull("Unexpected node parent", node.getParentNode());
                assertEquals("Unexpected node parent", "root", node.getParentNode().getElement());
                assertNotNull("Unexpected node children", node.getChildNodes());
                assertEquals("Unexpected number of node children", 0, node.getChildNodes().size());
            }
            if ("1B".equals(nodeName))
            {
                assertsNumber++;
                assertEquals("Unexpected node name", "1B", node.getElement());
                assertEquals("Unexpected node level", 1, node.getLevel());
                assertEquals("Unexpected node position", 1, node.getPosition());
                assertNotNull("Unexpected node parent", node.getParentNode());
                assertEquals("Unexpected node parent", "root", node.getParentNode().getElement());
                assertNotNull("Unexpected node children", node.getChildNodes());
                assertEquals("Unexpected number of node children", 0, node.getChildNodes().size());
            }
            if ("1C".equals(nodeName))
            {
                assertsNumber++;
                assertEquals("Unexpected node name", "1C", node.getElement());
                assertEquals("Unexpected node level", 1, node.getLevel());
                assertEquals("Unexpected node position", 2, node.getPosition());
                assertNotNull("Unexpected node parent", node.getParentNode());
                assertEquals("Unexpected node parent", "root", node.getParentNode().getElement());
                assertNotNull("Unexpected node children", node.getChildNodes());
                assertEquals("Unexpected number of node children", 0, node.getChildNodes().size());
            }
        }
        assertEquals("Unexpected number of nodes", 4, nodeNumber);
        assertEquals("Unexpected number of asserts", 4, assertsNumber);
    }

    /**
     *              root
     *         /      |      \
     *        1A     1B      1C
     *       /  \   / | \   /  \
     *      2D  2E 2F 2G 2H 2I 2J 
     */
    @Test
    public void iteratorTestB()
    {
        TreeNodeImpl<String> node1A = new TreeNodeImpl<String>("1A", this.root);
        node1A.addChildNode(new TreeNodeImpl<String>("2D", node1A));
        node1A.addChildNode(new TreeNodeImpl<String>("2E", node1A));
        TreeNodeImpl<String> node1B = new TreeNodeImpl<String>("1B", this.root);
        node1B.addChildNode(new TreeNodeImpl<String>("2F", node1B));
        node1B.addChildNode(new TreeNodeImpl<String>("2G", node1B));
        node1B.addChildNode(new TreeNodeImpl<String>("2H", node1B));
        TreeNodeImpl<String> node1C = new TreeNodeImpl<String>("1C", this.root);
        node1C.addChildNode(new TreeNodeImpl<String>("2I", node1C));
        node1C.addChildNode(new TreeNodeImpl<String>("2J", node1C));
        this.root.addChildNode(node1A);
        this.root.addChildNode(node1B);
        this.root.addChildNode(node1C);
        TreeNode<String> node = null;
        String nodeName = null;
        int nodeNumber = 0;
        int assertsNumber = 0;
        for (Iterator<TreeNode<String>> nodeIter = this.root.iterator(); nodeIter.hasNext() ; )
        {
            nodeNumber++;
            node = nodeIter.next();
            nodeName = node.getElement();
            if ("root".equals(nodeName))
            {
                assertsNumber++;
                assertEquals("Unexpected node name", "root", node.getElement());
                assertEquals("Unexpected node level", 0, node.getLevel());
                assertEquals("Unexpected node position", 0, node.getPosition());
                assertNull("Unexpected node parent", node.getParentNode());
                assertNotNull("Unexpected node children", node.getChildNodes());
                assertEquals("Unexpected number of node children", 3, node.getChildNodes().size());
            }
            if ("1A".equals(nodeName))
            {
                assertsNumber++;
                assertEquals("Unexpected node name", "1A", node.getElement());
                assertEquals("Unexpected node level", 1, node.getLevel());
                assertEquals("Unexpected node position", 0, node.getPosition());
                assertNotNull("Unexpected node parent", node.getParentNode());
                assertEquals("Unexpected node parent", "root", node.getParentNode().getElement());
                assertNotNull("Unexpected node children", node.getChildNodes());
                assertEquals("Unexpected number of node children", 2, node.getChildNodes().size());
            }
            if ("1B".equals(nodeName))
            {
                assertsNumber++;
                assertEquals("Unexpected node name", "1B", node.getElement());
                assertEquals("Unexpected node level", 1, node.getLevel());
                assertEquals("Unexpected node position", 1, node.getPosition());
                assertNotNull("Unexpected node parent", node.getParentNode());
                assertEquals("Unexpected node parent", "root", node.getParentNode().getElement());
                assertNotNull("Unexpected node children", node.getChildNodes());
                assertEquals("Unexpected number of node children", 3, node.getChildNodes().size());
            }
            if ("1C".equals(nodeName))
            {
                assertsNumber++;
                assertEquals("Unexpected node name", "1C", node.getElement());
                assertEquals("Unexpected node level", 1, node.getLevel());
                assertEquals("Unexpected node position", 2, node.getPosition());
                assertNotNull("Unexpected node parent", node.getParentNode());
                assertEquals("Unexpected node parent", "root", node.getParentNode().getElement());
                assertNotNull("Unexpected node children", node.getChildNodes());
                assertEquals("Unexpected number of node children", 2, node.getChildNodes().size());
            }
            if ("2D".equals(nodeName))
            {
                assertsNumber++;
                assertEquals("Unexpected node name", "2D", node.getElement());
                assertEquals("Unexpected node level", 2, node.getLevel());
                assertEquals("Unexpected node position", 0, node.getPosition());
                assertNotNull("Unexpected node parent", node.getParentNode());
                assertEquals("Unexpected node parent", "1A", node.getParentNode().getElement());
                assertNotNull("Unexpected node children", node.getChildNodes());
                assertEquals("Unexpected number of node children", 0, node.getChildNodes().size());
            }
            if ("2E".equals(nodeName))
            {
                assertsNumber++;
                assertEquals("Unexpected node name", "2E", node.getElement());
                assertEquals("Unexpected node level", 2, node.getLevel());
                assertEquals("Unexpected node position", 1, node.getPosition());
                assertNotNull("Unexpected node parent", node.getParentNode());
                assertEquals("Unexpected node parent", "1A", node.getParentNode().getElement());
                assertNotNull("Unexpected node children", node.getChildNodes());
                assertEquals("Unexpected number of node children", 0, node.getChildNodes().size());
            }
            if ("2F".equals(nodeName))
            {
                assertsNumber++;
                assertEquals("Unexpected node name", "2F", node.getElement());
                assertEquals("Unexpected node level", 2, node.getLevel());
                assertEquals("Unexpected node position", 0, node.getPosition());
                assertNotNull("Unexpected node parent", node.getParentNode());
                assertEquals("Unexpected node parent", "1B", node.getParentNode().getElement());
                assertNotNull("Unexpected node children", node.getChildNodes());
                assertEquals("Unexpected number of node children", 0, node.getChildNodes().size());
            }
            if ("2G".equals(nodeName))
            {
                assertsNumber++;
                assertEquals("Unexpected node name", "2G", node.getElement());
                assertEquals("Unexpected node level", 2, node.getLevel());
                assertEquals("Unexpected node position", 1, node.getPosition());
                assertNotNull("Unexpected node parent", node.getParentNode());
                assertEquals("Unexpected node parent", "1B", node.getParentNode().getElement());
                assertNotNull("Unexpected node children", node.getChildNodes());
                assertEquals("Unexpected number of node children", 0, node.getChildNodes().size());
            }
            if ("2H".equals(nodeName))
            {
                assertsNumber++;
                assertEquals("Unexpected node name", "2H", node.getElement());
                assertEquals("Unexpected node level", 2, node.getLevel());
                assertEquals("Unexpected node position", 2, node.getPosition());
                assertNotNull("Unexpected node parent", node.getParentNode());
                assertEquals("Unexpected node parent", "1B", node.getParentNode().getElement());
                assertNotNull("Unexpected node children", node.getChildNodes());
                assertEquals("Unexpected number of node children", 0, node.getChildNodes().size());
            }
            if ("2I".equals(nodeName))
            {
                assertsNumber++;
                assertEquals("Unexpected node name", "2I", node.getElement());
                assertEquals("Unexpected node level", 2, node.getLevel());
                assertEquals("Unexpected node position", 0, node.getPosition());
                assertNotNull("Unexpected node parent", node.getParentNode());
                assertEquals("Unexpected node parent", "1C", node.getParentNode().getElement());
                assertNotNull("Unexpected node children", node.getChildNodes());
                assertEquals("Unexpected number of node children", 0, node.getChildNodes().size());
            }
            if ("2J".equals(nodeName))
            {
                assertsNumber++;
                assertEquals("Unexpected node name", "2J", node.getElement());
                assertEquals("Unexpected node level", 2, node.getLevel());
                assertEquals("Unexpected node position", 1, node.getPosition());
                assertNotNull("Unexpected node parent", node.getParentNode());
                assertEquals("Unexpected node parent", "1C", node.getParentNode().getElement());
                assertNotNull("Unexpected node children", node.getChildNodes());
                assertEquals("Unexpected number of node children", 0, node.getChildNodes().size());
            }
        }
        assertEquals("Unexpected number of asserts", 11, assertsNumber);
        assertEquals("Unexpected number of nodes", 11, nodeNumber);
    }

}
