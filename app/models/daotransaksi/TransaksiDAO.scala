package models.daotransaksi

import models.daotransaksi.DBTableDefinitions.Transaksi

import scala.concurrent.Future

trait TransaksiDAO {
	def find(idtrans: Int): Future[Option[Transaksi]]
	def find(idtrans: Int, idbarang: Int): Future[Option[Transaksi]]
	def save(transaksi: Transaksi): Future[Transaksi]
	def all(): List[Transaksi]
	def allPlusNama(): List[(Int, Int, String)]
}
