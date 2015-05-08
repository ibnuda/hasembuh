package controllers

import javax.inject.Inject

import apriori.ExIm
import com.mohiva.play.silhouette.api.{Environment, LogoutEvent, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import forms._
import models.User
import play.api.libs.Files.TemporaryFile
import play.api.mvc.{AnyContent, MultipartFormData, Action}

import scala.concurrent.Future
import models.daoapriori.SupKonDAOSlick

class ApplicationController @Inject()(implicit val env: Environment[User, SessionAuthenticator])
	extends Silhouette[User, SessionAuthenticator] {

	val supKonDAOSlick = new SupKonDAOSlick

	def index: Action[AnyContent] = SecuredAction { implicit request =>
		val panjang = supKonDAOSlick.panjang
		println(panjang)
		if (panjang > 0) {
			Ok(views.html.supKon(supKonDAOSlick.all, request.identity))
		} else {
			Redirect(routes.ApplicationController.tambahSupKon)
		}
	}

	def tambahSupKon: Action[AnyContent] = SecuredAction { implicit request =>
		Ok(views.html.aturSupKon(SupKonForm.form, request.identity))
	}

	def simpanSupKon: Action[AnyContent] = SecuredAction { implicit request =>
		val nilaiSupKon = SupKonForm.form.bindFromRequest.get
		supKonDAOSlick.save(nilaiSupKon)
		Ok(views.html.supKon(supKonDAOSlick.all, request.identity))
	}

	def deleteSupKon(sup: Int): Action[AnyContent] = SecuredAction { implicit request =>
		supKonDAOSlick.delete(sup)
		Redirect(routes.ApplicationController.index)
	}

	def signIn: Action[AnyContent] = UserAwareAction.async { implicit request =>
		request.identity match {
			case Some(user) => Future.successful(Redirect(routes.ApplicationController.index()))
			case None => Future.successful(Ok(views.html.signIn(SignInForm.form)))
		}
	}

	def signUp: Action[AnyContent] = UserAwareAction.async { implicit request =>
		request.identity match {
			case Some(user) => Future.successful(Redirect(routes.ApplicationController.index()))
			case None => Future.successful(Ok(views.html.signUp(SignUpForm.form)))
		}
	}

	def signOut: Action[AnyContent] = SecuredAction.async { implicit request =>
		val result = Future.successful(Redirect(routes.ApplicationController.index()))
		env.eventBus.publish(LogoutEvent(request.identity, request, request2lang))

		request.authenticator.discard(result)
	}

	def formUpload(): Action[AnyContent] = SecuredAction { implicit request =>
		Ok(views.html.formUpload(request.identity))
	}

	def simpanUpload(): Action[MultipartFormData[TemporaryFile]] = SecuredAction(parse.multipartFormData) { implicit request =>
		request.body.file("eksel").map { eksel =>
			import java.io.File
			val namaFile = eksel.filename
			val tipeKonten = eksel.contentType
			eksel.ref.moveTo(new File(s"/tmp/$namaFile"))
			ExIm.importExcel(namaFile)
			Ok(views.html.home(request.identity))
			//Ok("berkas terunggah")
		}.getOrElse{
			println("gagal unggah")
			Redirect(routes.ApplicationController.formUpload())
		}
	}
}
