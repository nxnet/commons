package io.nxnet.commons.mvnutils.pom.resolver.impl;

import org.eclipse.aether.repository.LocalRepository;

import io.nxnet.commons.mvnutils.pom.resolver.LocalRepositoryFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceRegistry;

public class DefaultLocalRepositoryFactory implements LocalRepositoryFactory
{
    private String localRepoPath;
    
    public DefaultLocalRepositoryFactory()
    {
        this.localRepoPath = System.getProperty("java.io.tmpdir") + "/.m2/repository";
    }
    
    public LocalRepository getLocalRepository()
    {
        return new LocalRepository(this.localRepoPath);
    }

    public void init(ServiceRegistry serviceLocator)
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * @return the localRepoPath
     */
    public String getLocalRepoPath()
    {
        return localRepoPath;
    }

    /**
     * @param localRepoPath the localRepoPath to set
     */
    public void setLocalRepoPath(String localRepoPath)
    {
        this.localRepoPath = localRepoPath;
    }
    
}
