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