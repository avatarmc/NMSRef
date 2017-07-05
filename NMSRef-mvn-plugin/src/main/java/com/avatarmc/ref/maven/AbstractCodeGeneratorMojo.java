package com.avatarmc.ref.maven;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.util.graph.visitor.PreorderNodeListGenerator;

public abstract class AbstractCodeGeneratorMojo extends AbstractMojo {

    /**
     * The MavenProject object.
     *
     * @parameter property="project"
     * @readonly
     */
    protected MavenProject project;

    /**
     * The entry point to Aether, i.e. the component doing all the work.
     *
     * @component
     */
    protected RepositorySystem repoSystem;

    /**
     * The current repository/network configuration of Maven.
     *
     * @parameter default-value="${repositorySystemSession}"
     * @readonly
     */
    protected RepositorySystemSession repoSession;

    /**
     * The project's remote repositories to use for the resolution of plugins
     * and their dependencies.
     *
     * @parameter default-value="${project.remotePluginRepositories}"
     * @readonly
     */
    protected List<RemoteRepository> remoteRepos;

    /**
     * @parameter default-value="target/generated-sources/nms-ref"
     * @required
     */
    File outputDirectory;

    /**
     * @parameter default-value="target/mcp-mappings"
     * @required
     */
    File mappingDirectory;

    /**
     * @parameter
     * @required
     */
    URL spigot2srgURL;

    /**
     * @parameter
     * @required
     */
    URL mcpURL;

    /**
     * @parameter
     * @required
     */
    String targetPackage;

    /**
     * @parameter
     * @required
     */
    String mcPackage;

    /**
     * @parameter default-value="ref-excludes.list"
     * @required
     */
    File excludesFile;

    @Override
    public void execute() {
        try {
            generate();

            project.addCompileSourceRoot(outputDirectory.getAbsolutePath());
        } catch (Exception e) {
            getLog().error("General error", e);
        }
    }

    protected abstract void generate() throws Exception;

    protected List<Dependency> getArtifactsDependencies(Artifact a) {
        List<Dependency> ret = new ArrayList<>();

        CollectRequest collectRequest = new CollectRequest();
        collectRequest.setRoot(new Dependency(a, null));
        collectRequest.setRepositories(remoteRepos);

        try {
            DependencyNode node = repoSystem
                    .collectDependencies(repoSession, collectRequest).getRoot();
            DependencyRequest projectDependencyRequest = new DependencyRequest(
                    node, null);

            repoSystem.resolveDependencies(repoSession,
                    projectDependencyRequest);

            PreorderNodeListGenerator nlg = new PreorderNodeListGenerator();
            node.accept(nlg);

            ret.addAll(nlg.getDependencies(true));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    protected Artifact getProjectArtifact() {
        return new DefaultArtifact(project.getArtifact().toString());
    }
}
