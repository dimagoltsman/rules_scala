package scala.test.junit

import build.bazel.tests.integration.BazelBaseTestCase
import org.junit.{Assert, Test}

class SomeHelpreForTest

class SingleTestSoTargetWillNotFailDueToNoTestsTest extends BazelBaseTestCase {
  @Test
  def someTest: Unit ={
    val workspace =
      """
        |rules_scala_version="b158e5e8d5fdd78ba98771f3676f2da4deeafe05" # update this as needed
        |
        |http_archive(
        | name = "io_bazel_rules_scala",
        | url = "https://github.com/wix/rules_scala/archive/%s.zip"%rules_scala_version,
        | type = "zip",
        | strip_prefix= "rules_scala-%s" % rules_scala_version
        |)
        |
        |load("@io_bazel_rules_scala//junit:junit.bzl", "junit_repositories")
        |junit_repositories()
        |
        |load("@io_bazel_rules_scala//scala:toolchains.bzl", "scala_register_toolchains")
        |scala_register_toolchains()
        |
        |load("@io_bazel_rules_scala//scala:scala.bzl", "scala_repositories")
        |scala_repositories()
        |
        |
        |
        |build_bazel_integration_testing_version="36ffe6fe0f4bb727c1fe34209a8d6fd33d8d0d8e" # update this as needed
        |http_archive(
        |    name = "build_bazel_integration_testing",
        |    url = "https://github.com/bazelbuild/bazel-integration-testing/archive/%s.zip"%build_bazel_integration_testing_version,
        |    strip_prefix = "bazel-integration-testing-" + build_bazel_integration_testing_version,
        |)
        |load("@build_bazel_integration_testing//tools:repositories.bzl", "bazel_binaries")
        |bazel_binaries(versions = ["0.12.0"])
        |load("@build_bazel_integration_testing//tools:bazel_java_integration_test.bzl", "bazel_java_integration_test_deps")
        |bazel_java_integration_test_deps()
        |
        |
        |load("@build_bazel_integration_testing//tools:import.bzl", "bazel_external_dependency_archive")
        |
        |
        |BAZEL_JAVA_LAUNCHER_VERSION = "0.4.5"
        |java_stub_template_url = ("raw.githubusercontent.com/bazelbuild/bazel/" +
        |                            BAZEL_JAVA_LAUNCHER_VERSION +
        |                            "/src/main/java/com/google/devtools/build/lib/bazel/rules/java/" +
        |                            "java_stub_template.txt")
        |
        |bazel_external_dependency_archive(
        |    name = "java_stub_template",
        |    srcs = {
        |        "f09d06d55cd25168427a323eb29d32beca0ded43bec80d76fc6acd8199a24489": [
        |            "https://mirror.bazel.build/%s" % java_stub_template_url,
        |            "https://%s" % java_stub_template_url
        |        ],
        |    },
        |)
        |
      """.stripMargin


    driver.scratchFile("WORKSPACE", workspace)

    val build =
      """
        |load("@io_bazel_rules_scala//scala:scala.bzl", "scala_junit_test")
        |
        |scala_junit_test(
        |    name = "no_tests_found",
        |    srcs = ["JunitTest.scala"],
        |    suffixes = ["DoesNotMatch"],
        |    size = "small"
        |)
      """.stripMargin

    driver.scratchFile("BUILD", build)

    val junitTest =
      """
        |import org.junit.Test
        |import java.io._
        |class JunitTest {
        |
        |  @Test
        |  def running: Unit = {
        |  println("#########ALO########3")
        |  al pw = new PrintWriter(new File("/tmp/hello.txt" ))
        |pw.write("Hello, world")
        |pw.close
        |  }
        |}
      """.stripMargin

    driver.scratchFile("JunitTest.scala", junitTest)

    val cmd = driver.bazelCommand("test", "//...").build()
    val res = cmd.run()

    cmd.getErrorLines.toArray.toSeq.foreach(println)


    Assert.assertNotEquals(res, 0 )

  }
  //test_expect_failure/scala_junit_test:no_tests_found


}