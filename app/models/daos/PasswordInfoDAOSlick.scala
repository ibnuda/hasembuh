package models.daos

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO
import models.daos.DBTableDefinitions._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._

import scala.concurrent.Future

class PasswordInfoDAOSlick extends DelegableAuthInfoDAO[PasswordInfo] {

	import play.api.Play.current

	def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
		Future.successful {
			DB withSession { implicit session =>
				val infoId = slickLoginInfos.filter(
					x => x.providerID === loginInfo.providerID && x.providerKey === loginInfo.providerKey
				).first.id.get
				slickPasswordInfo insert DBPasswordInfo(authInfo.hasher, authInfo.password, authInfo.salt, infoId)
				authInfo
			}
		}
	}

	def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
		Future.successful {
			DB withSession { implicit session =>
				slickLoginInfos.filter(
					info => info.providerID === loginInfo.providerID && info.providerKey === loginInfo.providerKey
				).firstOption match {
					case Some(info) => {
						val passwordInfo = slickPasswordInfo.filter(_.loginInfoId === info.id).first
						Some(PasswordInfo(passwordInfo.hasher, passwordInfo.password, passwordInfo.salt))
					}
					case None => None
				}
			}
		}
	}
}
