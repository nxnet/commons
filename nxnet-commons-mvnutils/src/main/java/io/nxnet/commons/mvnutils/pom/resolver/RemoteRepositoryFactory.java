package io.nxnet.commons.mvnutils.pom.resolver;

import org.eclipse.aether.repository.RemoteRepository;

public interface RemoteRepositoryFactory
{
    RemoteRepository getRemoteRepository();
}
