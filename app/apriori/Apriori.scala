package apriori

import models.DataRule._
import models.daoapriori.DBTableDefinitions.SetBarang
import models.daoapriori.TransaksiDAOSlick
import models.daoapriori.SetBarangDAOSlick
import models.daoapriori.BarangDAOSlick

class Apriori {

	/*
	case class Akhir(besar: List[Int], kecil: List[Int], konfidensi: Double)
	case class KoleksiNDanMinusSatu(besar: List[Int], kecil: List[List[Int]])
	*/

	val slickTransaksi: TransaksiDAOSlick = new TransaksiDAOSlick
	val slickSetBarang: SetBarangDAOSlick = new SetBarangDAOSlick
	val slickBarang: BarangDAOSlick = new BarangDAOSlick

	def resetTabel: Int = slickSetBarang.reset

	def koleksi1: List[SetBarang] = {
		val daftar: List[Int] = slickTransaksi.allIDBarangTransaksi.sorted
		val daftarKoleksi: List[SetBarang] = {
			for (daf <- daftar) yield SetBarang(List(daf), 1, daftar.count(_ == daf))
		}
		daftarKoleksi.distinct
	}

	def koleksiN(n: Int): List[SetBarang] = {
		val koleksiNMinusSatu: List[SetBarang] = slickSetBarang.findByKoleksi(n - 1)
		val koleksinya: List[List[Int]] = generateKoleksi(n, slickSetBarang.listSetBarang(n))
		val koleksiKeN: List[SetBarang] = {
			for (koleksi <- koleksinya) yield SetBarang(koleksi, n, slickTransaksi.findByList(koleksi))
		}
		koleksiKeN
	}

	def generateKoleksi(n: Int, koleksiNMinusSatu: List[List[Int]]): List[List[Int]] = {
		val hasilKoleksiN: List[List[Int]] = koleksiNMinusSatu.flatten.sorted.distinct.combinations(n).toList
		if (n <= 2){
			hasilKoleksiN.distinct
		} else {
			val koleksiNLebihDariDua: List[List[Int]] = hasilKoleksiN.filter(x => superSubSet((n - 1), x, koleksiNMinusSatu))
			koleksiNLebihDariDua.distinct
		}
	}

	def superSubSet(koleksi: Int, kecil: List[Int], besar: List[List[Int]]): Boolean = {
		kecil.combinations(koleksi).toList.forall(x => besar.contains(x))
	}

	def prune(koleksi: Int) = slickSetBarang.prune(koleksi)

	def daftarUntukRule: List[KoleksiNDanMinusSatu] = {
		val dafPunk: List[KoleksiNDanMinusSatu] = if (slickSetBarang.isJamak) {
			val akhir:Int = slickSetBarang.koleksiFinal
			val kolFinal: List[SetBarang] = slickSetBarang.lihatKoleksi(akhir)
			val kolFinalMinusSatu: List[SetBarang] = slickSetBarang.lihatKoleksi(akhir - 1)
			val daftarKoleksiFinal: List[List[Int]] = for (kf <- kolFinal) yield kf.daftar
			val daftarKoleksiFinalMinusSatu: List[List[Int]]  = for (kfms <- kolFinalMinusSatu) yield kfms.daftar
			for (daffinal <- daftarKoleksiFinal)
				yield KoleksiNDanMinusSatu(daffinal, daftarKoleksiFinalMinusSatu.filter(x => x.toSet.subsetOf(daffinal.toSet)))
		} else {
			List(KoleksiNDanMinusSatu(List(0), List(List(0))))
		}
		dafPunk
	}

	def hitungRule(daftar: KoleksiNDanMinusSatu): List[Akhir] = {
		val rulenya: List[Akhir] = {
			val besar = daftar.besar
			for (kecil <- daftar.kecil)
			 yield Akhir(besar, kecil, hitungKonfidensi(besar, kecil))
		}
		rulenya
	}

	def hitungKonfidensi(besar: List[Int], kecil: List[Int]): Double = {
		val nilaiBesar: Int = slickSetBarang.findSupport(besar)
		val nilaiKecil: Int = slickSetBarang.findSupport(kecil)
		val nilai: Double = (nilaiBesar.toDouble / nilaiKecil)
		nilai
	}

	def hitungRuleList(daftar: List[KoleksiNDanMinusSatu]):  List[List[Akhir2]] = {
		val listRule: List[List[Akhir]] = {
			for (daf <- daftar) yield hitungRule(daf)
		}
		listRule.map(y => y.map(x =>Akhir2(slickBarang.listNamaBarang(x.besar), slickBarang.listNamaBarang(x.kecil), x.konfidensi * 100)))
	}

	def tampilkanTampilan(daftarSetBarang: List[SetBarang]): List[Tampilkan] = {
		daftarSetBarang.map(x => Tampilkan(slickBarang.listNamaBarang(x.daftar), x.koleksi, x.support))
	}
}