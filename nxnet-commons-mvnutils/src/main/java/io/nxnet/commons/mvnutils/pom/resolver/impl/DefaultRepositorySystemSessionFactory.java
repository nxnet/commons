package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.Properties;

import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifactType;
import org.eclipse.aether.collection.DependencyGraphTransformer;
import org.eclipse.aether.collection.DependencyManager;
import org.eclipse.aether.collection.DependencySelector;
import org.eclipse.aether.collection.DependencyTraverser;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.util.artifact.DefaultArtifactTypeRegistry;
import org.eclipse.aether.util.graph.manager.ClassicDependencyManager;
import org.eclipse.aether.util.graph.selector.AndDependencySelector;
import org.eclipse.aether.util.graph.selector.ExclusionDependencySelector;
import org.eclipse.aether.util.graph.selector.OptionalDependencySelector;
import org.eclipse.aether.util.graph.selector.ScopeDependencySelector;
import org.eclipse.aether.util.graph.transformer.ChainedDependencyGraphTransformer;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;
import org.eclipse.aether.util.graph.transformer.JavaDependencyContextRefiner;
import org.eclipse.aether.util.graph.transformer.JavaScopeDeriver;
import org.eclipse.aether.util.graph.transformer.JavaScopeSelector;
import org.eclipse.aether.util.graph.transformer.NearestVersionSelector;
import org.eclipse.aether.util.graph.transformer.SimpleOptionalitySelector;
import org.eclipse.aether.util.graph.traverser.FatArtifactTraverser;
import org.eclipse.aether.util.repository.DefaultProxySelector;
import org.eclipse.aether.util.repository.SimpleArtifactDescriptorPolicy;

import io.nxnet.commons.mvnutils.pom.resolver.LocalRepositoryFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinition;
import io.nxnet.commons.mvnutils.pom.resolver.ProxyDefinitionFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RepositorySystemFactory;
import io.nxnet.commons.mvnutils.pom.resolver.RepositorySystemSessionFactory;
import io.nxnet.commons.mvnutils.pom.resolver.ServiceRegistry;

public class DefaultRepositorySystemSessionFactory implements RepositorySystemSessionFactory
{
    private RepositorySystemFactory repositorySystemFactory;
    
    private ProxyDefinitionFactory proxyDefinitionFactory;
    
    private LocalRepositoryFactory localRepositoryFactory;
    
    public DefaultRepositorySystemSessionFactory()
    {
        this.repositorySystemFactory = new DefaultRepositorySystemFactory();
        this.proxyDefinitionFactory = new LocalhostProxyDefinitionFactory();
        this.localRepositoryFactory = new DefaultLocalRepositoryFactory();
    }

    public void init(ServiceRegistry serviceLocator)
    {
        this.repositorySystemFactory = serviceLocator.getService(RepositorySystemFactory.class);
        this.proxyDefinitionFactory = serviceLocator.getService(ProxyDefinitionFactory.class);
        this.localRepositoryFactory = serviceLocator.getService(LocalRepositoryFactory.class);
    }

