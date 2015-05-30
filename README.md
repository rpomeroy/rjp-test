# A Collection of Maven/Jenkins CI-isms

When implementing Jenkins CI builds for Maven projects we have a few goals:

* Every build should produce a unique version
* Within a build pipeline, the deliverable artifact must be built once and only once
* The build pipeline configuration must be stored in version control - preferable with the project
* Maven settings should be kept with the project - not relying on the user environment that Jenkins is running under
* Maven build-specific JVM settings would be nice to store in the project also
* *Would be even nicer if Maven had the equivalent of gradlew so we could package the build system with the project - alas it's not a standard thing in the Maven world!*

In this project I'll attempt to bring all this together.

## Unique Build Numbers
I like my Maven artifacts versions from CI builds to be of the form:

`
${artifactId}.${major}.${minor}.${patch}.${timestamp}-${git-short-hash}
`
In the past I accomplished this by doing the following:

* Using the [build-helper-maven-plugin](http://mojo.codehaus.org/build-helper-maven-plugin/) to parse the version into it's constituent parts
* Using the [gmaven-plugin](http://docs.codehaus.org/display/GMAVEN/Executing+Groovy+Code) to script getting the git short hash and generating a timestamp and injecting it into the build.
* Setting the `<finalName>` to an expression like the above
* ugg...

This sorta worked but the packaged pom.properties was still getting the version from what was last stored in the pom.xml on the disk. This was solved with:

* Using the [versions-maven-plugin](http://mojo.codehaus.org/versions-maven-plugin/) to set the version and rewrite the pom.xml in a pre-build step. 

### Only Slightly Improved Approach

Personally I think Maven should just bake in support for most of this. Lacking that I managed to get the process down to two plugins and I eliminated the pre-build step.  Some of this was just lack of knowledge on my part. Some was solved by new plugins from the community.  Still I wish this use-case could just be bundled up in a single plugin that just did the right thing.

* Use the [buildnumber-maven-plugin](buildnumber-maven-plugin) to get the git SHA1 hash (short version)
* Use the [build-helper-maven-plugin](http://mojo.codehaus.org/build-helper-maven-plugin/) to parse the version into it's constituent parts as before
* Set the `<finalName>` as before
* Enable filtering for `src/main/resources/`
* Add a custom pom.properties with the following content in `src/main/resources/META-INF/maven/${groupId}/${artifactId}/`

```
version=${project.build.finalName}
groupId=com.rjp
artifactId=rjp-test

```

and everything works as needed.  See the [pom.xml](pom.xml) for all the details
 
## Build Pipeline Configuration
The Jenkins [workflow-plugin](https://github.com/jenkinsci/workflow-plugin) looks like the only good answer to this problem presently (and it looks pretty good).  

*...more to to be written on this as soon as I learn it myself!*

## Per Project Maven Configuration
Recent versions (3.1+) of Maven now allow for a .mvn folder in your project where you can place a `settings.xml` and a `maven.config` file.  This is a welcome addition.  I won't repeat the [details](http://takari.io/2015/03/20/mmp.html) here.  Suffice to say it solves the last two problems nicely.

