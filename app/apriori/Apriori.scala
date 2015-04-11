package apriori

import models.DataItemset
import models.daotransaksi.TransaksiDAOSlick
import models.daotransaksi.SetBarangDAOSlick
import play.api.db.slick.Config.driver.simple._

class Apriori extends TraitApriori {

	val transaksi = new TransaksiDAOSlick
	val setBarang = new SetBarangDAOSlick

	def minimumSupport: Int = 2

	def minimumKonfidensi: Double = 0.4

	def daftarBarang: List[List[Int]] = {
		List(List(1, 2, 3))
	}

	def pruneBarang: List[List[Int]] = {
		List(List(1, 2, 3))
	}

	def koleksi1: List[DataItemset] = { //List[(List[Int], Int, Int)] = {
		val daftar: List[Int] = transaksi.allIDBarangTransaksi.sorted
		val daftarKoleksi = {
			for (daf <- daftar) yield DataItemset(List(daf), 1, daftar.count(_ == daf))
		}
		daftarKoleksi.distinct
	}

	def resetTabel = {
		setBarang.reset
	}
}