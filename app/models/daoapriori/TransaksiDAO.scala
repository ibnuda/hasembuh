package models.daoapriori

import models.daoapriori.DBTableDefinitions.Transaksi

import scala.concurrent.Future

trait TransaksiDAO {
	def find(idtrans: Int): Option[Transaksi]
	def find(idtrans: Int, idbarang: Int): Option[Transaksi]
	def save(transaksi: Transaksi): Future[Transaksi]
	def all(): List[Transaksi]
	def allPlusNama(): List[(Int, Int, String)]
}
