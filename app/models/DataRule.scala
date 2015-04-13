package models

object DataRule {
	case class Akhir(besar: List[Int], kecil: List[Int], konfidensi: Double)
	case class KoleksiNDanMinusSatu(besar: List[Int], kecil: List[List[Int]])
}
