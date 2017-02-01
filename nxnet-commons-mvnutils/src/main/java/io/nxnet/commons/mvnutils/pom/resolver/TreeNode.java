package io.nxnet.commons.mvnutils.pom.resolver;

import java.util.Iterator;
import java.util.List;

public interface TreeNode<E>
{
    E getElement();
    
    TreeNode<E> getParentNode();
    
    List<TreeNode<E>> getChildNodes();
    
    Iterator<TreeNode<E>> iterator();
    
    int getLevel();
    
    int getPosition();
}
