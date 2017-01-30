package hr.ericsson.m2mse.testutils.pom.resolver.impl;

import java.io.File;

import org.apache.maven.model.Model;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DefaultModelFactoryTest
{
    DefaultModelFactory modelFactory;

    @Before
    public void setUp()
    {
        this.modelFactory = new DefaultModelFactory();
    }

    @Test
    public void getModel() throws Exception
    {
        Model model = this.modelFactory.getModel(new File("src/test/resources/pom.xml"));
        assertEquals("foo", model.getProperties().get("prop-security-rest-impl"));
        assertEquals("bar", model.getProperties().get("prop-security"));
        System.out.println(model.getProperties());
    }
}
