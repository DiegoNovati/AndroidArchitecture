# Developer instruction

## Todo before pushing one ore more commits
Before pushing please run the following command:

```
./gradlew build
```

The 'build' task will assemble, lint and test the debug and release variant of the project.

Please solve all the errors and the warnings displayed in both the 'lint' reports (the urls are 
displayed only when there are warnings) before pushing

Note: this command will raise two warnings

```
WARNING:Your project has set `android.useAndroidX=true`, but configuration `releaseRuntimeClasspath` still contains legacy support libraries, which may cause runtime issues.
This behavior will not be allowed in Android Gradle plugin 8.0.
Please use only AndroidX dependencies or set `android.enableJetifier=true` in the `gradle.properties` file to migrate your project to AndroidX (see https://developer.android.com/jetpack/androidx/migrate for more info).
The following legacy support libraries are detected:
releaseRuntimeClasspath -> com.github.theGlenn:flipper-android-no-op:0.9.0 -> com.github.theGlenn.flipper-android-no-op:flipperandroidnoop:0.9.0 -> com.squareup.leakcanary:leakcanary-android-no-op:1.6.3 -> com.android.support:support-annotations:26.0.0
```
This problem is caused by the library used to proxy the Flipper library in production.
There is nothing we can do except waiting for an update version of the library

```
warning: The following options were not recognized by any processor: '[dagger.fastInit, dagger.hilt.android.internal.disableAndroidSuperclassValidation, dagger.hilt.internal.useAggregatingRootProcessor, kapt.kotlin.generated]'
```
This problem is caused by the 'kapt' library used with Hilt. There is nothing we can do except waiting for an update version of the library


