package models

object DataRule {
	case class Akhir(besar: List[Int], kecil: List[Int], konfidensi: Double)
	case class Akhir2(besar: String, kecil: String, konfidensi: Double)
	case class KoleksiNDanMinusSatu(besar: List[Int], kecil: List[List[Int]])
	case class Tampilkan(daftar: String, koleksi: Int, support: Int)
	case class Barang(idbarang: Int, nabarang: String, habarang: Int)
	case class Transaksi(no: Int, idtrans: Int, idbarang: Int)
	case class SetBarang(daftar: List[Int], koleksi: Int, support: Int)
	case class AsosRule(daftar: List[Int], rule: Map[String, String], konfidensi: Double)
	case class SupKon(id: Int, bundle: Int, support: Int, konfidensi: Double)
}
