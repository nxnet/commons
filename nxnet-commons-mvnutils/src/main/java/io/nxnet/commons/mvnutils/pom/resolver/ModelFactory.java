package io.nxnet.commons.mvnutils.pom.resolver;

import java.io.File;

public interface ModelFactory extends Initializable
{
    public Model getModel(File pom) throws ModelException;
}
