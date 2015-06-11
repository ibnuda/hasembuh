package controllers

import java.io.File
import javax.inject.Inject

import apriori.Eksel
import com.mohiva.play.silhouette.api.{Silhouette, Environment}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import forms.TransaksiForm
import models.User
import models.DataRule.Transaksi
import models.daoapriori.barang.BarangDAOSlick
import models.daoapriori.transaksi.TransaksiDAOSlick
import play.api.libs.Files.TemporaryFile
import play.api.mvc.{MultipartFormData, AnyContent, Action}

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

	def formUpload(): Action[AnyContent] = SecuredAction { implicit request =>
		Ok(views.html.formUpload(request.identity))
	}

	def simpanUpload(): Action[MultipartFormData[TemporaryFile]] = SecuredAction(parse.multipartFormData) { implicit request =>
		request.body.file("eksel").map { eksel =>
			val anu = new Eksel
			val namaFile = eksel.filename
			eksel.ref.moveTo(new File(s"/tmp/$namaFile"))
			val listTransaksi: List[Transaksi] = anu.berkasToTransaksi(new File(s"/tmp/$namaFile"))
			transaksiDAOSlick.save(listTransaksi)
			Redirect(routes.TransaksiController.index())
		}.getOrElse{
			println("gagal unggah")
			Redirect(routes.TransaksiController.formUpload())
		}
	}

}
