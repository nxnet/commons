package io.nxnet.commons.mvnutils.pom.resolver;

import java.io.File;

public interface ModelFactory
{
    public Model getModel(File pom) throws ModelException;
}
