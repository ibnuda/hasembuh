package models.daoapriori

import models.MyPostgresDriver.simple._
import models.DataRule.SupKon
import models.daoapriori.DBTableDefinitions.slickSupKon
import play.api.db.slick._

class SupKonDAOSlick extends SupKonDAO {

	import play.api.Play.current

	def minimumSupport: Int = {
		DB withSession { implicit session =>
			slickSupKon.map(_.support).first
		}
	}

	def save(supKon: SupKon) = {
		DB withSession { implicit session =>
			val inserted = new SupKon(1, supKon.bundle, supKon.support, supKon.konfidensi)
			slickSupKon.insert(inserted)
		}
	}

	def panjang: Int = {
		DB withSession { implicit session =>
			slickSupKon.length.run
		}
	}

	def all: List[SupKon] = {
		DB withSession { implicit session =>
			slickSupKon.sortBy(_.support).list
		}
	}

	def delete(sup: Int) = {
		DB withSession { implicit session =>
			slickSupKon.filter(_.support === sup).delete
		}
	}

	def getBundle: Int = {
		DB withSession { implicit sesion =>
			slickSupKon.first.bundle
		}
	}
}
