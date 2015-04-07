package forms

import play.api.data.Form
import play.api.data.Forms._

object SignUpForm {

	/**
	 * A play framework form.
	 */
	val form = Form(
		mapping(
			"username" -> nonEmptyText(6),
			"password" -> nonEmptyText
		)(Data.apply)(Data.unapply)
	)

	case class Data(
		               username: String,
		               password: String
		               )

}
