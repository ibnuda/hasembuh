package controllers

import java.io.File
import javax.inject.Inject

import apriori.Eksel
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

	def upload() = SecuredAction { implicit request =>
		Ok(views.html.formUploadBarang(request.identity))
	}

	def simpanUpload() = SecuredAction(parse.multipartFormData) { implicit request =>
		request.body.file("eksel").map { eksel =>
			val anu = new Eksel
			val namaFile = eksel.filename
			eksel.ref.moveTo(new File(s"/tmp/$namaFile"))
			val listBarang = anu.berkasToBarang(new File(s"/tmp/$namaFile"))
			barangdaoslick.save(listBarang)
			Redirect(routes.BarangController.index())
		}.getOrElse {
			Redirect(routes.BarangController.upload)
		}
	}
}
