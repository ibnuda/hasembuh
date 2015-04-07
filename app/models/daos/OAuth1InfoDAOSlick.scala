package models.daos

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import com.mohiva.play.silhouette.impl.providers.OAuth1Info
import models.daos.DBTableDefinitions._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._

import scala.concurrent.Future

class OAuth1InfoDAOSlick extends DelegableAuthInfoDAO[OAuth1Info] {

	import play.api.Play.current

	def save(loginInfo: LoginInfo, authInfo: OAuth1Info): Future[OAuth1Info] = {
		Future.successful(
			DB withSession { implicit session =>
				val infoId = slickLoginInfos.filter(
					x => x.providerID === loginInfo.providerID && x.providerKey === loginInfo.providerKey
				).first.id.get
				slickOAuth1Infos.filter(_.loginInfoId === infoId).firstOption match {
					case Some(info) =>
						slickOAuth1Infos update DBOAuth1Info(info.id, authInfo.token, authInfo.secret, infoId)
					case None =>
						slickOAuth1Infos insert DBOAuth1Info(123123, authInfo.token, authInfo.secret, infoId)
				}
				authInfo
			}
		)
	}

	def find(loginInfo: LoginInfo): Future[Option[OAuth1Info]] = {
		Future.successful(
			DB withSession { implicit session =>
				slickLoginInfos.filter(info => info.providerID === loginInfo.providerID && info.providerKey === loginInfo.providerKey).firstOption match {
					case Some(info) =>
						val oAuth1Info = slickOAuth1Infos.filter(_.loginInfoId === info.id).first
						Some(OAuth1Info(oAuth1Info.token, oAuth1Info.secret))
					case None => None
				}
			}
		)
	}

}

