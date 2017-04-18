package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.Iterator;

import org.apache.maven.model.Dependency;
import org.junit.Before;
import org.junit.Test;

import io.nxnet.commons.mvnutils.pom.resolver.TreeNode;

public class DefaultDependencyResolverTest
{
    private DefaultDependencyResolver defaultDependencyResolver;

    @Before
    public void setUp()
    {
        this.defaultDependencyResolver = new DefaultDependencyResolver();
    }

    @Test
    public void getDependencies() throws Exception
    {
        TreeNode<Dependency> dependencies = this.defaultDependencyResolver
                .getDependencyTree(//"hr.ericsson.m2mse.provisioning:m2mse-provisioning-dao-impl:3.1.0-SNAPSHOT");
                "io.nxnet.commons:"
                + "nxnet-commons-mvnutils:"
                + "0.1.0-RC13-SNAPSHOT");
        Iterator<TreeNode<Dependency>> dependenciesIter = dependencies.iterator();
        while (dependenciesIter.hasNext())
        {
            System.out.println(dependenciesIter.next());
        }
    }

    @Test
    public void getM2mseCoreDataIoDependencies() throws Exception
    {
        TreeNode<Dependency> dependencies = this.defaultDependencyResolver
                .getDependencyTree(
                "hr.ericsson.m2mse.core:"
                + "m2mse-core-data-io:"
                + "3.1.0-SNAPSHOT");
        System.out.println(dependencies);
    }

}
