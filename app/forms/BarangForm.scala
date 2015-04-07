package forms

import models.daotransaksi.DBTableDefinitions.Barang
import play.api.data.Form
import play.api.data.Forms._

object BarangForm {
	val form = Form(
		mapping(
			"idBarang" -> ignored(0),
			"namaBarang" -> nonEmptyText,
			"hargaBarang" -> number
		)(Barang.apply)(Barang.unapply)
	)
}
