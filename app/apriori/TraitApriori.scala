package apriori

trait TraitApriori {
	def minimumSupport: Int
	def minimumKonfidensi: Double
	def daftarBarang: List[List[Int]]
	def pruneBarang: List[List[Int]]

	/*
	val lol = List(List(1, 2), List(1, 3), List(1, 5), List(2, 3), List(2, 4), List(2, 5))
	val kek = lol.flatten.distinct.combinations(3).toList

	kek.foreach(x => x.combinations(2).toList.foreach(y => lol.contains(y)))
	val kek = List(List(List(1, 2, 3), List(1)), List(List(2, 3, 4), List(3)))

	for (ar <- kek) {
		for (a <- ar) println(a.head.toString)
	}
	*/
}