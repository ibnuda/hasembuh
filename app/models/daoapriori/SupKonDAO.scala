package models.daoapriori

import models.daoapriori.DBTableDefinitions.SupKon

trait SupKonDAO {
	def save(supKon: SupKon)
	def panjang: Int
	def all: List[SupKon]
}