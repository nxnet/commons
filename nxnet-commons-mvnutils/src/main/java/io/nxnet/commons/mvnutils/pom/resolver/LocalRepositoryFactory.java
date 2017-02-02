package io.nxnet.commons.mvnutils.pom.resolver;

import org.eclipse.aether.repository.LocalRepository;

public interface LocalRepositoryFactory
{
    LocalRepository getLocalRepository();
}
