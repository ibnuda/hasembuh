package models.daotransaksi

import play.api.db.slick.Config.driver.simple._

object DBTableDefinitions {

	val slickBarang = TableQuery[DBBarang]
	val slickTransak = TableQuery[DBTransaksi]

	case class Barang(idbarang: Int, nabarang: String, habarang: Int)

	case class Transaksi(no: Int, idTrans: Int, idBarang: Int)

	class DBBarang(tag: Tag) extends Table[Barang](tag, "barang") {
		def * = (idbarang, nabarang, habarang) <>(Barang.tupled, Barang.unapply)

		def idbarang = column[Int]("idbarang", O.PrimaryKey, O.AutoInc)

		def nabarang = column[String]("nabarang", O.NotNull)

		def habarang = column[Int]("habarang", O.NotNull)
	}

	class DBTransaksi(tag: Tag) extends Table[Transaksi](tag, "transaksi") {
		def * = (no, idtrans, idbarang) <>(Transaksi.tupled, Transaksi.unapply)

		def no = column[Int]("no", O.PrimaryKey, O.AutoInc)

		def idtrans = column[Int]("idtrans")

		def idbarang = column[Int]("idbarang")

		def baran = foreignKey("idbarang", idbarang, slickBarang)(_.idbarang)
	}

}
