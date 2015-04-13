package models.daoapriori

import models.daoapriori.DBTableDefinitions.SupKon

trait SupKonDAO {
	def minimumSupport: Int
	def save(supKon: SupKon)
	def panjang: Int
	def all: List[SupKon]
	def delete(sup: Int): Int
}
