package models.daoapriori.apriori

import models.MyPostgresDriver.simple._
import models.DataRule.SetBarang
import models.daoapriori.DBTableDefinitions._
import models.daoapriori.SupKonDAOSlick
import play.api.db.slick._

import scala.concurrent.Future

class SetBarangDAOSlick extends SetBarangDAO {

	import play.api.Play.current
	val supKonDAOSlick = new SupKonDAOSlick

	def find(listBarang: List[Int]): Option[SetBarang] = {
		DB withSession { implicit session =>
			slickSetBarang.filter(_.daftar === listBarang).firstOption match {
				case Some(daftar) => Some(daftar)
				case None => None
			}
		}
	}

	def findSupport(listBarang: List[Int]): Int = {
		DB withSession { implicit session =>
			try {
				slickSetBarang.filter(_.daftar === listBarang).map(_.support).first.run
			} catch {
				case e: Exception => 1
			}
		}
	}

	def findByKoleksi(koleksi: Int) = {
		DB withSession { implicit session =>
			slickSetBarang.filter(_.koleksi === koleksi).list
		}
	}

	def save(setBarang: SetBarang) = {
		DB withTransaction { implicit session =>
			Future.successful {
				val setbarang = SetBarang(setBarang.daftar, setBarang.koleksi, setBarang.support)
				slickSetBarang.insert(setbarang)
				setbarang
			}
		}
	}

	def save(listSetBarang: List[SetBarang]) = {
		DB withTransaction { implicit session =>
			Future.successful {
				for (setBarang <- listSetBarang) save(setBarang)
				listSetBarang
			}
		}
	}

	def lihatKoleksi(koleksi: Int): List[SetBarang] = {
		DB withSession { implicit session =>
			prune(koleksi)
			slickSetBarang.filter(_.koleksi === koleksi).sortBy(_.daftar).list
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

	def koleksiFinal: Int = {
		DB withSession { implicit session =>
			slickSetBarang.map(_.koleksi).sortBy(_.desc).first
		}
	}

	def isJamak: Boolean = {
		DB withSession { implicit session =>
			val jamak = slickSetBarang.map(_.koleksi).list.distinct
			jamak.length > 1
		}
	}

	def isAda(daftar: List[Int]): Boolean = {
		DB withSession { implicit session =>
			slickSetBarang.filter(_.daftar === daftar).exists.run
		}
	}

}
