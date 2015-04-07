package models.daotransaksi

import models.daotransaksi.DBTableDefinitions._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._

import scala.concurrent.Future

class BarangDAOSlick extends BarangDAO {

	import play.api.Play.current

	def find(id: Int) = {
		DB withSession { implicit session =>
			Future.successful {
				slickBarang.filter(_.idbarang === id).firstOption match {
					case Some(barang) => Some(Barang(barang.idbarang, barang.nabarang, barang.habarang))
					case None => None
				}
			}
		}
	}

	def find(nama: String) = {
		DB withSession { implicit session =>
			Future.successful {
				slickBarang.filter(_.nabarang === nama).firstOption match {
					case Some(barang) => Some(Barang(barang.idbarang, barang.nabarang, barang.habarang))
					case None => None
				}
			}
		}
	}

	//def findHarga(harga: Int)

	def save(barang: Barang) = {
		DB withSession { implicit session =>
			Future.successful {
				val inserted = Barang(barang.idbarang, barang.nabarang, barang.habarang)
				slickBarang.filter(_.idbarang === barang.idbarang).firstOption match {
					case Some(barangKetemu) =>
						slickBarang.filter(_.idbarang === barangKetemu.idbarang).update(barangKetemu)
					case None => slickBarang.insert(barang)
				}
				barang
			}
		}
	}
}
