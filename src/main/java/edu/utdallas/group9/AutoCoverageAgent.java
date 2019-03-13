package edu.utdallas.group9;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.nio.file.Paths;


public class AutoCoverageAgent {

    public static void main(String[] args){
        if (args.length != 1) {
            System.out.println("Invalid arguments. Usage: AutoCoverageAgent [repoUrl]");
            return;
        }

        downloadRepo(args[0]);
    }

    private static void downloadRepo(String repoUrl) {
        String dir = repoUrl.substring(repoUrl.lastIndexOf('/') + 1);
        String cloneDirectoryPath = "downloads" + File.separator + dir;
        try {
            System.out.println("Cloning "+ repoUrl);
            Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(Paths.get(cloneDirectoryPath).toFile())
                    .call();
            System.out.println("Completed Cloning");
        } catch (GitAPIException e) {
            System.out.println("Exception occurred while cloning repo");
            e.printStackTrace();
        }
    }
}
