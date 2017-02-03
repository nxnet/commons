package io.nxnet.commons.mvnutils.pom.resolver;

import org.eclipse.aether.repository.Proxy;

public class ProxyDefinition
{
    private Proxy proxy;
    
    private String nonProxyHosts;

    public ProxyDefinition() {}

    public ProxyDefinition(String type, String host, int port, String nonProxyHosts)
    {
        this.proxy = new Proxy(type, host, port);
        this.nonProxyHosts = nonProxyHosts;
    }

    /**
     * @return the proxy
     */
    public Proxy getProxy()
    {
        return proxy;
    }

    /**
     * @param proxy the proxy to set
     */
    public void setProxy(Proxy proxy)
    {
        this.proxy = proxy;
    }

    /**
     * @return the nonProxyHosts
     */
    public String getNonProxyHosts()
    {
        return nonProxyHosts;
    }

    /**
     * @param nonProxyHosts the nonProxyHosts to set
     */
    public void setNonProxyHosts(String nonProxyHosts)
    {
        this.nonProxyHosts = nonProxyHosts;
    }

}
