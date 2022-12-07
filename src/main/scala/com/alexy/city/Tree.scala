package com.alexy.city

sealed trait Tree {
  def result: Float
}
case class Leaf(from: Int, until: Int, override val result: Float) extends Tree
case class Node(left: Tree, right: Tree) extends Tree {
  override val result: Float = math.max(left.result, right.result)
}
