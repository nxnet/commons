package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.io.File;

import org.apache.maven.model.Model;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import io.nxnet.commons.mvnutils.pom.resolver.impl.DefaultModelFactory;

public class DefaultModelFactoryTest
{
    DefaultModelFactory modelFactory;

    @Before
    public void setUp()
    {
        this.modelFactory = new DefaultModelFactory();
        this.modelFactory.addProxy("http", "localhost", 3128, "localhost|127.0.0.1");
    }

    @Test
    public void getModel() throws Exception
    {
        Model model = this.modelFactory.getModel(new File("src/test/resources/pom.xml"));
        assertEquals("foo", model.getProperties().get("prop-security-rest-impl"));
        //assertEquals("bar", model.getProperties().get("prop-security"));
        System.out.println(model.getProperties());
    }
}
