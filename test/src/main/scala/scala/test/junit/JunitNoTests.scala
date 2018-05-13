package scala.test.junit

import org.junit.Test
import build.bazel.tests.integration.BazelBaseTestCase
import java.lang.System

class SomeHelpreForTest


//class AAABlatTest{
//  @Test
//  def someTest: Unit ={
//    System.setProperty("bazel.configuration", "./tools/bazel_java_integration_test.bzl")
//    println("#######################")
//  }
//}

class SingleTestSoTargetWillNotFailDueToNoTestsTest extends BazelBaseTestCase {
  @Test
  def someTest: Unit =
  	println("passing")
}