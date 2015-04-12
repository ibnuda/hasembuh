package models.daoapriori

import models.MyPostgresDriver.simple._
import models.daoapriori.DBTableDefinitions.SupKon
import models.daoapriori.DBTableDefinitions.slickSupKon
import play.api.db.slick._

class SupKonDAOSlick extends SupKonDAO {

	import play.api.Play.current

	def minimumSupport = {
		DB withSession { implicit session =>
			slickSupKon.map(_.support).first
		}
	}

	def save(supKon: SupKon) = {
		DB withSession { implicit session =>
			val inserted = new SupKon(1, supKon.support, supKon.konfidensi)
			slickSupKon.insert(inserted)
		}
	}

	def panjang = {
		DB withSession { implicit session =>
			slickSupKon.length.run
		}
	}

	def all = {
		DB withSession { implicit session =>
			slickSupKon.sortBy(_.support).list
		}
	}

	def delete(sup: Int) = {
		DB withSession { implicit session =>
			slickSupKon.filter(_.support === sup).delete
		}
	}
}
