package scala.test.junit

import build.bazel.tests.integration.BazelBaseTestCase
import org.junit.{Assert, Test}

class SomeHelpreForTest

class SingleTestSoTargetWillNotFailDueToNoTestsTest extends BazelBaseTestCase {
  @Test
  def someTest: Unit ={
    val workspace =
      """
        |rules_scala_version="72b402753b82377251d2370a3accfd4999707418" # update this as needed
        |
        |http_archive(
        |             name = "io_bazel_rules_scala",
        |             url = "https://github.com/wix/rules_scala/archive/%s.zip"%rules_scala_version,
        |             type = "zip",
        |             strip_prefix= "rules_scala-%s" % rules_scala_version
        |)
        |
        |load("@io_bazel_rules_scala//scala:toolchains.bzl", "scala_register_toolchains")
        |scala_register_toolchains()
        |
        |load("@io_bazel_rules_scala//scala:scala.bzl", "scala_repositories")
        |scala_repositories()
      """.stripMargin


    driver.scratchFile("WORKSPACE", workspace)

    val build =
      """
        |load(
        |    "@io_bazel_rules_scala//scala:scala.bzl",
        |    "scala_library",
        |     )
        |
        |scala_library(
        |    name = "JunitTest",
        |    srcs = ["JunitTest.java"],
        |     )
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
        |  al pw = new PrintWriter(new File("/private/var/tmp/_bazel_dimitrig/1aba28a4159e91abb45df531d110314d/execroot/io_bazel_rules_scala/bazel-out/darwin-fastbuild/testlogs/test/junit_no_tests/bazel0.12.0/hello.txt" ))
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