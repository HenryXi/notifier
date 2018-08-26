package com.henryxi.maven.notifier;


import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;


@Mojo(name = "notify", inheritByDefault = false, aggregator = true)
public class Notifier extends AbstractMojo {

    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    private MavenSession session;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(property = "executableFilePath", readonly = true)
    private String executableFilePath;

    private Log log = getLog();

    public void execute() throws MojoExecutionException {
        try {
            List<MavenProject> allProjects = session.getAllProjects();
            allProjects.remove(project.getExecutionProject());
            if (allProjects.size() == 1) {
                log.info("building...... 1 project left");
            }
            if (allProjects.size() > 1) {
                log.info("building...... " + allProjects.size() + " projects left");
            }
            if (allProjects.size() == 0) {
                log.info("build finish, start notify");
                File executableFile = new File(executableFilePath);
                if (executableFile.exists()) {
                    Runtime.getRuntime().exec(executableFilePath);
                } else {
                    log.info("file not exist!");
                }
            }
        } catch (Exception e) {
            log.error("henryxi notifier error,", e);
        }
    }
}
