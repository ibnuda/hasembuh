package models.daoapriori

import models.MyPostgresDriver.simple._
import models.daoapriori.DBTableDefinitions._
import play.api.db.slick._

import scala.concurrent.Future

class SetBarangDAOSlick extends SetBarangDAO {

	import play.api.Play.current
	val supKonDAOSlick = new SupKonDAOSlick

	def find(listBarang: List[Int]) = {
		DB withSession { implicit session =>
			Future.successful {
				slickSetBarang.filter(_.daftar === listBarang).firstOption match {
					case Some(daftar) => Some(daftar)
					case None => None
				}
			}
		}
	}

	def findByKoleksi(koleksi: Int) = {
		DB withSession { implicit session =>
			slickSetBarang.filter(_.koleksi === koleksi).list
		}
	}

	def save(setBarang: SetBarang) = {
		DB withSession { implicit session =>
			Future.successful {
				val setbarang = SetBarang(setBarang.daftar, setBarang.koleksi, setBarang.support)
				slickSetBarang.insert(setbarang)
				setbarang
			}
		}
	}

	def save(listSetBarang: List[SetBarang]) = {
		DB withSession { implicit session =>
			Future.successful {
				for (setBarang <- listSetBarang) save(setBarang)
				listSetBarang
			}
		}
	}

	def lihatKoleksi(koleksi: Int): List[SetBarang] = {
		DB withSession { implicit session =>
			prune(koleksi)
			slickSetBarang.filter(_.koleksi === koleksi).list
		}
	}

	def reset: Int = {
		DB withSession { implicit session =>
			slickSetBarang.delete
		}
	}

	def prune(koleksi: Int): Int = {
		DB withTransaction { implicit session =>
			val minimumSupport: Int = supKonDAOSlick.minimumSupport
			slickSetBarang.filter(_.support < minimumSupport).filter(_.koleksi === koleksi).delete
		}
	}

	def listSetBarang(n: Int): List[List[Int]] = {
		DB withSession { implicit session =>
			val koleksiNMinusSatu: List[SetBarang] = lihatKoleksi(n - 1)
			val setBarangN = {
				for (koleksi <- koleksiNMinusSatu) yield koleksi.daftar
			}
			setBarangN.distinct
		}
	}
}
