package controllers

import javax.inject.Inject

import apriori.Apriori
import com.mohiva.play.silhouette.api.{Silhouette, Environment}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import models.User

import scala.concurrent.Future

class AprioriController @Inject()(implicit val env: Environment[User, SessionAuthenticator])
	extends Silhouette[User, SessionAuthenticator] {

	def apriori = new Apriori

	def index = SecuredAction.async { implicit request =>
		// TODO : menampilkan daftar barang di transaksi dan jumlahnya.
		Future.successful(
			Ok(views.html.apriori(apriori.koleksi1, request.identity))
		)
	}
}
