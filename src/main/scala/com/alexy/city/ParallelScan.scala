
package com.alexy.city

object ParallelScan {
  /**
   *
   * Familiarize yourself with parallel partial reduction trees:
   *
   * https://www.sciencedirect.com/topics/computer-science/partial-reduction
   *
   * In this exercise, your task is to scan left on a given input array and output
   * a new array consisting of the maximum values to the left of a given index in
   * the original array.
   *
   * Example:
   * In: [0, 0, 1, 5, 2, 3, 6]
   * Out: [0, 0, 1, 5, 5, 5, 6]
   *
   * *** Task 1 ***
   * Using the partial parallel reduction tree paradigm, implement the methods described
   * below, making use of the parallelism abstractions provided. You should not utilize
   * data or function parallel constructs that have not been provided.
   *
   * Utilize good judgment when choosing side-effecting vs pure implementations to
   * blend good functional style with performance.
   *
   * Note that a trivial sequential
   * implementation has been provided for reference.
   *
   * *** Task 2 ***
   * Using scalameter, compare performance between a fully sequential implementation
   * and your parallel implementation, and provide some recommendations for optimal
   * threshold values for your system.
   *
   * *** Task 3 ***
   * Using your observations from Task 2, extrapolate to general systems. */

  /** Input: the given part of the array and returns the maximum value.
   * from - inclusive
   * until - non-inclusive
   */
  def sequentialUpsweep(input: Array[Float], from: Int, until: Int): Float = {
    var max = input(from)
    var i = from + 1
    while (i < until) {
      max = Math.max(input(i), max)
      i += 1
    }
    max
  }

  /** Traverses the part of the array starting at `from` and until `until`, and
   *  returns the reduction tree for that part of the array.
   *
   *  The reduction tree is a `Tree.Leaf` if the length of the specified part of the
   *  array is smaller or equal to `threshold`, and a `Tree.Node` otherwise.
   *  If the specified part of the array is longer than `threshold`, then the
   *  work is divided and done recursively in parallel.
   */
  def upsweep(input: Array[Float], from: Int, until: Int, threshold: Int): Tree = {
    if (until - from > threshold) {
      val middle = from + (until - from) / 2
      val (left, right) = parallel(
        upsweep(input, from, middle, threshold),
        upsweep(input, middle, until, threshold))
      Node(left, right)
    } else {
      Leaf(from, until, sequentialUpsweep(input, from, until))
    }
  }

  /** Traverses the part of the `input` array starting at `from` and until
   *  `until`, and computes the maximum value for each entry of the output array,
   *  given the `startingValue`.
   */
  def sequentialDownsweep(input: Array[Float],
                          output: Array[Float],
                          startingValue: Float,
                          from: Int,
                          until: Int): Unit = {

    output(from) = math.max(startingValue, input(from))
    var i = from + 1
    while (i < until) {
      output(i) = math.max(output(i - 1), input(i))
      i += 1
    }
  }

  def fill(output: Array[Float], value: Float, from: Int, until: Int): Unit = {
    var i = from
    while (i < until) {
      output(i) = value
      i += 1
    }
  }

  /** Pushes the maximum value in the prefix of the array to each leaf of the
   *  reduction `tree` in parallel, and then calls `downsweepSequential` to write
   *  the `output` values.
   */
  def downsweep(input: Array[Float],
                output: Array[Float],
                startingValue: Float,
                tree: Tree): Unit = {

    tree match {
      case Leaf(from, until, res) if res < startingValue =>
        fill(output, startingValue, from, until)
      case Leaf(from, until, _) =>
        sequentialDownsweep(input, output, startingValue, from, until)
      case Node(left, right) =>
        parallel(
          downsweep(input, output, startingValue, left),
          downsweep(input, output, math.max(left.result, startingValue), right))
    }
  }

  /** Compute the ray shadowing buffer in parallel. */
  def scan(input: Array[Float], output: Array[Float], threshold: Int): Unit = {
    val tree = upsweep(input, 0, input.length, threshold)
    downsweep(input, output, Float.MinValue, tree)
  }

}
