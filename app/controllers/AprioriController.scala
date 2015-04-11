package controllers

import javax.inject.Inject

import apriori.Apriori
import com.mohiva.play.silhouette.api.{Silhouette, Environment}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import models.User
import models.daotransaksi.SetBarangDAOSlick

import scala.concurrent.Future

class AprioriController @Inject()(implicit val env: Environment[User, SessionAuthenticator])
	extends Silhouette[User, SessionAuthenticator] {

	val apriori = new Apriori
	val setbarang = new SetBarangDAOSlick

	def index = SecuredAction { implicit request =>
		// TODO : menampilkan daftar barang di transaksi dan jumlahnya.
		apriori.resetTabel
		val listBarang = apriori.koleksi1
		setbarang.save(listBarang)
		Ok(views.html.apriori(listBarang, request.identity))
	}
}
