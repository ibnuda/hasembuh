package forms

import models.daotransaksi.DBTableDefinitions.Transaksi
import play.api.data.Form
import play.api.data.Forms._

object TransaksiForm {
	val form = Form(
		mapping(
			"notransaksi" -> ignored(0),
			"idtransaksi" -> number,
			"barangbarang" -> number
		)(Transaksi.apply)(Transaksi.unapply)
	)
}
