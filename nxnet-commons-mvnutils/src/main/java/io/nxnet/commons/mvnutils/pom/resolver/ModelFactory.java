package io.nxnet.commons.mvnutils.pom.resolver;

import java.io.File;

import org.apache.maven.model.Model;

public interface ModelFactory
{
    public Model getModel(File pom) throws ModelException;
}
