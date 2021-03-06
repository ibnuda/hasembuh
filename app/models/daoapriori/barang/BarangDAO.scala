package models.daoapriori.barang

import models.DataRule.Barang

import scala.concurrent.Future

trait BarangDAO {
	def find(id: Int): Future[Option[Barang]]

	def find(nama: String): Future[Option[Barang]]

	//def findHarga(harga: Int): Future[Option[Barang]]
	def save(barang: Barang)
	def save(listBarang: List[Barang])

	def all(): List[Barang]
	def allNama(): List[(Int, String)]
}
