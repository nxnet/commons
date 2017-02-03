package io.nxnet.commons.mvnutils.pom.resolver.impl;

import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinition;
import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinitionFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceRegistry;

public class NullProxyDefinitionFactory implements ProxyDefinitionFactory
{
    public ProxyDefinition getProxyDefinition()
    {
        return null;
    }

    public void init(ServiceRegistry serviceLocator)
    {
        // TODO Auto-generated method stub
        
    }

}
