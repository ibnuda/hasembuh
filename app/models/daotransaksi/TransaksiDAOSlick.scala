package models.daotransaksi

import play.api.db.slick.Config.driver.simple._
import models.daotransaksi.DBTableDefinitions._
import play.api.db.slick._

import scala.concurrent.Future

class TransaksiDAOSlick extends TransaksiDAO {

	import play.api.Play.current

	def find(id: Int) = {
		DB withSession { implicit session =>
			Future.successful {
				slickTransak.filter(_.idtrans === id).firstOption match {
					case Some(tran) => Some(Transaksi(tran.no, tran.idtrans, tran.idbarang))
					case None => None
				}
			}
		}
	}

	def find(id: Int, idbarang: Int) = {
		DB withSession { implicit session =>
			Future.successful {
				slickTransak.filter(x => x.idtrans === id && x.idbarang === idbarang).firstOption match {
					case Some(tran) => Some(Transaksi(tran.no, tran.idtrans, tran.idbarang))
					case None => None
				}
			}
		}
	}

	def save(transaksi: Transaksi) = {
		DB withSession { implicit session =>
			Future.successful {
				//val panjang = slickTransak.length + 1
				//val tran = (slickTransak returning slickTransak.map(_.no)) += Transaksi(None, transaksi.idtrans, transaksi.idbarang)
				val tran = Transaksi(transaksi.no , transaksi.idtrans, transaksi.idbarang)
				slickTransak.insert(tran)
				tran
			}
		}
	}

	def all = {
		DB withSession { implicit session =>
			slickTransak.sortBy(_.no.asc.nullsLast).list
		}
	}

	def allPlusNama = {
		DB withSession { implicit session =>
			val implisitJoin = for {
				transaksi <- slickTransak
				barang <- slickBarang if transaksi.idbarang === barang.idbarang
			} yield (transaksi.no, transaksi.idtrans, barang.nabarang)
			implisitJoin.list
		}
	}

	def allIDBarangTransaksi = {
		DB withSession { implicit session =>
			slickTransak.map(_.idbarang).list
		}
	}
}
