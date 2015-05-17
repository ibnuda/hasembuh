package models.daoapriori

import models.daoapriori.DBTableDefinitions._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._

import scala.concurrent.Future

class BarangDAOSlick extends BarangDAO {

	import play.api.Play.current

	def find(id: Int): Future[Option[Barang]] = {
		DB withSession { implicit session =>
			Future.successful {
				slickBarang.filter(_.idbarang === id).firstOption match {
					case Some(barang) => Some(Barang(barang.idbarang, barang.nabarang, barang.habarang))
					case None => None
				}
			}
		}
	}

	def find(nama: String): Future[Option[Barang]] = {
		DB withSession { implicit session =>
			Future.successful {
				slickBarang.filter(_.nabarang === nama).firstOption match {
					case Some(barang) => Some(Barang(barang.idbarang, barang.nabarang, barang.habarang))
					case None => None
				}
			}
		}
	}

	def save(barang: Barang) = {
		DB withTransaction  { implicit session =>
      val inserted = Barang(barang.idbarang, barang.nabarang, barang.habarang)
      slickBarang.filter(_.idbarang === barang.idbarang).firstOption match {
        case Some(barangKetemu) =>
          slickBarang.filter(_.idbarang === barangKetemu.idbarang).update(barangKetemu)
        case None => slickBarang.insert(barang)
      }
      barang
    }
	}

	def save(listBarang: List[Barang]) = {
		for (barang <- listBarang) save(barang)
	}

	def all = {
		DB withSession { implicit session =>
			slickBarang.sortBy(_.idbarang.asc.nullsFirst).list
		}
	}

	def allNama = {
		DB withSession { implicit session =>
			slickBarang.map(b => (b.idbarang, b.nabarang)).list
		}
	}

	def namaBarang(id: Int): String = {
		DB withSession { implicit session =>
			val namaBarang: String = slickBarang.filter(_.idbarang === id).firstOption match {
				case Some(barang) => barang.nabarang
        case _ => "Tidak Ada"
			}
			namaBarang
		}
	}

	def listNamaBarang(listID: List[Int]): String = {
    val listNama = for (id <- listID) yield(namaBarang(id))
		listNama.mkString(", ")
	}
}
