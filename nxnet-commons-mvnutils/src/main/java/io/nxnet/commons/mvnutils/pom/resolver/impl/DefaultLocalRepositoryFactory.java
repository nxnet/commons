package io.nxnet.commons.mvnutils.pom.resolver.impl;

import org.eclipse.aether.repository.LocalRepository;

import io.nxnet.commons.mvnutils.pom.resolver.LocalRepositoryFactory;

public class DefaultLocalRepositoryFactory implements LocalRepositoryFactory
{
    public LocalRepository getLocalRepository()
    {
        return new LocalRepository("target/local-repo");
    }

    public void init()
    {
        // TODO Auto-generated method stub
        
    }
}
