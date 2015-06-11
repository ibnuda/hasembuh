package controllers

import javax.inject.Inject

import apriori.Apriori
import com.mohiva.play.silhouette.api.{Silhouette, Environment}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import models.DataRule.{Akhir2, Akhir, Tampilkan}
import models.User
import models.DataRule.SetBarang
import models.daoapriori.apriori.SetBarangDAOSlick
import models.daoapriori.barang.BarangDAOSlick
import models.daoapriori.SupKonDAOSlick
import play.api.mvc.{AnyContent, Action}

class AprioriController @Inject()(implicit val env: Environment[User, SessionAuthenticator])
	extends Silhouette[User, SessionAuthenticator] {

	val apriori = new Apriori
	val slickSupKon = new SupKonDAOSlick
	val slickSetBarang = new SetBarangDAOSlick
  val slickBarang = new BarangDAOSlick

	def index: Action[AnyContent] = SecuredAction { implicit request =>
		apriori.resetTabel
		slickSetBarang.save(apriori.koleksi1)
		apriori.prune(1)
		val listDua = apriori.tampilkanTampilan(slickSetBarang.lihatKoleksi(1))
		Ok(views.html.aprior(listDua, request.identity))
	}

	def itemset(koleksi: Int): Action[AnyContent] = SecuredAction { implicit request =>
    val simpan: List[SetBarang] = apriori.koleksiN(koleksi)
		println(slickSupKon.getBundle)
		if ((simpan.length < 1) || (koleksi > slickSupKon.getBundle)) {
			Redirect(routes.AprioriController.asosrule())
		} else {
			slickSetBarang.save(simpan)
			Redirect(routes.AprioriController.itemset(koleksi + 1))
			//val tampilkan = apriori.tampilkanTampilan(simpan)
			//Ok(views.html.aprior(tampilkan, request.identity))
		}
	}

	def asosrule: Action[AnyContent] = SecuredAction { implicit request =>
		val rulenya: List[List[Akhir2]] = apriori.hitungRuleList(apriori.daftarUntukRule)
		if (rulenya.length > 0){
			Ok(views.html.rule(rulenya, request.identity))
		} else {
			Ok(views.html.home(request.identity))
		}
	}

}
