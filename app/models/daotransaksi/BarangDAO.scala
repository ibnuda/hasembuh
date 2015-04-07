package models.daotransaksi

import models.daotransaksi.DBTableDefinitions.Barang

import scala.concurrent.Future

trait BarangDAO {
	def find(id: Int): Future[Option[Barang]]

	def find(nama: String): Future[Option[Barang]]

	//def findHarga(harga: Int): Future[Option[Barang]]
	def save(barang: Barang): Future[Barang]
}
