package models.daoapriori

import play.api.db.slick.DB
import models.MyPostgresDriver.simple._

object DBTableDefinitions {

	val slickBarang = TableQuery[DBBarang]
	val slickTransak = TableQuery[DBTransaksi]
	val slickSetBarang = TableQuery[DBSetBarang]
	val slickAsosRule = TableQuery[DBAsosRule]
	val slickSupKon = TableQuery[DBSupKon]

	case class Barang(idbarang: Int, nabarang: String, habarang: Int)

	case class Transaksi(no: Int, idtrans: Int, idbarang: Int)

	case class SetBarang(daftar: List[Int], koleksi: Int, support: Int)

	case class AsosRule(daftar: List[Int], rule: Map[String, String])

	case class SupKon(id: Int, support: Int, konfidensi: Double)

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
		def daftar = column[List[Int]]("daftar")
		def koleksi = column[Int]("koleksi")
		def support = column[Int]("support")
		def * = (daftar, koleksi, support) <> (SetBarang.tupled, SetBarang.unapply)
	}

	class DBAsosRule(tag: Tag) extends Table[AsosRule](tag, "asosrule"){
		def daftar = column[List[Int]]("daftar")
		def rule = column[Map[String, String]]("rule")
		def * = (daftar, rule) <> (AsosRule.tupled, AsosRule.unapply)
	}

	class DBSupKon(tag: Tag) extends Table[SupKon](tag, "supkon"){
		def id = column[Int]("id")
		def support = column[Int]("support")
		def konfidensi = column[Double]("konfidensi")
		def * = (id, support, konfidensi) <> (SupKon.tupled, SupKon.unapply)
	}
}
