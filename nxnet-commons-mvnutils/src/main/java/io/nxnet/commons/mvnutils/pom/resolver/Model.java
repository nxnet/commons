package io.nxnet.commons.mvnutils.pom.resolver;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.apache.maven.model.Build;
import org.apache.maven.model.CiManagement;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Developer;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.InputLocation;
import org.apache.maven.model.IssueManagement;
import org.apache.maven.model.License;
import org.apache.maven.model.MailingList;
import org.apache.maven.model.Organization;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Prerequisites;
import org.apache.maven.model.Profile;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Repository;
import org.apache.maven.model.Scm;

public class Model extends org.apache.maven.model.Model
{
    private static final long serialVersionUID = 904665542754300058L;
    
    private org.apache.maven.model.Model delegee;
    
    public Model(org.apache.maven.model.Model model)
    {
        this.delegee = model;
    }
    
    public TreeNode<Dependency> getDependencyTree() {
        return null;
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
     * @param dependency
     * @see org.apache.maven.model.ModelBase#addDependency(org.apache.maven.model.Dependency)
     */
    public void addDependency(Dependency dependency)
    {
        delegee.addDependency(dependency);
    }

    /**
     * @param string
     * @see org.apache.maven.model.ModelBase#addModule(java.lang.String)
     */
    public void addModule(String string)
    {
        delegee.addModule(string);
    }

    /**
     * @param repository
     * @see org.apache.maven.model.ModelBase#addPluginRepository(org.apache.maven.model.Repository)
     */
    public void addPluginRepository(Repository repository)
    {
        delegee.addPluginRepository(repository);
    }

    /**
     * @param key
     * @param value
     * @see org.apache.maven.model.ModelBase#addProperty(java.lang.String, java.lang.String)
     */
    public void addProperty(String key, String value)
    {
        delegee.addProperty(key, value);
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
     * @param repository
     * @see org.apache.maven.model.ModelBase#addRepository(org.apache.maven.model.Repository)
     */
    public void addRepository(Repository repository)
    {
        delegee.addRepository(repository);
    }

    /**
     * @param contributor
     * @see org.apache.maven.model.Model#addContributor(org.apache.maven.model.Contributor)
     */
    public void addContributor(Contributor contributor)
    {
        delegee.addContributor(contributor);
    }

    /**
     * @param developer
     * @see org.apache.maven.model.Model#addDeveloper(org.apache.maven.model.Developer)
     */
    public void addDeveloper(Developer developer)
    {
        delegee.addDeveloper(developer);
    }

    /**
     * @param license
     * @see org.apache.maven.model.Model#addLicense(org.apache.maven.model.License)
     */
    public void addLicense(License license)
    {
        delegee.addLicense(license);
    }

    /**
     * @param mailingList
     * @see org.apache.maven.model.Model#addMailingList(org.apache.maven.model.MailingList)
     */
    public void addMailingList(MailingList mailingList)
    {
        delegee.addMailingList(mailingList);
    }

    /**
     * @param profile
     * @see org.apache.maven.model.Model#addProfile(org.apache.maven.model.Profile)
     */
    public void addProfile(Profile profile)
    {
        delegee.addProfile(profile);
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#clone()
     */
    public org.apache.maven.model.Model clone()
    {
        return delegee.clone();
    }

    /**
     * @return
     * @see org.apache.maven.model.ModelBase#getDependencies()
     */
    public List<Dependency> getDependencies()
    {
        return delegee.getDependencies();
    }

    /**
     * @return
     * @see org.apache.maven.model.ModelBase#getDependencyManagement()
     */
    public DependencyManagement getDependencyManagement()
    {
        return delegee.getDependencyManagement();
    }

    /**
     * @return
     * @see org.apache.maven.model.ModelBase#getDistributionManagement()
     */
    public DistributionManagement getDistributionManagement()
    {
        return delegee.getDistributionManagement();
    }

    /**
     * @param key
     * @return
     * @see org.apache.maven.model.ModelBase#getLocation(java.lang.Object)
     */
    public InputLocation getLocation(Object key)
    {
        return delegee.getLocation(key);
    }

    /**
     * @return
     * @see org.apache.maven.model.ModelBase#getModules()
     */
    public List<String> getModules()
    {
        return delegee.getModules();
    }

    /**
     * @return
     * @see org.apache.maven.model.ModelBase#getPluginRepositories()
     */
    public List<Repository> getPluginRepositories()
    {
        return delegee.getPluginRepositories();
    }

    /**
     * @return
     * @see org.apache.maven.model.ModelBase#getProperties()
     */
    public Properties getProperties()
    {
        return delegee.getProperties();
    }

    /**
     * @return
     * @see org.apache.maven.model.ModelBase#getReporting()
     */
    public Reporting getReporting()
    {
        return delegee.getReporting();
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getArtifactId()
     */
    public String getArtifactId()
    {
        return delegee.getArtifactId();
    }

    /**
     * @return
     * @see org.apache.maven.model.ModelBase#getReports()
     */
    public Object getReports()
    {
        return delegee.getReports();
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getBuild()
     */
    public Build getBuild()
    {
        return delegee.getBuild();
    }

    /**
     * @return
     * @see org.apache.maven.model.ModelBase#getRepositories()
     */
    public List<Repository> getRepositories()
    {
        return delegee.getRepositories();
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getCiManagement()
     */
    public CiManagement getCiManagement()
    {
        return delegee.getCiManagement();
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getContributors()
     */
    public List<Contributor> getContributors()
    {
        return delegee.getContributors();
    }

    /**
     * @param dependency
     * @see org.apache.maven.model.ModelBase#removeDependency(org.apache.maven.model.Dependency)
     */
    public void removeDependency(Dependency dependency)
    {
        delegee.removeDependency(dependency);
    }

    /**
     * @param string
     * @see org.apache.maven.model.ModelBase#removeModule(java.lang.String)
     */
    public void removeModule(String string)
    {
        delegee.removeModule(string);
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getDescription()
     */
    public String getDescription()
    {
        return delegee.getDescription();
    }

    /**
     * @param repository
     * @see org.apache.maven.model.ModelBase#removePluginRepository(org.apache.maven.model.Repository)
     */
    public void removePluginRepository(Repository repository)
    {
        delegee.removePluginRepository(repository);
    }

    /**
     * @param repository
     * @see org.apache.maven.model.ModelBase#removeRepository(org.apache.maven.model.Repository)
     */
    public void removeRepository(Repository repository)
    {
        delegee.removeRepository(repository);
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getDevelopers()
     */
    public List<Developer> getDevelopers()
    {
        return delegee.getDevelopers();
    }

    /**
     * @param dependencies
     * @see org.apache.maven.model.ModelBase#setDependencies(java.util.List)
     */
    public void setDependencies(List<Dependency> dependencies)
    {
        delegee.setDependencies(dependencies);
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getGroupId()
     */
    public String getGroupId()
    {
        return delegee.getGroupId();
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getInceptionYear()
     */
    public String getInceptionYear()
    {
        return delegee.getInceptionYear();
    }

    /**
     * @param dependencyManagement
     * @see org.apache.maven.model.ModelBase#setDependencyManagement(org.apache.maven.model.DependencyManagement)
     */
    public void setDependencyManagement(DependencyManagement dependencyManagement)
    {
        delegee.setDependencyManagement(dependencyManagement);
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getIssueManagement()
     */
    public IssueManagement getIssueManagement()
    {
        return delegee.getIssueManagement();
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getLicenses()
     */
    public List<License> getLicenses()
    {
        return delegee.getLicenses();
    }

    /**
     * @param distributionManagement
     * @see org.apache.maven.model.ModelBase#setDistributionManagement(org.apache.maven.model.DistributionManagement)
     */
    public void setDistributionManagement(DistributionManagement distributionManagement)
    {
        delegee.setDistributionManagement(distributionManagement);
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getMailingLists()
     */
    public List<MailingList> getMailingLists()
    {
        return delegee.getMailingLists();
    }

    /**
     * @param key
     * @param location
     * @see org.apache.maven.model.ModelBase#setLocation(java.lang.Object, org.apache.maven.model.InputLocation)
     */
    public void setLocation(Object key, InputLocation location)
    {
        delegee.setLocation(key, location);
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getModelEncoding()
     */
    public String getModelEncoding()
    {
        return delegee.getModelEncoding();
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getModelVersion()
     */
    public String getModelVersion()
    {
        return delegee.getModelVersion();
    }

    /**
     * @param modules
     * @see org.apache.maven.model.ModelBase#setModules(java.util.List)
     */
    public void setModules(List<String> modules)
    {
        delegee.setModules(modules);
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getName()
     */
    public String getName()
    {
        return delegee.getName();
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getOrganization()
     */
    public Organization getOrganization()
    {
        return delegee.getOrganization();
    }

    /**
     * @param pluginRepositories
     * @see org.apache.maven.model.ModelBase#setPluginRepositories(java.util.List)
     */
    public void setPluginRepositories(List<Repository> pluginRepositories)
    {
        delegee.setPluginRepositories(pluginRepositories);
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getPackaging()
     */
    public String getPackaging()
    {
        return delegee.getPackaging();
    }

    /**
     * @param properties
     * @see org.apache.maven.model.ModelBase#setProperties(java.util.Properties)
     */
    public void setProperties(Properties properties)
    {
        delegee.setProperties(properties);
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getParent()
     */
    public Parent getParent()
    {
        return delegee.getParent();
    }

    /**
     * @param reporting
     * @see org.apache.maven.model.ModelBase#setReporting(org.apache.maven.model.Reporting)
     */
    public void setReporting(Reporting reporting)
    {
        delegee.setReporting(reporting);
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getPrerequisites()
     */
    public Prerequisites getPrerequisites()
    {
        return delegee.getPrerequisites();
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getProfiles()
     */
    public List<Profile> getProfiles()
    {
        return delegee.getProfiles();
    }

    /**
     * @param reports
     * @see org.apache.maven.model.ModelBase#setReports(java.lang.Object)
     */
    public void setReports(Object reports)
    {
        delegee.setReports(reports);
    }

    /**
     * @param repositories
     * @see org.apache.maven.model.ModelBase#setRepositories(java.util.List)
     */
    public void setRepositories(List<Repository> repositories)
    {
        delegee.setRepositories(repositories);
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getScm()
     */
    public Scm getScm()
    {
        return delegee.getScm();
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getUrl()
     */
    public String getUrl()
    {
        return delegee.getUrl();
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getVersion()
     */
    public String getVersion()
    {
        return delegee.getVersion();
    }

    /**
     * @param contributor
     * @see org.apache.maven.model.Model#removeContributor(org.apache.maven.model.Contributor)
     */
    public void removeContributor(Contributor contributor)
    {
        delegee.removeContributor(contributor);
    }

    /**
     * @param developer
     * @see org.apache.maven.model.Model#removeDeveloper(org.apache.maven.model.Developer)
     */
    public void removeDeveloper(Developer developer)
    {
        delegee.removeDeveloper(developer);
    }

    /**
     * @param license
     * @see org.apache.maven.model.Model#removeLicense(org.apache.maven.model.License)
     */
    public void removeLicense(License license)
    {
        delegee.removeLicense(license);
    }

    /**
     * @param mailingList
     * @see org.apache.maven.model.Model#removeMailingList(org.apache.maven.model.MailingList)
     */
    public void removeMailingList(MailingList mailingList)
    {
        delegee.removeMailingList(mailingList);
    }

    /**
     * @param profile
     * @see org.apache.maven.model.Model#removeProfile(org.apache.maven.model.Profile)
     */
    public void removeProfile(Profile profile)
    {
        delegee.removeProfile(profile);
    }

    /**
     * @param artifactId
     * @see org.apache.maven.model.Model#setArtifactId(java.lang.String)
     */
    public void setArtifactId(String artifactId)
    {
        delegee.setArtifactId(artifactId);
    }

    /**
     * @param build
     * @see org.apache.maven.model.Model#setBuild(org.apache.maven.model.Build)
     */
    public void setBuild(Build build)
    {
        delegee.setBuild(build);
    }

    /**
     * @param ciManagement
     * @see org.apache.maven.model.Model#setCiManagement(org.apache.maven.model.CiManagement)
     */
    public void setCiManagement(CiManagement ciManagement)
    {
        delegee.setCiManagement(ciManagement);
    }

    /**
     * @param contributors
     * @see org.apache.maven.model.Model#setContributors(java.util.List)
     */
    public void setContributors(List<Contributor> contributors)
    {
        delegee.setContributors(contributors);
    }

    /**
     * @param description
     * @see org.apache.maven.model.Model#setDescription(java.lang.String)
     */
    public void setDescription(String description)
    {
        delegee.setDescription(description);
    }

    /**
     * @param developers
     * @see org.apache.maven.model.Model#setDevelopers(java.util.List)
     */
    public void setDevelopers(List<Developer> developers)
    {
        delegee.setDevelopers(developers);
    }

    /**
     * @param groupId
     * @see org.apache.maven.model.Model#setGroupId(java.lang.String)
     */
    public void setGroupId(String groupId)
    {
        delegee.setGroupId(groupId);
    }

    /**
     * @param inceptionYear
     * @see org.apache.maven.model.Model#setInceptionYear(java.lang.String)
     */
    public void setInceptionYear(String inceptionYear)
    {
        delegee.setInceptionYear(inceptionYear);
    }

    /**
     * @param issueManagement
     * @see org.apache.maven.model.Model#setIssueManagement(org.apache.maven.model.IssueManagement)
     */
    public void setIssueManagement(IssueManagement issueManagement)
    {
        delegee.setIssueManagement(issueManagement);
    }

    /**
     * @param licenses
     * @see org.apache.maven.model.Model#setLicenses(java.util.List)
     */
    public void setLicenses(List<License> licenses)
    {
        delegee.setLicenses(licenses);
    }

    /**
     * @param mailingLists
     * @see org.apache.maven.model.Model#setMailingLists(java.util.List)
     */
    public void setMailingLists(List<MailingList> mailingLists)
    {
        delegee.setMailingLists(mailingLists);
    }

    /**
     * @param modelEncoding
     * @see org.apache.maven.model.Model#setModelEncoding(java.lang.String)
     */
    public void setModelEncoding(String modelEncoding)
    {
        delegee.setModelEncoding(modelEncoding);
    }

    /**
     * @param modelVersion
     * @see org.apache.maven.model.Model#setModelVersion(java.lang.String)
     */
    public void setModelVersion(String modelVersion)
    {
        delegee.setModelVersion(modelVersion);
    }

    /**
     * @param name
     * @see org.apache.maven.model.Model#setName(java.lang.String)
     */
    public void setName(String name)
    {
        delegee.setName(name);
    }

    /**
     * @param organization
     * @see org.apache.maven.model.Model#setOrganization(org.apache.maven.model.Organization)
     */
    public void setOrganization(Organization organization)
    {
        delegee.setOrganization(organization);
    }

    /**
     * @param packaging
     * @see org.apache.maven.model.Model#setPackaging(java.lang.String)
     */
    public void setPackaging(String packaging)
    {
        delegee.setPackaging(packaging);
    }

    /**
     * @param parent
     * @see org.apache.maven.model.Model#setParent(org.apache.maven.model.Parent)
     */
    public void setParent(Parent parent)
    {
        delegee.setParent(parent);
    }

    /**
     * @param prerequisites
     * @see org.apache.maven.model.Model#setPrerequisites(org.apache.maven.model.Prerequisites)
     */
    public void setPrerequisites(Prerequisites prerequisites)
    {
        delegee.setPrerequisites(prerequisites);
    }

    /**
     * @param profiles
     * @see org.apache.maven.model.Model#setProfiles(java.util.List)
     */
    public void setProfiles(List<Profile> profiles)
    {
        delegee.setProfiles(profiles);
    }

    /**
     * @param scm
     * @see org.apache.maven.model.Model#setScm(org.apache.maven.model.Scm)
     */
    public void setScm(Scm scm)
    {
        delegee.setScm(scm);
    }

    /**
     * @param url
     * @see org.apache.maven.model.Model#setUrl(java.lang.String)
     */
    public void setUrl(String url)
    {
        delegee.setUrl(url);
    }

    /**
     * @param version
     * @see org.apache.maven.model.Model#setVersion(java.lang.String)
     */
    public void setVersion(String version)
    {
        delegee.setVersion(version);
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getPomFile()
     */
    public File getPomFile()
    {
        return delegee.getPomFile();
    }

    /**
     * @param pomFile
     * @see org.apache.maven.model.Model#setPomFile(java.io.File)
     */
    public void setPomFile(File pomFile)
    {
        delegee.setPomFile(pomFile);
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getProjectDirectory()
     */
    public File getProjectDirectory()
    {
        return delegee.getProjectDirectory();
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#getId()
     */
    public String getId()
    {
        return delegee.getId();
    }

    /**
     * @return
     * @see org.apache.maven.model.Model#toString()
     */
    public String toString()
    {
        return delegee.toString();
    }
    
}
