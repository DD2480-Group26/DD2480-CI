# Continuous Integration Server
A continuous integration (CI) server is a server which continuously checks a certain repository, and creates notifications about pushes to the repository etc.

This CI server will parse a commit by cloning the repository, compiling the code, running the code's tests, and then notify the commiter about the result of the compilation and tests through email.

### Run and build
* The CI server can be run using the command `./gradlew run`, and will listen on port 8080. 
* To build (compile and test), use `./gradlew build`.

### Project structure
The project uses the standard Gradle application structure.
* The implementation code is located in [`ci/src/main/java`](ci/src/main/java).
* The tests are located in [`ci/src/test/java`](ci/src/test/java).

### Build history (P7)
* Links to all build logs can be found by going to `/builds`, e.g. http://localhost:8080/builds. 
* An individual build log can be accessed by going to `/builds/<dateofbuild>.txt`, e.g. http://localhost:8080/builds/2022-02-14T18-00-04.txt.

Example logs:
```
Build date: 2022-02-14 18:00:04 +01:00
Status for commit e537f113bbcc2ba3a82d7a71719ebe9ebb1f9e38:
 	Compile: Success
	Test: Success 
```
```
Build date: 2022-02-15 18:57:16 +01:00
Status for commit 1ac065ee44203ce565f424b839cf3cfd51043778:
 	Compile: Success
	Test: Fail 

Error message for test:
	
 Failedtest: EmailTest > TestSendStringOneRecipientSuccess FAILED
```
### Javadoc
Javadoc for the project can be generated using the command `./gradlew javadoc`. The javadoc files can then be found in the `ci/build/docs/javadoc` directory.

# Statement of contributions
### Individual
Elisabeth: Notification with email, unit test for notification, PushStatus.

Victor: JGit library, git connection, clone… , and unit test for the pull/checkout command.

Julia: Implement a handler for incoming http requests to our server. Checking and parsing payload. Test compilation status for committed code.

Carl: Gradle, build history (P7), README.

Hemen: Implement the function for test code on commit.

### Remarkable contribution (P8)
We check that the author of the commit is a member of our project, thus a non-member's commit would not activate the CI server.

# Implementation and tests
### Compilation and testing
Compilation and testing is implemented by cloning the relevant branch into its own directory, then running the command `./gradlew build` in the directory. When running this command, the code is first compiled and then the tests are ran. After the build result has been logged, the directory is deleted. 

Unit-tests of compilation and testing are implemented by using a folder containing three projects, where one project will compile and pass all tests; one will compile but fail a test; and another will not compile. It is then possible to assert that running the compiling and testing code on each project returns the expected result.
### Notification
Created an Email class in Java with the package javax.mail. This Email class handles the notifications. For every push, it will send the CI result to every participant in the group project. The CI results include if the committed code compiled successfully and if it passed all the tests. If it fails to either compile or to pass all tests it will give an error message for why it failed. The CI results will come from the pushStatus object that will be given as a parameter. The Email class will return if the email was sent successfully and is used in the unit tests for the email. In the unit test, both successfully and failed sent emails are tested, where different numbers of recipients are used. It also tests if it can get the correct content from the PushStatus-object, where different parameters in PushStatus are used.

# State of the team (SEMAT)
According to the checklist for Way-of-working given in “Essence -kernel and language for software engineering methods,” (Omg.org, 2018), the group could be said to be in the “Collaborating” state. Our communication is clearly better than it was during the first assignment. For example, a few days after the first assignment presentation, we did a reflection on the points that could be improved in our workflow. In particular, before every meeting, everyone should define the points that need to be discussed. This made the meetings quicker and more efficient.  If someone needs some help on an issue, we also decided to do small meetings with 2 or 3 people. Finally, most of the work is done, and every team member knows and trusts each other, but we are not performing well enough to finish the P+ requirements in time. Because of this point,  we don’t think that we can pretend to be the “Performing” state for now. We are aiming to reach this state for the next assignment.
