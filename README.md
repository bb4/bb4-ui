# bb4-ui

Common swing UI code for bb4 projects
It is a library project that produces a bb4-ui-X.x jar file.
This jar file along with the source and javadoc jars will be published to Sonatype so
other projects can easily depend on it.

### How to Build
Type 'gradlew build' at the root (or ./gradlew if running in Cygwin). This will build everything, but since its a library project there won't be much to see.
If you want to open the source in Intellij, then first run 'gradle idea'.

When there is a [new release](https://github.com/bb4/bb4-common/wiki/Building-bb4-Projects), versioned artifacts will be published by Barry Becker to [Sonatype](https://oss.sonatype.org).

### License
All source (unless otherwise specified in individual file) is provided under the [MIT License](http://www.opensource.org/licenses/MIT)




