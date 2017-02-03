package io.nxnet.commons.mvnutils.pom.resolver.impl;

import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinition;
import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinitionFactory;

public class NullProxyDefinitionFactory implements ProxyDefinitionFactory
{
    public ProxyDefinition getProxyDefinition()
    {
        return new ProxyDefinition();
    }

    public void init()
    {
        // TODO Auto-generated method stub
        
    }

}