    public RepositorySystemSession getRepositorySystemSession()
    {
        DefaultRepositorySystemSession session = new DefaultRepositorySystemSession();

        DependencyTraverser depTraverser = new FatArtifactTraverser();
        session.setDependencyTraverser( depTraverser );

        DependencyManager depManager = new ClassicDependencyManager();
        session.setDependencyManager( depManager );

        DependencySelector depFilter =
            new AndDependencySelector( new ScopeDependencySelector( "test", "provided" ),
                                       new OptionalDependencySelector(), new ExclusionDependencySelector() );
        session.setDependencySelector( depFilter );

        DependencyGraphTransformer transformer =
            new ConflictResolver( new NearestVersionSelector(), new JavaScopeSelector(),
                                  new SimpleOptionalitySelector(), new JavaScopeDeriver() );
        new ChainedDependencyGraphTransformer( transformer, new JavaDependencyContextRefiner() );
        session.setDependencyGraphTransformer( transformer );

        DefaultArtifactTypeRegistry stereotypes = new DefaultArtifactTypeRegistry();
        stereotypes.add( new DefaultArtifactType( "pom" ) );
        stereotypes.add( new DefaultArtifactType( "maven-plugin", "jar", "", "java" ) );
        stereotypes.add( new DefaultArtifactType( "jar", "jar", "", "java" ) );
        stereotypes.add( new DefaultArtifactType( "ejb", "jar", "", "java" ) );
        stereotypes.add( new DefaultArtifactType( "ejb-client", "jar", "client", "java" ) );
        stereotypes.add( new DefaultArtifactType( "test-jar", "jar", "tests", "java" ) );
        stereotypes.add( new DefaultArtifactType( "javadoc", "jar", "javadoc", "java" ) );
        stereotypes.add( new DefaultArtifactType( "java-source", "jar", "sources", "java", false, false ) );
        stereotypes.add( new DefaultArtifactType( "war", "war", "", "java", false, true ) );
        stereotypes.add( new DefaultArtifactType( "ear", "ear", "", "java", false, true ) );
        stereotypes.add( new DefaultArtifactType( "rar", "rar", "", "java", false, true ) );
        stereotypes.add( new DefaultArtifactType( "par", "par", "", "java", false, true ) );
        session.setArtifactTypeRegistry( stereotypes );

        session.setArtifactDescriptorPolicy( new SimpleArtifactDescriptorPolicy( true, true ) );

        // MNG-5670 guard against ConcurrentModificationException
        Properties sysProps = new Properties();
        for ( String key : System.getProperties().stringPropertyNames() )
        {
            sysProps.put( key, System.getProperty( key ) );
        }
        session.setSystemProperties( sysProps );
        session.setConfigProperties( sysProps );

        session.setTransferListener(new ConsoleTransferListener());
        session.setRepositoryListener(new ConsoleRepositoryListener());

        // Local repository manager
        RepositorySystem repositorySystem = this.repositorySystemFactory.getRepositorySystem();
        LocalRepository localRepository = this.localRepositoryFactory.getLocalRepository();
        LocalRepositoryManager localRepositoryManager = repositorySystem
                .newLocalRepositoryManager(session, localRepository);
        session.setLocalRepositoryManager(localRepositoryManager);

        // uncomment to generate dirty trees
        // session.setDependencyGraphTransformer( null );
        
        // Proxy selector
        ProxyDefinition proxyDefinition = this.proxyDefinitionFactory.getProxyDefinition();
        if (proxyDefinition != null)
        {
            DefaultProxySelector proxySelector = new DefaultProxySelector();
            proxySelector.add(proxyDefinition.getProxy(), proxyDefinition.getNonProxyHosts());
            session.setProxySelector(proxySelector);
        }

        return session;
    }

    /**
     * @return the repositorySystemFactory
     */
    public RepositorySystemFactory getRepositorySystemFactory()
    {
        return repositorySystemFactory;
    }

    /**
     * @param repositorySystemFactory the repositorySystemFactory to set
     */
    public void setRepositorySystemFactory(RepositorySystemFactory repositorySystemFactory)
    {
        this.repositorySystemFactory = repositorySystemFactory;
    }

    /**
     * @return the proxyDefinitionFactory
     */
    public ProxyDefinitionFactory getProxyDefinitionFactory()
    {
        return proxyDefinitionFactory;
    }

    /**
     * @param proxyDefinitionFactory the proxyDefinitionFactory to set
     */
    public void setProxyDefinitionFactory(ProxyDefinitionFactory proxyDefinitionFactory)
    {
        this.proxyDefinitionFactory = proxyDefinitionFactory;
    }

    /**
     * @return the localRepositoryFactory
     */
    public LocalRepositoryFactory getLocalRepositoryFactory()
    {
        return localRepositoryFactory;
    }

    /**
     * @param localRepositoryFactory the localRepositoryFactory to set
     */
    public void setLocalRepositoryFactory(LocalRepositoryFactory localRepositoryFactory)
    {
        this.localRepositoryFactory = localRepositoryFactory;
    }

}
