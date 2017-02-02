package io.nxnet.commons.mvnutils.pom.resolver;

import org.eclipse.aether.RepositorySystem;

public interface RepositorySystemFactory
{
    RepositorySystem getRepositorySystem();
}
