package forms

import models.daoapriori.DBTableDefinitions.SupKon
import play.api.data.format.Formats._
import play.api.data.Form
import play.api.data.Forms._


object SupKonForm {
	val form = Form (
		mapping(
			"id" -> ignored(0),
			"support" -> number,
			"konfidensi" -> of(doubleFormat)
		)(SupKon.apply)(SupKon.unapply)
	)
}
