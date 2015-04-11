package forms

import play.api.data.Form
import play.api.data.Forms._


object AprioriForm {
	val form = Form (
		mapping(
			"minimumSupport" -> number,
			"konfidensi" -> number
		)(Data.apply)(Data.unapply)
	)

	case class Data(minimumSupport: Int, konfidensi: Int)
}
