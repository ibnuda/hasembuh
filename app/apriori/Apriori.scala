package apriori

import models.daoapriori.DBTableDefinitions.SetBarang
import models.daoapriori.TransaksiDAOSlick
import models.daoapriori.SetBarangDAOSlick

class Apriori {

	val transaksi = new TransaksiDAOSlick
	val setBarang = new SetBarangDAOSlick

	def koleksi1: List[SetBarang] = {
		val daftar: List[Int] = transaksi.allIDBarangTransaksi.sorted
		val daftarKoleksi = {
			for (daf <- daftar) yield SetBarang(List(daf), 1, daftar.count(_ == daf))
		}
		daftarKoleksi.distinct
	}

	def resetTabel = {
		setBarang.reset
	}
}