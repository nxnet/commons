package io.nxnet.commons.mvnutils.pom.resolver.impl;

import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinition;
import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinitionFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceRegistry;

public class LocalhostProxyDefinitionFactory implements ProxyDefinitionFactory
{
    @Override
    public ProxyDefinition getProxyDefinition()
    {
        return new ProxyDefinition("http", "localhost", 3128, "localhost|127.0.0.1");
    }

    public void init(ServiceRegistry serviceLocator)
    {
        // TODO Auto-generated method stub
        
    }

}
