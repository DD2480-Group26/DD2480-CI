package ci;

import java.io.File;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;

public class GitConnector {

    /**
     * Recursively delete a folder and all the files contained
     *
     * @param file Folder name
     */
    public static void deleteDirectory(File file) {
        for (File subfile : file.listFiles()) {

            if (subfile.isDirectory()) {
                deleteDirectory(subfile);
            }
            subfile.delete();
        }
    }


    /**
     * Clone a remote Git repo in a selected folder
     *
     * @param remotePath Https link to Git Repository
     * @param localPath Local folder
     * @return Git object
     */
    public static Git cloneRepo(String remotePath, File localPath) {
        Git git;
        try {
            CloneCommand cloneCommand = new CloneCommand();
            cloneCommand.setURI(remotePath);
            cloneCommand.setDirectory(localPath);
            git = cloneCommand.call();
            return git;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Pull a git repo
     *
     * @param localRepoPath Local folder
     * @param branch Name of the branch to pull
     * @return True if the pull succeeded
     */
    public static boolean gitPull(File localRepoPath, String branch) {
        boolean pullSuccessful;

        try (Git git = Git.open(localRepoPath)) {

            PullCommand pull = git.pull();
            pull.setRemote("origin");
            pull.setRemoteBranchName(branch);
            pull.setRebase(true);
            PullResult result = pull.call();
            pullSuccessful = result.isSuccessful();

        } catch (Exception e) {
            pullSuccessful = false;
        }
        return pullSuccessful;
    }

    /**
     * Checkout from main to the selected branch
     *
     * @param repositoryLocalPath Local folder
     * @param branchName Name  of the branch to pull
     * @return True if the checkout succeeded
     */
    public static boolean checkoutToBranch(File repositoryLocalPath, String branchName) {

        boolean actionCompleted = false;

        try (Git git = Git.open(repositoryLocalPath)) {
            git.checkout()
                    .setName(branchName)
                    .setStartPoint("origin/" + branchName)
                    .call();

            actionCompleted = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actionCompleted;
    }


}

