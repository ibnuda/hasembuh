package apriori

import models.daotransaksi.TransaksiDAOSlick
import play.api.db.slick.Config.driver.simple._

class Apriori extends TraitApriori {

	val transaksi = new TransaksiDAOSlick

	def minimumSupport: Int = 2

	def minimumKonfidensi: Double = 0.4

	def daftarBarang: List[List[Int]] = {
		List(List(1, 2, 3))
	}

	def pruneBarang: List[List[Int]] = {
		List(List(1, 2, 3))
	}

	def koleksi1: List[List[Int]] = {
		val daftar: List[Int] = transaksi.allIDBarangTransaksi.sorted
		val daftarKoleksi = {
			for (e <- daftar) yield List(e, daftar.count(_ == e))
		}
		daftarKoleksi.distinct
	}
}