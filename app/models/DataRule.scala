package models

object DataRule {
	case class Akhir(besar: List[Int], kecil: List[Int], konfidensi: Double)
	case class Akhir2(besar: String, kecil: String, konfidensi: Double)
	case class KoleksiNDanMinusSatu(besar: List[Int], kecil: List[List[Int]])
	case class Tampilkan(daftar: String, koleksi: Int, support: Int)
}
