package io.nxnet.commons.mvnutils.pom.resolver;

import org.eclipse.aether.repository.LocalRepository;

public interface LocalRepositoryFactory extends Initializable
{
    LocalRepository getLocalRepository();
}
