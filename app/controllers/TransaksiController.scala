package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.{Silhouette, Environment}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import forms.TransaksiForm
import models.User
import models.daotransaksi.TransaksiDAOSlick
import models.daotransaksi.BarangDAOSlick

import scala.concurrent.Future

class TransaksiController @Inject()(
	implicit val env: Environment[User, SessionAuthenticator],
	val barangDAOSlick: BarangDAOSlick,
	val transaksiDAOSlick: TransaksiDAOSlick)
	extends Silhouette[User, SessionAuthenticator] {

	def index = SecuredAction.async { implicit request =>
		Future.successful(Ok(views.html.transaksi(transaksiDAOSlick.allPlusNama, request.identity)))
	}

	def tambah() = SecuredAction.async { implicit request =>
		Future.successful(
			Ok(views.html.tambahTransaksi(TransaksiForm.form, barangDAOSlick, request.identity))
		)
	}

	def simpan = SecuredAction.async { implicit request =>
		val transaksiSimpan = TransaksiForm.form.bindFromRequest.get
		println(transaksiSimpan)
		transaksiDAOSlick.save(transaksiSimpan)
		Future.successful(Redirect(routes.TransaksiController.index))
	}
}
