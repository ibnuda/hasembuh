package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.{Environment, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import forms.BarangForm
import models.User
import models.daoapriori.BarangDAOSlick

import scala.concurrent.Future

class BarangController @Inject()(
	implicit val env: Environment[User, SessionAuthenticator],
	val barangdaoslick: BarangDAOSlick )
	extends Silhouette[User, SessionAuthenticator] {

	def index = SecuredAction.async { implicit request =>
		Future.successful(Ok(views.html.barang(barangdaoslick.all, request.identity)))
	}

	def simpan = SecuredAction.async { implicit request =>
		val barang = BarangForm.form.bindFromRequest.get
		barangdaoslick.save(barang)
		Future.successful(Redirect(routes.BarangController.index()))
	}

	def tambah() = SecuredAction { implicit request =>
		Ok(views.html.tambahBarang(BarangForm.form, request.identity))
	}
}
