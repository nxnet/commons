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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.repository.internal.DefaultArtifactDescriptorReader;
import org.apache.maven.repository.internal.DefaultVersionRangeResolver;
import org.apache.maven.repository.internal.DefaultVersionResolver;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.apache.maven.repository.internal.SnapshotMetadataGeneratorFactory;
import org.apache.maven.repository.internal.VersionsMetadataGeneratorFactory;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.ArtifactDescriptorReader;
import org.eclipse.aether.impl.ArtifactResolver;
import org.eclipse.aether.impl.DependencyCollector;
import org.eclipse.aether.impl.Deployer;
import org.eclipse.aether.impl.Installer;
import org.eclipse.aether.impl.LocalRepositoryProvider;
import org.eclipse.aether.impl.MetadataGeneratorFactory;
import org.eclipse.aether.impl.MetadataResolver;
import org.eclipse.aether.impl.OfflineController;
import org.eclipse.aether.impl.RemoteRepositoryManager;
import org.eclipse.aether.impl.RepositoryConnectorProvider;
import org.eclipse.aether.impl.RepositoryEventDispatcher;
import org.eclipse.aether.impl.SyncContextFactory;
import org.eclipse.aether.impl.UpdateCheckManager;
import org.eclipse.aether.impl.UpdatePolicyAnalyzer;
import org.eclipse.aether.impl.VersionRangeResolver;
import org.eclipse.aether.impl.VersionResolver;
import org.eclipse.aether.internal.impl.DefaultArtifactResolver;
import org.eclipse.aether.internal.impl.DefaultChecksumPolicyProvider;
import org.eclipse.aether.internal.impl.DefaultDependencyCollector;
import org.eclipse.aether.internal.impl.DefaultDeployer;
import org.eclipse.aether.internal.impl.DefaultFileProcessor;
import org.eclipse.aether.internal.impl.DefaultInstaller;
import org.eclipse.aether.internal.impl.DefaultLocalRepositoryProvider;
import org.eclipse.aether.internal.impl.DefaultMetadataResolver;
import org.eclipse.aether.internal.impl.DefaultOfflineController;
import org.eclipse.aether.internal.impl.DefaultRemoteRepositoryManager;
import org.eclipse.aether.internal.impl.DefaultRepositoryConnectorProvider;
import org.eclipse.aether.internal.impl.DefaultRepositoryEventDispatcher;
import org.eclipse.aether.internal.impl.DefaultRepositoryLayoutProvider;
import org.eclipse.aether.internal.impl.DefaultRepositorySystem;
import org.eclipse.aether.internal.impl.DefaultSyncContextFactory;
import org.eclipse.aether.internal.impl.DefaultTransporterProvider;
import org.eclipse.aether.internal.impl.DefaultUpdateCheckManager;
import org.eclipse.aether.internal.impl.DefaultUpdatePolicyAnalyzer;
import org.eclipse.aether.internal.impl.EnhancedLocalRepositoryManagerFactory;
import org.eclipse.aether.internal.impl.Maven2RepositoryLayoutFactory;
import org.eclipse.aether.internal.impl.SimpleLocalRepositoryManagerFactory;
import org.eclipse.aether.internal.impl.slf4j.Slf4jLoggerFactory;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.repository.Proxy;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.checksum.ChecksumPolicyProvider;
import org.eclipse.aether.spi.connector.layout.RepositoryLayoutFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.spi.io.FileProcessor;
import org.eclipse.aether.spi.localrepo.LocalRepositoryManagerFactory;
import org.eclipse.aether.spi.log.LoggerFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.repository.DefaultProxySelector;

/**
 * A helper to boot the repository system and a repository system session.
 */
public class Booter
{
    public static RepositorySystem newRepositorySystemOld()
    {
        return ManualRepositorySystemFactory.newRepositorySystem();
    }

