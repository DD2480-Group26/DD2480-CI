# DD2480-CI

This project implements the [Continous Integration (Asssignment 2)](https://kth.instructure.com/courses/31884/assignments/185708).

This program is the core code for a CI server linked to GitHub. On push the GitHub repository send an https request to the server. The server then clone the repository, compile and test the code. Then an e-mail is send to the members of the team.

## Project structure
The main class are located in [`ci/src/main/java`](ci/src/main/ja).
The test class are located in [`ci/src/test/java`](ci/src/test/ja)

## Statement of contributions

### Individual
Elisabeth: Notification with email, unit test for notification, PushStatus.

Victor: JGit library, git connection, clone… , and unit test for the pull/checkout command.

Julia: Implement a handler for incoming http requests to our server. Checking and parsing payload. Test compilation status for committed code.

Carl: Gradle.

Hemen: Implement the function for test code on commit.

### General
As a group, we broke down the requirements of the projects into issues describing features. Then we assigned a person responsible for an issue during our meetings. Some other issues were created and assigned during the development of the projects.

Below is a description of our workflow with github when working on these issues:

For documentation, we used Javadoc annotations. For communication we are using different tools as Google Drive, Zoom and Discord.

### Compilation
Compilation is implemented by cloning the relevant branch into its own directory, then running the command `./gradlew build` in the directory. While running this command, testing is also executed, and thus is implemented in the exact same way. After the build result has been gathered, the directory is deleted.

### Notification
Created an Email class in Java with the package javax.mail. This Email class handles the notifications. For every push, it will send the CI result to every participant in the group project. The CI results include if the committed code compiled successfully and if it passed all the tests. If it fails to either compile or to pass all tests it will give an error message for why it failed. The CI results will come from the pushStatus object that will be given as a parameter. The Email class will return if the email was sent successfully and is used in the unit tests for the email. In the unit test, both successfully and failed sent emails are tested, where different numbers of recipients are used. It also tests if it can get the correct content from the PushStatus-object, where different parameters in PushStatus are used.

### Execution
For implementation of tests, see implementation of compilation above.

### Group state
According to the checklist for Way-of-working given in “Essence -kernel and language for software engineering methods,” (Omg.org, 2018), the group could be said to be in the “Collaborating” state. Our communication is clearly better than it was during the first assignment. For example, a few days after the first assignment presentation, we did a reflection on the points that could be improved in our workflow. In particular, before every meeting, everyone should define the points that need to be discussed. This made the meetings quicker and more efficient.  If someone needs some help on an issue, we also decided to do small meetings with 2 or 3 people. Finally, most of the work is done, and every team member knows and trusts each other, but we are not performing well enough to finish the P+ requirements in time. Because of this point,  we don’t think that we can pretend to be the “Performing” state for now. We are aiming to reach this state for the next assignment.

### Include remarkable contribution in Statement of contributions
We plan to check that the author of the commit is a member of our project. 
