package models.daoapriori

import models.daoapriori.DBTableDefinitions.SetBarang

import scala.concurrent.Future

trait SetBarangDAO {
	def find(list: List[Int]): Future[Option[SetBarang]]
	def findByKoleksi(koleksi: Int): Future[Option[List[SetBarang]]]
	def save(setBarang: SetBarang): Future[SetBarang]
	def save(listSetBarang: List[SetBarang]): Future[List[SetBarang]]
}
