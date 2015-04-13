package controllers

import javax.inject.Inject

import apriori.Apriori
import com.mohiva.play.silhouette.api.{Silhouette, Environment}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import models.User
import models.daoapriori.SetBarangDAOSlick

import scala.concurrent.Future

class AprioriController @Inject()(implicit val env: Environment[User, SessionAuthenticator])
	extends Silhouette[User, SessionAuthenticator] {

	val apriori = new Apriori
	val setbarang = new SetBarangDAOSlick

	def index = SecuredAction { implicit request =>
		apriori.resetTabel
		setbarang.save(apriori.koleksi1)
		apriori.prune(1)
		val listDua = setbarang.lihatKoleksi(1)
		Ok(views.html.apriori(listDua, request.identity))
	}

	def itemset(koleksi: Int) = SecuredAction { implicit request =>
		setbarang.save(apriori.koleksiN(koleksi))
		Ok(views.html.apriori(apriori.koleksiN(koleksi), request.identity))
	}

}
