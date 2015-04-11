package models

case class DataItemset(itemset: List[Int], koleksi: Int, support: Int)

/*
class DataItemset(a: List[Int], b: Int, c: Int) extends DataItem(a, b, c) {
	override def itemset: List[Int] = a
	override def koleksi: Int = b
	override def support: Int = c
}
*/
