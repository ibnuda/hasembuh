package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.api.{Environment, LogoutEvent, Silhouette}
import com.mohiva.play.silhouette.impl.authenticators.SessionAuthenticator
import forms._
import models.User

import scala.concurrent.Future
import models.daoapriori.SupKonDAOSlick

/**
 * The basic application controller.
 *
 * @param env The Silhouette environment.
 */
class ApplicationController @Inject()(implicit val env: Environment[User, SessionAuthenticator])
	extends Silhouette[User, SessionAuthenticator] {

	val supKonDAOSlick = new SupKonDAOSlick

	/**
	 * Handles the index action.
	 *
	 * @return The result to display.
	def index = SecuredAction.async { implicit request =>
		Future.successful(Ok(views.html.home(request.identity)))
	}
	 */

	def index = SecuredAction { implicit request =>
		val panjang = supKonDAOSlick.panjang
		println(panjang)
		if (panjang > 0) {
			Ok(views.html.supKon(supKonDAOSlick.all, request.identity))
		} else {
			Redirect(routes.ApplicationController.tambahSupKon)
		}
	}

	def tambahSupKon = SecuredAction { implicit request =>
		Ok(views.html.aturSupKon(SupKonForm.form, request.identity))
	}

	def simpanSupKon = SecuredAction { implicit request =>
		val nilaiSupKon = SupKonForm.form.bindFromRequest.get
		supKonDAOSlick.save(nilaiSupKon)
		Ok(views.html.supKon(supKonDAOSlick.all, request.identity))
	}

	def deleteSupKon(sup: Int) = SecuredAction { implicit request =>
		supKonDAOSlick.delete(sup)
		Redirect(routes.ApplicationController.index)
	}

	def signIn = UserAwareAction.async { implicit request =>
		request.identity match {
			case Some(user) => Future.successful(Redirect(routes.ApplicationController.index()))
			case None => Future.successful(Ok(views.html.signIn(SignInForm.form)))
		}
	}

	def signUp = UserAwareAction.async { implicit request =>
		request.identity match {
			case Some(user) => Future.successful(Redirect(routes.ApplicationController.index()))
			case None => Future.successful(Ok(views.html.signUp(SignUpForm.form)))
		}
	}

	def signOut = SecuredAction.async { implicit request =>
		val result = Future.successful(Redirect(routes.ApplicationController.index()))
		env.eventBus.publish(LogoutEvent(request.identity, request, request2lang))

		request.authenticator.discard(result)
	}

}
