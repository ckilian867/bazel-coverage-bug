# bazel-coverage-bug

## Summary

When the classpath of a test is over the [CLASSPATH_LIMIT](https://github.com/bazelbuild/bazel/blob/master/src/main/java/com/google/devtools/build/lib/bazel/rules/java/java_stub_template.txt#L395), Bazel will create a [manifest jar](https://github.com/bazelbuild/bazel/blob/master/src/main/java/com/google/devtools/build/lib/bazel/rules/java/java_stub_template.txt#L344) that contains a manifest with an entry of the classpath.

The [JacocoCoverageRunner](https://cs.opensource.google/bazel/bazel/+/release-6.2.1:src/java_tools/junitrunner/java/com/google/testing/coverage/JacocoCoverageRunner.java;l=363) accounts for this and will undo the process to find the classpath during coverage collection. However, one of the checks [(urls.length == 1)](https://cs.opensource.google/bazel/bazel/+/release-6.2.1:src/java_tools/junitrunner/java/com/google/testing/coverage/JacocoCoverageRunner.java;l=368) assumes that the manifest jar is the only jar on the classpath.

If a test has a jar in its `data`, it can also be added to the classpath. Since the length of the array is greater than 1, the logic is skipped and the coverage collection file is empty.

## Reproduction

`//repro:repro_test` is a simple test that adds a bytebuddy jar as a javaagent and adds the relevant jar to `data`.

Since the classpath is very small, the manifest jar is not created, so coverage is collected successfully:

```
$ bazel test //repro:repro_test
...
$ du -h bazel-out/_coverage/_coverage_report.dat
4.0K	bazel-out/_coverage/_coverage_report.dat
```

However, if I manually override CLASSPATH_LIMIT so that a manfiest jar is created, coverage disappears.

```
$ bazel test //repro:repro_test --action_env CLASSPATH_LIMIT=1
...
$ du -h bazel-out/_coverage/_coverage_report.dat
0	bazel-out/_coverage/_coverage_report.dat
```

If I remove the javaagent and run the same command, coverage is collected again:

```
$ bazel test //repro:repro_test --action_env CLASSPATH_LIMIT=1
...
$ du -h bazel-out/_coverage/_coverage_report.dat
4.0K	bazel-out/_coverage/_coverage_report.dat
```
