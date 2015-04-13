package apriori

import models.daoapriori.DBTableDefinitions.SetBarang
import models.daoapriori.TransaksiDAOSlick
import models.daoapriori.SetBarangDAOSlick

class Apriori {

	val transaksi = new TransaksiDAOSlick
	val setBarang = new SetBarangDAOSlick

	def resetTabel: Int = {
		setBarang.reset
	}

	def koleksi1: List[SetBarang] = {
		val daftar: List[Int] = transaksi.allIDBarangTransaksi.sorted
		val daftarKoleksi = {
			for (daf <- daftar) yield SetBarang(List(daf), 1, daftar.count(_ == daf))
		}
		daftarKoleksi.distinct
	}

	def koleksiN(n: Int): List[SetBarang] = {
		val koleksiNMinusSatu: List[SetBarang] = setBarang.findByKoleksi(n - 1)
		val koleksinya: List[List[Int]] = generateKoleksi(n, setBarang.listSetBarang(n))
		val koleksiKeN = {
			for (koleksi <- koleksinya) yield SetBarang(koleksi, n, transaksi.findByList(koleksi))
		}
		koleksiKeN
	}

	def generateKoleksi(n: Int, koleksiNMinusSatu: List[List[Int]]): List[List[Int]] = {
		val hasilKoleksiN = koleksiNMinusSatu.flatten.distinct.combinations(n).toList
		if (n <= 2){
			hasilKoleksiN.distinct
		} else {
			val koleksiNLebihDariDua = hasilKoleksiN.filter(x => superSubSet((n - 1), x, koleksiNMinusSatu))
			koleksiNLebihDariDua.distinct
		}
	}

	def superSubSet(koleksi: Int, kecil: List[Int], besar: List[List[Int]]): Boolean = {
		kecil.combinations(koleksi).toList.forall(x => besar.contains(x))
	}

	def prune(koleksi: Int) = setBarang.prune(koleksi)

}