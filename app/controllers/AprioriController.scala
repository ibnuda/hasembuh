package controllers

import javax.inject.Inject

import apriori.Apriori
import com.mohiva.play.silhouette.api.{Silhouette, Environment}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import models.DataRule.Tampilkan
import models.User
import models.daoapriori.DBTableDefinitions.SetBarang
import models.daoapriori.SetBarangDAOSlick
import models.daoapriori.BarangDAOSlick

import scala.concurrent.Future

class AprioriController @Inject()(implicit val env: Environment[User, SessionAuthenticator])
	extends Silhouette[User, SessionAuthenticator] {

	val apriori = new Apriori
	val slickSetBarang = new SetBarangDAOSlick
  val slickBarang = new BarangDAOSlick

	def index = SecuredAction { implicit request =>
		apriori.resetTabel
		slickSetBarang.save(apriori.koleksi1)
		apriori.prune(1)
		val listDua = apriori.tampilkanTampilan(slickSetBarang.lihatKoleksi(1))
		Ok(views.html.aprior(listDua, request.identity))
	}

	def itemset(koleksi: Int) = SecuredAction { implicit request =>
    val simpan: List[SetBarang] = apriori.koleksiN(koleksi)
		slickSetBarang.save(simpan)
    val tampilkan = apriori.tampilkanTampilan(simpan)
    Ok(views.html.aprior(tampilkan, request.identity))
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
