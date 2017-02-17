package io.nxnet.commons.mvnutils.pom.resolver;

import java.util.List;

import org.eclipse.aether.repository.RemoteRepository;

public interface RemoteRepositoryFactory extends Initializable
{
    List<RemoteRepository> getRemoteRepositories();
}
