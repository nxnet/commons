package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import io.nxnet.commons.mvnutils.pom.resolver.Model;
import io.nxnet.commons.mvnutils.pom.resolver.ModelFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceRegistry;

public class DefaultModelFactoryTest
{
    ModelFactory modelFactory;

    @Before
    public void setUp()
    {
        //this.modelFactory = new DefaultModelFactory();
        this.modelFactory = ServiceRegistry.getInstance().getService(ModelFactory.class);
    }

    @Test
    public void getModel() throws Exception
    {
        Model model = this.modelFactory.getModel(new File("pom.xml"));
        assertEquals("foo", model.getProperties().get("project-property"));
        assertEquals("baz", model.getProperties().get("project-property-override"));
        System.out.println(model.getDependencyTree());
    }
}
