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
		Ok(views.html.aprior(listDua, request.identity))
	}

	def itemset(koleksi: Int) = SecuredAction { implicit request =>
		setbarang.save(apriori.koleksiN(koleksi))
		Ok(views.html.aprior(apriori.koleksiN(koleksi), request.identity))
	}

	def asosrule = SecuredAction { implicit request =>
		val rulenya = apriori.hitungRuleList(apriori.daftarUntukRule)
		if (rulenya.length > 0){
			Ok(views.html.rule(rulenya, request.identity))
		} else {
			Ok(views.html.home(request.identity))
		}
	}

}
