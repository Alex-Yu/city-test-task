package com.alexy.city
import org.scalatest._
import flatspec._
import org.scalatest.matchers.should.Matchers._

class ParallelScanSpec extends AnyFlatSpec {
  behavior of "sequentialUpsweep"

  it should "find max" in {
    val input = Array[Float](15, 5, 10, 3, 2, 11, 4, 50)
    ParallelScan.sequentialUpsweep(input, 1, 7) shouldBe 11
  }

  behavior of "sequentialDownsweep"

  it should "return proper max scan in the output" in {
    val input = Array[Float](3, 5, 10, 3, 2, 11, 4, 50)
    val output = new Array[Float](input.length)

    val from = 1
    val until = 7
    ParallelScan.sequentialDownsweep(input, output, input(0), 1, 7)

    output.slice(from, until) shouldBe Array(5, 10, 10, 10, 11, 11)
  }

  behavior of "scan"

  it should "scan properly" in {
    val input = Array[Float](3, 5, 10, 3, 2, 11, 4, 5)
    val output = new Array[Float](input.length)

    val expected = Array[Float](3, 5, 10, 10, 10, 11, 11, 11)

    ParallelScan.scan(input, output, 3)

    output shouldBe expected
  }
}