    public static RepositorySystem newRepositorySystem()
    {
        //return ManualRepositorySystemFactory.newRepositorySystem();
        // return org.eclipse.aether.examples.guice.GuiceRepositorySystemFactory.newRepositorySystem();
        // return org.eclipse.aether.examples.sisu.SisuRepositorySystemFactory.newRepositorySystem();
        // return org.eclipse.aether.examples.plexus.PlexusRepositorySystemFactory.newRepositorySystem();
        DefaultRepositorySystem repositorySystem = new DefaultRepositorySystem();
        
        // Logger factory
        LoggerFactory loggerFactory = new Slf4jLoggerFactory(); // OK
        repositorySystem.setLoggerFactory(loggerFactory);
        
        // Offline controller
        DefaultOfflineController offlineController = new DefaultOfflineController(); // OK
        offlineController.setLoggerFactory(loggerFactory);

        // Metadata generator factories
        Collection<MetadataGeneratorFactory> metadataGeneratorFactories = new ArrayList<MetadataGeneratorFactory>(); // OK
        metadataGeneratorFactories.add(new SnapshotMetadataGeneratorFactory());
        metadataGeneratorFactories.add(new VersionsMetadataGeneratorFactory());
        
        // Enhanced Local Repository Manager Factory
        EnhancedLocalRepositoryManagerFactory enhancedLocalRepositoryManagerFactory = new EnhancedLocalRepositoryManagerFactory(); // OK
        enhancedLocalRepositoryManagerFactory.setLoggerFactory(loggerFactory);
        
        // Simple Local Repository Manager Factory
        SimpleLocalRepositoryManagerFactory simpleLocalRepositoryManagerFactory = new SimpleLocalRepositoryManagerFactory(); // OK
        simpleLocalRepositoryManagerFactory.setLoggerFactory(loggerFactory);
        
        // Local repository manager factories
        List<LocalRepositoryManagerFactory> localRepositoryManagerFactories = new ArrayList<LocalRepositoryManagerFactory>(); // OK
        localRepositoryManagerFactories.add(enhancedLocalRepositoryManagerFactory);
        localRepositoryManagerFactories.add(simpleLocalRepositoryManagerFactory);
        
        // Repository Event Dispatcher
        DefaultRepositoryEventDispatcher repositoryEventDispatcher = new DefaultRepositoryEventDispatcher(); // OK
        repositoryEventDispatcher.setLoggerFactory(loggerFactory);

        // File processor
        DefaultFileProcessor fileProcessor = new DefaultFileProcessor(); // OK
        
        // File transporter factory
        FileTransporterFactory fileTransporterFactory = new FileTransporterFactory(); // OK
        fileTransporterFactory.setLoggerFactory(loggerFactory);
        
        // Http transporter factory
        HttpTransporterFactory httpTransporterFactory = new HttpTransporterFactory(); // OK
        httpTransporterFactory.setLoggerFactory(loggerFactory);
        
        // Transporter factories
        Collection<TransporterFactory> transporterFactories = new ArrayList<TransporterFactory>(); // OK
        transporterFactories.add(fileTransporterFactory);
        transporterFactories.add(httpTransporterFactory);
        
        // Transporter provider
        DefaultTransporterProvider defaultTransporterProvider = new DefaultTransporterProvider(); // OK
        defaultTransporterProvider.setLoggerFactory(loggerFactory);
        defaultTransporterProvider.setTransporterFactories(transporterFactories);
        
        // Repository layout factories
        Collection<RepositoryLayoutFactory> repositoryLayoutFactories = new ArrayList<RepositoryLayoutFactory>(); // OK
        repositoryLayoutFactories.add(new Maven2RepositoryLayoutFactory());
        
        // Repository Layout Provider
        DefaultRepositoryLayoutProvider repositoryLayoutProvider = new DefaultRepositoryLayoutProvider(); // OK
        repositoryLayoutProvider.setLoggerFactory(loggerFactory);
        repositoryLayoutProvider.setRepositoryLayoutFactories(repositoryLayoutFactories);
        
        // Checksum Policy Provider
        DefaultChecksumPolicyProvider checksumPolicyProvider = new DefaultChecksumPolicyProvider(); // OK
        checksumPolicyProvider.setLoggerFactory(loggerFactory);
                
        // Repository connector factory
        BasicRepositoryConnectorFactory repositoryConnectorFactory = new BasicRepositoryConnectorFactory(); // OK
        repositoryConnectorFactory.setLoggerFactory(loggerFactory);
        repositoryConnectorFactory.setTransporterProvider(defaultTransporterProvider);
        repositoryConnectorFactory.setRepositoryLayoutProvider(repositoryLayoutProvider);
        repositoryConnectorFactory.setChecksumPolicyProvider(checksumPolicyProvider);
        repositoryConnectorFactory.setFileProcessor(fileProcessor);
        
        // Repository Connector Factories
        Collection<RepositoryConnectorFactory> repositoryConnectorFactories = new ArrayList<RepositoryConnectorFactory>(); // OK
        repositoryConnectorFactories.add(repositoryConnectorFactory);
                
        // Repository Connector Provider
        DefaultRepositoryConnectorProvider repositoryConnectorProvider = new DefaultRepositoryConnectorProvider(); // OK
        repositoryConnectorProvider.setLoggerFactory(loggerFactory);
        repositoryConnectorProvider.setRepositoryConnectorFactories(repositoryConnectorFactories);
        
        // Sync context factory
        DefaultSyncContextFactory syncContextFactory = new DefaultSyncContextFactory(); // OK
        repositorySystem.setSyncContextFactory(syncContextFactory);
        
        // Update policy analizer
        DefaultUpdatePolicyAnalyzer updatePolicyAnalyzer = new DefaultUpdatePolicyAnalyzer(); // OK
        updatePolicyAnalyzer.setLoggerFactory(loggerFactory);
        
        // Update check manager
        DefaultUpdateCheckManager updateCheckManager = new DefaultUpdateCheckManager(); // OK
        updateCheckManager.setLoggerFactory(loggerFactory);
        updateCheckManager.setUpdatePolicyAnalyzer(updatePolicyAnalyzer);
        
        // Local repository provider
        DefaultLocalRepositoryProvider localRepositoryProvider = new DefaultLocalRepositoryProvider(); // OK
        localRepositoryProvider.setLocalRepositoryManagerFactories(localRepositoryManagerFactories);
        localRepositoryProvider.setLoggerFactory(loggerFactory);
        repositorySystem.setLocalRepositoryProvider(localRepositoryProvider);
        
        // Remote repository manager
        DefaultRemoteRepositoryManager remoteRepositoryManager = new DefaultRemoteRepositoryManager(); // OK
        remoteRepositoryManager.setLoggerFactory(loggerFactory);
        remoteRepositoryManager.setUpdatePolicyAnalyzer(updatePolicyAnalyzer);
        remoteRepositoryManager.setChecksumPolicyProvider(checksumPolicyProvider);
        repositorySystem.setRemoteRepositoryManager(remoteRepositoryManager);

        // Metadata resolver
        DefaultMetadataResolver metadataResolver = new DefaultMetadataResolver(); // OK
        metadataResolver.setLoggerFactory(loggerFactory);
        metadataResolver.setRepositoryEventDispatcher(repositoryEventDispatcher);
        metadataResolver.setUpdateCheckManager(updateCheckManager);
        metadataResolver.setRepositoryConnectorProvider(repositoryConnectorProvider);
        metadataResolver.setRemoteRepositoryManager(remoteRepositoryManager);
        metadataResolver.setSyncContextFactory(syncContextFactory);
        metadataResolver.setOfflineController(offlineController);
        repositorySystem.setMetadataResolver(metadataResolver);
        
        // Version resolver
        DefaultVersionResolver versionResolver = new DefaultVersionResolver(); // OK
        versionResolver.setLoggerFactory(loggerFactory);
        versionResolver.setMetadataResolver(metadataResolver);
        versionResolver.setSyncContextFactory(syncContextFactory);
        versionResolver.setRepositoryEventDispatcher(repositoryEventDispatcher);
        repositorySystem.setVersionResolver(versionResolver);

        // Version range resolver
        DefaultVersionRangeResolver versionRangeResolver = new DefaultVersionRangeResolver(); // OK
        versionRangeResolver.setLoggerFactory(loggerFactory);
        versionRangeResolver.setMetadataResolver(metadataResolver);
        versionRangeResolver.setSyncContextFactory(syncContextFactory);
        versionRangeResolver.setRepositoryEventDispatcher(repositoryEventDispatcher);
        repositorySystem.setVersionRangeResolver(versionRangeResolver);
        
        // Artifact resolver
        DefaultArtifactResolver artifactResolver = new DefaultArtifactResolver(); // OK
        artifactResolver.setLoggerFactory(loggerFactory);
        artifactResolver.setFileProcessor(fileProcessor);
        artifactResolver.setRepositoryEventDispatcher(repositoryEventDispatcher);
        artifactResolver.setVersionResolver(versionResolver);
        artifactResolver.setUpdateCheckManager(updateCheckManager);
        artifactResolver.setRepositoryConnectorProvider(repositoryConnectorProvider);
        artifactResolver.setRemoteRepositoryManager(remoteRepositoryManager);
        artifactResolver.setSyncContextFactory(syncContextFactory);
        artifactResolver.setOfflineController(offlineController);
        repositorySystem.setArtifactResolver(artifactResolver);

        // Artifact descriptor reader
        DefaultArtifactDescriptorReader artifactDescriptorReader = new DefaultArtifactDescriptorReader(); // OK
        artifactDescriptorReader.setLoggerFactory(loggerFactory);
        artifactDescriptorReader.setRemoteRepositoryManager(remoteRepositoryManager);
        artifactDescriptorReader.setVersionResolver(versionResolver);
        artifactDescriptorReader.setVersionRangeResolver(versionRangeResolver);
        artifactDescriptorReader.setArtifactResolver(artifactResolver);
        artifactDescriptorReader.setRepositoryEventDispatcher(repositoryEventDispatcher);
        artifactDescriptorReader.setModelBuilder(new DefaultModelBuilderFactory().newInstance());
        repositorySystem.setArtifactDescriptorReader(artifactDescriptorReader);

        // Dependency collector
        DefaultDependencyCollector dependencyCollector = new DefaultDependencyCollector(); // OK
        dependencyCollector.setLoggerFactory(loggerFactory);
        dependencyCollector.setRemoteRepositoryManager(remoteRepositoryManager);
        dependencyCollector.setArtifactDescriptorReader(artifactDescriptorReader);
        dependencyCollector.setVersionRangeResolver(versionRangeResolver);
        repositorySystem.setDependencyCollector(dependencyCollector);
        
        // Installer
        DefaultInstaller installer = new DefaultInstaller(); // OK
        installer.setLoggerFactory(loggerFactory);
        installer.setFileProcessor(fileProcessor);
        installer.setRepositoryEventDispatcher(repositoryEventDispatcher);
        installer.setMetadataGeneratorFactories(metadataGeneratorFactories);
        installer.setSyncContextFactory(syncContextFactory);
        repositorySystem.setInstaller(installer);
        
        // Deployer
        DefaultDeployer deployer = new DefaultDeployer(); // OK
        deployer.setLoggerFactory(loggerFactory);
        deployer.setFileProcessor(fileProcessor);
        deployer.setRepositoryEventDispatcher(repositoryEventDispatcher);
        deployer.setRepositoryConnectorProvider(repositoryConnectorProvider);
        deployer.setRemoteRepositoryManager(remoteRepositoryManager);
        deployer.setUpdateCheckManager(updateCheckManager);
        deployer.setMetadataGeneratorFactories(metadataGeneratorFactories);
        deployer.setSyncContextFactory(syncContextFactory);
        deployer.setOfflineController(offlineController);
        repositorySystem.setDeployer(deployer);
        
        return repositorySystem;
    }

