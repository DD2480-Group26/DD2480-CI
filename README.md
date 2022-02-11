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

### General
As a group, we broke down the requirements of the projects into issues describing features. Then we assigned a person responsible for an issue during our meetings. Some other issues were created and assigned during the development of the projects.

Below is a description of our workflow with github when working on these issues:

For documentation, we used Javadoc annotations. For communication we are using different tools as Google Drive, Zoom and Discord.

### Compilation
Compilation is done using gradle and the following command : "./gradlew run"

Gradle manage all the depencies. For example, the library we are using to clone and pull the git repository relies on others that are automatically imported by gradle.

### Notification

### Execution


### Group state
According to the checklist for Way-of-working given in “Essence -kernel and language for software engineering methods,” (Omg.org, 2018), the group could be said to be in the “Collaborating” state. Our communication is clearly better than it was during the first assignment. For example, a few days after the first assignment presentation, we did a reflection on the points that could be improved in our workflow. In particular, before every meeting, everyone should define the points that need to be discussed. This made the meetings quicker and more efficient.  If someone needs some help on an issue, we also decided to do small meetings with 2 or 3 people. Finally, most of the work is done, and every team member knows and trusts each other, but we are not performing well enough to finish the P+ requirements in time. Because of this point,  we don’t think that we can pretend to be the “Performing” state for now. We are aiming to reach this state for the next assignment.
