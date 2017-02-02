package io.nxnet.commons.mvnutils.pom.resolver;

import org.eclipse.aether.RepositorySystemSession;

public interface RepositorySystemSessionFactory
{
    RepositorySystemSession getSession();
}
