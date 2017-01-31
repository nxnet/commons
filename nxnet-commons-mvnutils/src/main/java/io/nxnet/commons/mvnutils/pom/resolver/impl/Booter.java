/*******************************************************************************
 * Copyright (c) 2010, 2014 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sonatype, Inc. - initial API and implementation
 *******************************************************************************/
package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.Proxy;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.util.repository.DefaultProxySelector;

/**
 * A helper to boot the repository system and a repository system session.
 */
public class Booter
{

    public static RepositorySystem newRepositorySystem()
    {
        return ManualRepositorySystemFactory.newRepositorySystem();
        // return org.eclipse.aether.examples.guice.GuiceRepositorySystemFactory.newRepositorySystem();
        // return org.eclipse.aether.examples.sisu.SisuRepositorySystemFactory.newRepositorySystem();
        // return org.eclipse.aether.examples.plexus.PlexusRepositorySystemFactory.newRepositorySystem();
    }

    public static DefaultRepositorySystemSession newRepositorySystemSession( RepositorySystem system )
    {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        LocalRepository localRepo = new LocalRepository( "target/local-repo" );
        session.setLocalRepositoryManager( system.newLocalRepositoryManager( session, localRepo ) );

        session.setTransferListener( new ConsoleTransferListener() );
        session.setRepositoryListener( new ConsoleRepositoryListener() );

        // uncomment to generate dirty trees
        // session.setDependencyGraphTransformer( null );
        
        //DefaultProxySelector proxySelector = new DefaultProxySelector();
        //proxySelector.add(new Proxy("http", "localhost", 3128), "localhost|127.0.0.1");
        //session.setProxySelector(proxySelector);

        return session;
    }

    public static List<RemoteRepository> newRepositories( RepositorySystem system, RepositorySystemSession session )
    {
        return newRepositories();
    }
    
    public static List<RemoteRepository> newRepositories()
    {
        return new ArrayList<RemoteRepository>(Arrays.asList(
                newLocalRepository()/*, newEtkcRepository(), newCentralRepository()*/));
    }

    private static RemoteRepository newCentralRepository()
    {
        return new RemoteRepository.Builder( "central", "default", "http://central.maven.org/maven2/" )
                /*.setProxy(new Proxy("http", "localhost", 3128))*/.build();
    }

    private static RemoteRepository newEtkcRepository()
    {
        return new RemoteRepository.Builder( "etkc", "default", "http://ehrzgux504.etk.extern.eu.ericsson.se/content/groups/parent/" )
                /*.setProxy(new Proxy("http", "localhost", 3128))*/.build();
    }

    private static RemoteRepository newLocalRepository()
    {
        return new RemoteRepository.Builder( "local", "default", getLocalRepoLocation().toExternalForm() )
                .build();
    }
    
    private static URL getLocalRepoLocation()
    {
        String localRepoPath = System.getProperty("user.home") + "/.m2/repository/";
        try
        {
            return new URL("file://" + localRepoPath);
        }
        catch (MalformedURLException e)
        {
            throw new IllegalStateException(e);
        }
    }

}
