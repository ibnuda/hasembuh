package models.daotransaksi

import play.api.db.slick.Config.driver.simple._

object DBTableDefinitions {

	val slickBarang = TableQuery[DBBarang]
	val slickTransak = TableQuery[DBTransaksi]

	case class Barang(idbarang: Int, nabarang: String, habarang: Int)

	case class Transaksi(no: Int, idtrans: Int, idbarang: Int)

	case class ItemsetSatu(id: Int, idbarangsatu: Int, support: Int)

	case class ItemsetDua(id: Int, idbarangsatu: Int, idbarangdua: Int, support: Int)

	case class ItemsetTiga(id: Int, idbarangsatu: Int, idbarangdua: Int, idbarangtiga: Int, support: Int)

	case class ItemsetEmpat(id: Int, idbarangsatu: Int, idbarangdua: Int, idbarangtiga: Int, idbarangempat: Int, support: Int)

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

	class DBItemSatu(tag: Tag) extends Table[ItemsetSatu](tag, "itemset_satu"){
		def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
		def idbarangsatu = column[Int]("idbarangsatu", O.NotNull)
		def support = column[Int]("support")
		def * = (id, idbarangsatu, support) <> (ItemsetSatu.tupled, ItemsetSatu.unapply)
	}

	class DBItemDua(tag: Tag) extends Table[ItemsetDua](tag, "itemset_dua"){
		def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
		def idbarangsatu = column[Int]("idbarangsatu", O.NotNull)
		def idbarangdua = column[Int]("idbarangdua", O.NotNull)
		def support = column[Int]("support")
		def * = (id, idbarangsatu, idbarangdua, support) <> (ItemsetDua.tupled, ItemsetDua.unapply)
	}

	class DBItemTiga(tag: Tag) extends Table[ItemsetTiga](tag, "itemset_tiga"){
		def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
		def idbarangsatu = column[Int]("idbarangsatu", O.NotNull)
		def idbarangdua = column[Int]("idbarangdua", O.NotNull)
		def idbarangtiga = column[Int]("idbarangtiga", O.NotNull)
		def support = column[Int]("support")
		def * = (id, idbarangsatu, idbarangdua, idbarangtiga, support) <> (ItemsetTiga.tupled, ItemsetTiga.unapply)
	}

	class DBItemEmpat(tag: Tag) extends Table[ItemsetEmpat](tag, "itemset_empat"){
		def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
		def idbarangsatu = column[Int]("idbarangsatu", O.NotNull)
		def idbarangdua = column[Int]("idbarangdua", O.NotNull)
		def idbarangtiga = column[Int]("idbarangtiga", O.NotNull)
		def idbarangempat = column[Int]("idbarangempat", O.NotNull)
		def support = column[Int]("support")
		def * = (id, idbarangsatu, idbarangdua, idbarangtiga, idbarangempat, support) <> (ItemsetEmpat.tupled, ItemsetEmpat.unapply)
	}
}