    public static DefaultRepositorySystemSession newRepositorySystemSession(
            RepositorySystem system, Map<Proxy, String> proxies)
    {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        LocalRepository localRepo = new LocalRepository( "target/local-repo" );
        LocalRepositoryManager localRepositoryManager = system.newLocalRepositoryManager(session, localRepo);
        session.setLocalRepositoryManager(localRepositoryManager);

        session.setTransferListener( new ConsoleTransferListener() );
        session.setRepositoryListener( new ConsoleRepositoryListener() );

        // uncomment to generate dirty trees
        // session.setDependencyGraphTransformer( null );
        
        DefaultProxySelector proxySelector = new DefaultProxySelector();
        for (Entry<Proxy, String> proxyEntry : proxies.entrySet())
        {
            proxySelector.add(proxyEntry.getKey(), proxyEntry.getValue());
            
        }
        session.setProxySelector(proxySelector);

        return session;
    }

    public static List<RemoteRepository> newRepositories(Proxy proxy)
    {
        return new ArrayList<RemoteRepository>(Arrays.asList(
                newLocalRepository(), newEtkcRepository(proxy), newCentralRepository(proxy)));
    }

    public static RemoteRepository newCentralRepository(Proxy proxy)
    {
        RemoteRepository.Builder repositoryBuilder = new RemoteRepository
                .Builder("central", "default", "http://central.maven.org/maven2/");
        if (proxy != null)
        {
            repositoryBuilder.setProxy(proxy);
        }
        
        return repositoryBuilder.build();
    }

    public static RemoteRepository newEtkcRepository(Proxy proxy)
    {
        RemoteRepository.Builder repositoryBuilder = new RemoteRepository
                .Builder("etkc", "default", "http://ehrzgux504.etk.extern.eu.ericsson.se/content/groups/parent/");
        if (proxy != null)
        {
            repositoryBuilder.setProxy(proxy);
        }
        
        return repositoryBuilder.build();
    }

    public static RemoteRepository newLocalRepository()
    {
        return new RemoteRepository
                .Builder("local", "default", getLocalRepoLocation().toExternalForm())
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
