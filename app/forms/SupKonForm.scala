package forms

import models.DataRule.SupKon
import play.api.data.format.Formats._
import play.api.data.Form
import play.api.data.Forms._


object SupKonForm {
	val form = Form (
		mapping(
			"id" -> ignored(0),
			"bundle" -> number,
			"support" -> number,
			"konfidensi" -> of(doubleFormat)
		)(SupKon.apply)(SupKon.unapply)
	)
}
