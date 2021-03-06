package models.daoapriori.apriori

import models.DataRule.SetBarang

import scala.concurrent.Future

trait SetBarangDAO {
	def find(list: List[Int]): Option[SetBarang]
	def findByKoleksi(koleksi: Int): List[SetBarang]
	def save(setBarang: SetBarang): Future[SetBarang]
	def save(listSetBarang: List[SetBarang]): Future[List[SetBarang]]
	def lihatKoleksi(koleksi: Int): List[SetBarang]
	def reset: Int
	def prune(koleksi: Int): Int
	def listSetBarang(n: Int): List[List[Int]]
}
