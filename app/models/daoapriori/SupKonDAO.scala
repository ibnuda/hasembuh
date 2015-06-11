package models.daoapriori

import models.DataRule.SupKon

trait SupKonDAO {
	def minimumSupport: Int
	def save(supKon: SupKon)
	def panjang: Int
	def all: List[SupKon]
	def delete(sup: Int): Int
	def getBundle: Int
}
