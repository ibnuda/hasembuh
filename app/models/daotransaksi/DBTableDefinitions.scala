package models.daotransaksi

import play.api.db.slick.DB
import models.MyPostgresDriver.simple._

object DBTableDefinitions {

	val slickBarang = TableQuery[DBBarang]
	val slickTransak = TableQuery[DBTransaksi]
	val slickSetBarang = TableQuery[DBSetBarang]
	val slickAsosRule = TableQuery[DBAsosRule]

	case class Barang(idbarang: Int, nabarang: String, habarang: Int)

	case class Transaksi(no: Int, idtrans: Int, idbarang: Int)

	case class SetBarang(no :Int, daftar: List[Int], koleksi: Int, support: Int)

	case class AsosRule(no: Int, daftar: List[Int], rule: Map[String, String])

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

	class DBSetBarang(tag: Tag) extends Table[SetBarang](tag, "itemset"){
		def no = column[Int]("no", O.AutoInc, O.PrimaryKey)
		def daftar = column[List[Int]]("daftar")
		def koleksi = column[Int]("koleksi")
		def support = column[Int]("support")
		def * = (no, daftar, koleksi, support) <> (SetBarang.tupled, SetBarang.unapply)
	}

	class DBAsosRule(tag: Tag) extends Table[AsosRule](tag, "asosrule"){
		def no = column[Int]("no", O.AutoInc, O.PrimaryKey)
		def daftar = column[List[Int]]("daftar")
		def rule = column[Map[String, String]]("rule")
		def * = (no, daftar, rule) <> (AsosRule.tupled, AsosRule.unapply)
	}
}
