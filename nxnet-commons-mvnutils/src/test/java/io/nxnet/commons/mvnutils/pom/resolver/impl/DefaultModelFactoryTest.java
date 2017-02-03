package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.io.File;

import org.apache.maven.model.Model;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import io.nxnet.commons.mvnutils.pom.resolver.ModelFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceLocator;

public class DefaultModelFactoryTest
{
    ModelFactory modelFactory;

    @Before
    public void setUp()
    {
        //this.modelFactory = new DefaultModelFactory();
        this.modelFactory = ServiceLocator.getInstance().getService(ModelFactory.class);
    }

    @Test
    public void getModel() throws Exception
    {
        Model model = this.modelFactory.getModel(new File("src/test/resources/pom.xml"));
        assertEquals("foo", model.getProperties().get("myprop"));
        //assertEquals("bar", model.getProperties().get("prop-security"));
        System.out.println(model.getProperties());
    }
}
