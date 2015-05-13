package models.daoapriori

import play.api.db.slick.Config.driver.simple._
import models.daoapriori.DBTableDefinitions._
import play.api.db.slick._

import scala.concurrent.Future

class TransaksiDAOSlick extends TransaksiDAO {

	import play.api.Play.current

	def find(id: Int): Option[Transaksi] = {
		DB withSession { implicit session =>
			slickTransak.filter(_.idtrans === id).firstOption match {
				case Some(tran) => Some(Transaksi(tran.no, tran.idtrans, tran.idbarang))
				case None => None
			}
		}
	}

	def find(id: Int, idbarang: Int): Option[Transaksi] = {
		DB withSession { implicit session =>
			slickTransak.filter(x => x.idtrans === id && x.idbarang === idbarang).firstOption match {
				case Some(tran) => Some(Transaksi(tran.no, tran.idtrans, tran.idbarang))
				case None => None
			}
		}
	}

	def save(transaksi: Transaksi): Future[Transaksi] = {
		DB withTransaction { implicit session =>
			Future.successful {
				val tran = Transaksi(transaksi.no , transaksi.idtrans, transaksi.idbarang)
				slickTransak.insert(tran)
				tran
			}
		}
	}

	def save(listTransaksi: List[Transaksi]): Future[List[Transaksi]] = {
		DB withTransaction { implicit session =>
			Future.successful {
				for (transaksi <- listTransaksi) save(transaksi)
				listTransaksi
			}
		}
	}

	def all: List[DBTransaksi#TableElementType] = {
		DB withSession { implicit session =>
			slickTransak.sortBy(_.no.asc.nullsLast).list
		}
	}

	def allPlusNama: List[(Int, Int, String)] = {
		DB withSession { implicit session =>
			val implisitJoin = for {
				transaksi <- slickTransak
				barang <- slickBarang if transaksi.idbarang === barang.idbarang
			} yield (transaksi.no, transaksi.idtrans, barang.nabarang)
			implisitJoin.list
		}
	}

	def allIDBarangTransaksi: List[Int] = {
		DB withSession { implicit session =>
			slickTransak.map(_.idbarang).list
		}
	}

	def findListBarang(id: Int): List[Int] = {
		DB withSession { implicit session =>
			//val daftarnya = slickTransak.filter(_.idbarang === id).map(_.idbarang).list
			val daftar = slickTransak.filter(_.idbarang === id).map(_.idtrans).list
			daftar
		}
	}

	def findByList(list: List[Int]): Int = {
		val listListInt = for (li <- list) yield findListBarang(li)
		listListInt.reduceLeft(_ intersect _).length
	}
}
