package io.nxnet.commons.mvnutils.pom.resolver.impl;

import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.InputLocation;

public class DependencyWrapper extends Dependency
{
    private static final long serialVersionUID = -6462621529918590506L;
    
    private Dependency delegee;

    public DependencyWrapper(Dependency delegee)
    {
        this.delegee = delegee;
    }

    /**
     * @return
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return delegee.hashCode();
    }

    /**
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        return delegee.equals(obj);
    }

    /**
     * @param exclusion
     * @see org.apache.maven.model.Dependency#addExclusion(org.apache.maven.model.Exclusion)
     */
    public void addExclusion(Exclusion exclusion)
    {
        delegee.addExclusion(exclusion);
    }

    /**
     * @return
     * @see org.apache.maven.model.Dependency#clone()
     */
    public Dependency clone()
    {
        return delegee.clone();
    }

    /**
     * @return
     * @see org.apache.maven.model.Dependency#getArtifactId()
     */
    public String getArtifactId()
    {
        return delegee.getArtifactId();
    }

    /**
     * @return
     * @see org.apache.maven.model.Dependency#getClassifier()
     */
    public String getClassifier()
    {
        return delegee.getClassifier();
    }

    /**
     * @return
     * @see org.apache.maven.model.Dependency#getExclusions()
     */
    public List<Exclusion> getExclusions()
    {
        return delegee.getExclusions();
    }

    /**
     * @return
     * @see org.apache.maven.model.Dependency#getGroupId()
     */
    public String getGroupId()
    {
        return delegee.getGroupId();
    }

    /**
     * @param key
     * @return
     * @see org.apache.maven.model.Dependency#getLocation(java.lang.Object)
     */
    public InputLocation getLocation(Object key)
    {
        return delegee.getLocation(key);
    }

    /**
     * @return
     * @see org.apache.maven.model.Dependency#getOptional()
     */
    public String getOptional()
    {
        return delegee.getOptional();
    }

    /**
     * @return
     * @see org.apache.maven.model.Dependency#getScope()
     */
    public String getScope()
    {
        return delegee.getScope();
    }

    /**
     * @return
     * @see org.apache.maven.model.Dependency#getSystemPath()
     */
    public String getSystemPath()
    {
        return delegee.getSystemPath();
    }

    /**
     * @return
     * @see org.apache.maven.model.Dependency#getType()
     */
    public String getType()
    {
        return delegee.getType();
    }

    /**
     * @return
     * @see org.apache.maven.model.Dependency#getVersion()
     */
    public String getVersion()
    {
        return delegee.getVersion();
    }

    /**
     * @param exclusion
     * @see org.apache.maven.model.Dependency#removeExclusion(org.apache.maven.model.Exclusion)
     */
    public void removeExclusion(Exclusion exclusion)
    {
        delegee.removeExclusion(exclusion);
    }

    /**
     * @param artifactId
     * @see org.apache.maven.model.Dependency#setArtifactId(java.lang.String)
     */
    public void setArtifactId(String artifactId)
    {
        delegee.setArtifactId(artifactId);
    }

    /**
     * @param classifier
     * @see org.apache.maven.model.Dependency#setClassifier(java.lang.String)
     */
    public void setClassifier(String classifier)
    {
        delegee.setClassifier(classifier);
    }

    /**
     * @param exclusions
     * @see org.apache.maven.model.Dependency#setExclusions(java.util.List)
     */
    public void setExclusions(List<Exclusion> exclusions)
    {
        delegee.setExclusions(exclusions);
    }

    /**
     * @param groupId
     * @see org.apache.maven.model.Dependency#setGroupId(java.lang.String)
     */
    public void setGroupId(String groupId)
    {
        delegee.setGroupId(groupId);
    }

    /**
     * @param key
     * @param location
     * @see org.apache.maven.model.Dependency#setLocation(java.lang.Object, org.apache.maven.model.InputLocation)
     */
    public void setLocation(Object key, InputLocation location)
    {
        delegee.setLocation(key, location);
    }

    /**
     * @param optional
     * @see org.apache.maven.model.Dependency#setOptional(java.lang.String)
     */
    public void setOptional(String optional)
    {
        delegee.setOptional(optional);
    }

    /**
     * @param scope
     * @see org.apache.maven.model.Dependency#setScope(java.lang.String)
     */
    public void setScope(String scope)
    {
        delegee.setScope(scope);
    }

    /**
     * @param systemPath
     * @see org.apache.maven.model.Dependency#setSystemPath(java.lang.String)
     */
    public void setSystemPath(String systemPath)
    {
        delegee.setSystemPath(systemPath);
    }

    /**
     * @param type
     * @see org.apache.maven.model.Dependency#setType(java.lang.String)
     */
    public void setType(String type)
    {
        delegee.setType(type);
    }

    /**
     * @param version
     * @see org.apache.maven.model.Dependency#setVersion(java.lang.String)
     */
    public void setVersion(String version)
    {
        delegee.setVersion(version);
    }

    /**
     * @return
     * @see org.apache.maven.model.Dependency#isOptional()
     */
    public boolean isOptional()
    {
        return delegee.isOptional();
    }

    /**
     * @param optional
     * @see org.apache.maven.model.Dependency#setOptional(boolean)
     */
    public void setOptional(boolean optional)
    {
        delegee.setOptional(optional);
    }

    /**
     * @return
     * @see org.apache.maven.model.Dependency#toString()
     */
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder(this.getGroupId());
        if (this.getArtifactId() != null && !this.getArtifactId().isEmpty()) 
            stringBuilder.append(":").append(this.getArtifactId());
        if (this.getType() != null && !this.getType().isEmpty())
            stringBuilder.append(":").append(this.getType());
        if (this.getClassifier() != null && !this.getClassifier().isEmpty())
            stringBuilder.append(":").append(this.getClassifier());
        if (this.getVersion() != null && !this.getVersion().isEmpty())
            stringBuilder.append(":").append(this.getVersion());
        if (this.getScope() != null && !this.getScope().isEmpty())
            stringBuilder.append(":").append(this.getScope());
        return stringBuilder.toString();
    }

    /**
     * @return
     * @see org.apache.maven.model.Dependency#getManagementKey()
     */
    public String getManagementKey()
    {
        return delegee.getManagementKey();
    }

}
