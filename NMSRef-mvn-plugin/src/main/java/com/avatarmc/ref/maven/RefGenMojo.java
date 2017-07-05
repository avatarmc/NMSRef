package com.avatarmc.ref.maven;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

import org.apache.maven.model.Dependency;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;

import com.google.common.collect.ImmutableSet;

/**
 * @goal generate-sources
 * @phase generate-sources
 */
public class RefGenMojo extends AbstractCodeGeneratorMojo {

    @Override
    public void generate() throws Exception {
        DefaultArtifact spigot = null;
        for (Dependency o : project.getDependencies()) {
            if (!"spigot".equals(o.getArtifactId())) {
                continue;
            }

            spigot = new DefaultArtifact(o.getGroupId() + ':'
                    + o.getArtifactId() + ':' + o.getVersion(), null);
        }

        if (spigot == null) {
            throw new RuntimeException("Failed to find spigot dependency: "
                    + project.getDependencies());
        }

        File spigotFile = resolveArtifact(spigot);

        getLog().info("Generating mappigns from " + spigot2srgURL + " and "
                + spigotFile);
        getLog().info("mc=" + mcPackage + ",target=" + targetPackage);
        getLog().info("exlucdes=" + excludesFile
                + " (" + excludesFile.exists() + ")");

        Set<String> excludes;
        if (excludesFile.exists()) {
            try (FileInputStream is = new FileInputStream(excludesFile)) {
                excludes = ListReader.read(is);
            }
            getLog().info(excludes.toString());
        } else {
            excludes = ImmutableSet.of();
        }

        try (URLClassLoader loader = new URLClassLoader(
                new URL[] { spigotFile.toURI().toURL() })) {
            new ReflectionGenerator(
                    mappingDirectory,
                    spigot2srgURL,
                    mcpURL,
                    targetPackage, mcPackage,
                    loader,
                    outputDirectory,
                    excludes).run();
        }
    }

    private File resolveArtifact(Artifact artifact) {
        ArtifactRequest request = new ArtifactRequest();
        request.setArtifact(artifact);

        ArtifactResult result;
        try {
            result = repoSystem.resolveArtifact(repoSession, request);
        } catch (ArtifactResolutionException ex) {
            throw new RuntimeException(ex);
        }

        return result.getArtifact().getFile();
    }
}
