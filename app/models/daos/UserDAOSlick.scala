package models.daos

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.User
import models.daos.DBTableDefinitions._
import play.Logger
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._

import scala.concurrent.Future


class UserDAOSlick extends UserDAO {

	import play.api.Play.current

	def find(loginInfo: LoginInfo) = {
		DB withSession { implicit session =>
			Future.successful {
				slickLoginInfos.filter(
					x => x.providerID === loginInfo.providerID && x.providerKey === loginInfo.providerKey
				).firstOption match {
					case Some(info) =>
						slickUserLoginInfos.filter(_.loginInfoId === info.id).firstOption match {
							case Some(userLoginInfo) =>
								slickUsers.filter(_.id === userLoginInfo.userID).firstOption match {
									case Some(user) =>
										Some(User(UUID.fromString(user.userID), loginInfo, user.username))
									case None => None
								}
							case None => None
						}
					case None => None
				}
			}
		}
	}

	def find(userID: UUID) = {
		DB withSession { implicit session =>
			Future.successful {
				slickUsers.filter(_.id === userID.toString).firstOption match {
					case Some(user) =>
						Logger.debug("mencari user: : " + user.username)
						slickUserLoginInfos.filter(_.userID === user.userID).firstOption match {
							case Some(info) =>
								slickLoginInfos.filter(_.id === info.loginInfoId).firstOption match {
									case Some(loginInfo) =>
										Some(User(UUID.fromString(user.userID), LoginInfo(loginInfo.providerId, loginInfo.providerKey), user.username))
									case None => {
										Logger.debug("tidak ketemu loginInfo")
										None
									}
								}
							case None => {
								Logger.debug("tidak ketemu info")
								None
							}
						}
					case None => {
						Logger.debug("tidak ketemu user pada baris 60")
						None
					}
				}
			}
		}
	}

	def save(user: User) = {
		DB withSession { implicit session =>
			Future.successful {
				val dbUser = DBUser(user.userID.toString, user.username)
				slickUsers.filter(_.id === dbUser.userID).firstOption match {
					case Some(userFound) => {
						Logger.debug("ketemu user " + userFound.username)
						slickUsers.filter(_.id === dbUser.userID).update(userFound)
					}
					case None => {
						Logger.debug("tidak ketemu user")
						Logger.debug("UDAOS 79. inserting to ke db useruser")
						Logger.debug("UDAOS 80. userID : " + dbUser.userID.toString)
						Logger.debug("UDAOS 81. username : " + dbUser.username)
						slickUsers.insert(dbUser)
					}
				}
				var dBLoginInfo = DBLoginInfo(None, user.loginInfo.providerID, user.loginInfo.providerKey)
				slickLoginInfos.filter(
					info => info.providerID === dBLoginInfo.providerId &&
						info.providerKey === dBLoginInfo.providerKey
				).firstOption match {
					case None => {
						Logger.debug("inserting dBLoginInfo provider ID " + dBLoginInfo.providerId)
						slickLoginInfos.insert(dBLoginInfo)
					}
					case Some(info) => Logger.debug("sudah ada " + info)
				}
				dBLoginInfo = slickLoginInfos.filter(
					info => info.providerID === dBLoginInfo.providerId &&
						info.providerKey === dBLoginInfo.providerKey
				).first
				slickUserLoginInfos.filter(
					info => info.userID === dbUser.userID && info.loginInfoId === dBLoginInfo.id
				).firstOption match {
					case None => {
						Logger.debug("masukkan user id " + dbUser.userID + " dan username " + dbUser.username)
						slickUserLoginInfos += DBUserLoginInfo(dbUser.userID, dBLoginInfo.id.get)
					}
					case Some(info) =>
				}
				user
			}

		}
	}
}
