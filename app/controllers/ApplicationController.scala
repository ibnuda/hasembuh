package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.{Environment, LogoutEvent, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import forms._
import models.User
import models.DataRule.SupKon
import models.daoapriori.transaksi.TransaksiDAOSlick
import play.api.mvc.{AnyContent, Action}

import scala.concurrent.Future
import models.daoapriori.SupKonDAOSlick

class ApplicationController @Inject()(implicit val env: Environment[User, SessionAuthenticator],
                                      val transaksiDaoSlick: TransaksiDAOSlick )
	extends Silhouette[User, SessionAuthenticator] {

	val supKonDAOSlick: SupKonDAOSlick = new SupKonDAOSlick

	def index: Action[AnyContent] = SecuredAction { implicit request =>
		val panjang: Int = supKonDAOSlick.panjang
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
		val nilaiSupKon: SupKon = SupKonForm.form.bindFromRequest.get
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

}
