package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.Iterator;

import org.apache.maven.model.Dependency;
import org.junit.Before;
import org.junit.Test;

import io.nxnet.commons.mvnutils.pom.resolver.TreeNode;

public class DefaultDependencyFactoryTest
{
    private DefaultDependencyFactory dependencyFactory;

    @Before
    public void setUp()
    {
        this.dependencyFactory = new DefaultDependencyFactory();
//        this.dependencyFactory.addProxy("http", "hrzg-proxy01.etk.extern.eu.ericsson.se", 8080, 
//                "*.ericsson.se|192.168.*|172.17.*|172.17.67.131|127.0.0.1|localhost");
        this.dependencyFactory.addProxy("http", "localhost", 3128, "localhost|127.0.0.1");
    }

    @Test
    public void getDependencies() throws Exception
    {
        TreeNode<Dependency> dependencies = this.dependencyFactory
                .getDependencies(//"hr.ericsson.m2mse.provisioning:m2mse-provisioning-dao-impl:3.1.0-SNAPSHOT");
                "hr.ericsson.m2mse.provisioning:"
                + "m2mse-provisioning-integration-tests:"
                + "3.1.0-SNAPSHOT");
        Iterator<TreeNode<Dependency>> dependenciesIter = dependencies.iterator();
        while (dependenciesIter.hasNext())
        {
            System.out.println(dependenciesIter.next());
        }
    }

}
