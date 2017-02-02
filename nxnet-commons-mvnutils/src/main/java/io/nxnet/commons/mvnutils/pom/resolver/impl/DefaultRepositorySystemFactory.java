package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.repository.internal.DefaultArtifactDescriptorReader;
import org.apache.maven.repository.internal.DefaultVersionRangeResolver;
import org.apache.maven.repository.internal.DefaultVersionResolver;
import org.apache.maven.repository.internal.SnapshotMetadataGeneratorFactory;
import org.apache.maven.repository.internal.VersionsMetadataGeneratorFactory;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.MetadataGeneratorFactory;
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
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.layout.RepositoryLayoutFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.spi.localrepo.LocalRepositoryManagerFactory;
import org.eclipse.aether.spi.log.LoggerFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;

import io.nxnet.commons.mvnutils.pom.resolver.RemoteRepositoryManagerFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RepositorySystemFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceLocator;

public class DefaultRepositorySystemFactory implements RepositorySystemFactory
{
    private RepositorySystem instance = null;
    
    private RemoteRepositoryManagerFactory remoteRepositoryManagerFactory;
    
    public DefaultRepositorySystemFactory()
    {
        this.remoteRepositoryManagerFactory = ServiceLocator.getInstance().
                getService(RemoteRepositoryManagerFactory.class);
    }
    
    public RepositorySystem getRepositorySystem()
    {
        if (this.instance == null)
        {
            this.instance = initRepositorySystem();
        }
        
        return this.instance;
    }

    private RepositorySystem initRepositorySystem()
    {
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
        DefaultRemoteRepositoryManager remoteRepositoryManager = (DefaultRemoteRepositoryManager)this
                .remoteRepositoryManagerFactory.getRemoteRepositoryManager(); // OK
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
}
