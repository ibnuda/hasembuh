package models.daoapriori

import play.api.db.slick.DB
import models.MyPostgresDriver.simple._

import scala.slick.lifted.{ForeignKeyQuery, ProvenShape}

object DBTableDefinitions {

	val slickBarang = TableQuery[DBBarang]
	val slickTransak = TableQuery[DBTransaksi]
	val slickSetBarang = TableQuery[DBSetBarang]
	val slickAsosRule = TableQuery[DBAsosRule]
	val slickSupKon = TableQuery[DBSupKon]

	case class Barang(idbarang: Int, nabarang: String, habarang: Int)

	case class Transaksi(no: Int, idtrans: Int, idbarang: Int)

	case class SetBarang(daftar: List[Int], koleksi: Int, support: Int)

	case class AsosRule(daftar: List[Int], rule: Map[String, String], konfidensi: Double)

	case class SupKon(id: Int, bundle: Int, support: Int, konfidensi: Double)

	class DBBarang(tag: Tag) extends Table[Barang](tag, "barang") {
		def idbarang: Column[Int] = column[Int]("idbarang", O.PrimaryKey, O.AutoInc)
		def nabarang: Column[String] = column[String]("nabarang", O.NotNull)
		def habarang: Column[Int] = column[Int]("habarang", O.NotNull)
		def * : ProvenShape[Barang] = (idbarang, nabarang, habarang) <>(Barang.tupled, Barang.unapply)
	}

	class DBTransaksi(tag: Tag) extends Table[Transaksi](tag, "transaksi") {
		def no: Column[Int] = column[Int]("no", O.PrimaryKey, O.AutoInc)
		def idtrans: Column[Int] = column[Int]("idtrans")
		def idbarang: Column[Int] = column[Int]("idbarang")
		def baran: ForeignKeyQuery[DBBarang, Barang] = foreignKey("idbarang", idbarang, slickBarang)(_.idbarang)
		def * : ProvenShape[Transaksi] = (no, idtrans, idbarang) <>(Transaksi.tupled, Transaksi.unapply)
	}

	class DBSetBarang(tag: Tag) extends Table[SetBarang](tag, "itemset"){
		def daftar: Column[List[Int]] = column[List[Int]]("daftar")
		def koleksi: Column[Int] = column[Int]("koleksi")
		def support: Column[Int] = column[Int]("support")
		def * : ProvenShape[SetBarang] = (daftar, koleksi, support) <> (SetBarang.tupled, SetBarang.unapply)
	}

	class DBAsosRule(tag: Tag) extends Table[AsosRule](tag, "asosrule"){
		def daftar: Column[List[Int]] = column[List[Int]]("daftar")
		def rule: Column[Map[String, String]] = column[Map[String, String]]("rule")
		def konfidensi: Column[Double] = column[Double]("konfid")
		def * : ProvenShape[AsosRule] = (daftar, rule, konfidensi) <> (AsosRule.tupled, AsosRule.unapply)
	}

	class DBSupKon(tag: Tag) extends Table[SupKon](tag, "supkon"){
		def id: Column[Int] = column[Int]("id")
		def bundle: Column[Int] = column[Int]("bundle")
		def support: Column[Int] = column[Int]("support")
		def konfidensi: Column[Double] = column[Double]("konfidensi")
		def * : ProvenShape[SupKon] = (id, bundle, support, konfidensi) <> (SupKon.tupled, SupKon.unapply)
	}
}
