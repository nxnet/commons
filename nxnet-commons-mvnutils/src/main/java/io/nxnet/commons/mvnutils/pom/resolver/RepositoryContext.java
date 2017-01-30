package io.nxnet.commons.mvnutils.pom.resolver;

import java.util.List;

import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.impl.RemoteRepositoryManager;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;

public interface RepositoryContext
{
    RepositorySystemSession getRepositorySystemSession();

    RepositorySystem getRepositorySystem();

    LocalRepository getLocalRepository();
    
    List<RemoteRepository> getRemoteRepositories();
    
    RemoteRepositoryManager getRemoteRepositoryManager();
}
