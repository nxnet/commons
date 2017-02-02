package io.nxnet.commons.mvnutils.pom.resolver;

import org.eclipse.aether.impl.RemoteRepositoryManager;

public interface RemoteRepositoryManagerFactory
{
    RemoteRepositoryManager getRemoteRepositoryManager();
}
