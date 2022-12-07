package com.alexy.city

import org.scalameter.api._
import org.scalameter.picklers.Implicits._
import scala.util.Random

object ScanBenchmark extends Bench.OfflineRegressionReport {
  override lazy val executor = LocalExecutor(
    new Executor.Warmer.Default,
    Aggregator.median[Double],
    measurer)

  val sizes = Seq(
    1e4,  // 10k
    2e4,  // 20k
    5e4,  // 50k
    1e5,  // 100k
    1e6,  // 1m
    1e7   // 10m
  ).map(_.toInt)

  sizes.foreach { size =>

    val in = {
      val max = 10000
      val r = new Random()
      Array.fill(size)(r.nextFloat * r.nextInt(max))
    }

    val thresholds: Gen[Int] = Gen.exponential("threshold")(2, 2 * size, 2)

    performance of s"ParallelScan with $size elements in array" in {
      measure method "sequentialDownsweep" in {
        val out = new Array[Float](size)
        using(thresholds) in { _ =>
          ParallelScan.sequentialDownsweep(in, out, Int.MinValue, 0, size)
        }
      }

      measure method "scan" in {
        val out = new Array[Float](size)
        using(thresholds) in { threshold =>
          ParallelScan.scan(in, out, threshold)
        }
      }
    }
  }
}

