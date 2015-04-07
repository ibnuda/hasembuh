package models.daos

import play.api.db.slick.Config.driver.simple._

object DBTableDefinitions {
	val slickUsers = TableQuery[Users]
	val slickLoginInfos = TableQuery[LoginInfos]
	val slickUserLoginInfos = TableQuery[UserLoginInfos]
	val slickPasswordInfo = TableQuery[PasswordInfos]
	val slickOAuth1Infos = TableQuery[OAuth1Infos]
	val slickOAuth2Infos = TableQuery[OAuth2Infos]

	case class DBUser(
		                 userID: String,
		                 username: String
		                 )

	class Users(tag: Tag) extends Table[DBUser](tag, "useruser") {
		def * = (id, username) <>(DBUser.tupled, DBUser.unapply)

		def id = column[String]("userID", O.PrimaryKey)

		def username = column[String]("username")
	}

	case class DBLoginInfo(
		                      id: Option[Long],
		                      providerId: String,
		                      providerKey: String
		                      )

	class LoginInfos(tag: Tag) extends Table[DBLoginInfo](tag, "logininfo") {
		def * = (id.?, providerID, providerKey) <>(DBLoginInfo.tupled, DBLoginInfo.unapply)

		def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

		def providerID = column[String]("providerID")

		def providerKey = column[String]("providerKey")
	}

	case class DBUserLoginInfo(
		                          userID: String,
		                          loginInfoId: Long
		                          )

	class UserLoginInfos(tag: Tag) extends Table[DBUserLoginInfo](tag, "userlogininfo") {
		def * = (userID, loginInfoId) <>(DBUserLoginInfo.tupled, DBUserLoginInfo.unapply)

		def userID = column[String]("userID", O.NotNull)

		def loginInfoId = column[Long]("loginInfoId")
	}

	case class DBPasswordInfo(
		                         hasher: String,
		                         password: String,
		                         salt: Option[String],
		                         loginInfoId: Long
		                         )

	class PasswordInfos(tag: Tag) extends Table[DBPasswordInfo](tag, "passwordinfo") {
		def * = (hasher, password, salt, loginInfoId) <>(DBPasswordInfo.tupled, DBPasswordInfo.unapply)

		def hasher = column[String]("hasher")

		def password = column[String]("password")

		def salt = column[Option[String]]("salt")

		def loginInfoId = column[Long]("loginInfoId")
	}

	case class DBOAuth1Info(
		                       id: Long,
		                       token: String,
		                       secret: String,
		                       loginInfoId: Long
		                       )

	class OAuth1Infos(tag: Tag) extends Table[DBOAuth1Info](tag, "oauth1info") {
		def * = (id, token, secret, loginInfoId) <>(DBOAuth1Info.tupled, DBOAuth1Info.unapply)

		def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

		def token = column[String]("token")

		def secret = column[String]("secret")

		def loginInfoId = column[Long]("loginInfoId")
	}

	case class DBOAuth2Info(
		                       id: Long,
		                       accessToken: String,
		                       tokenType: Option[String],
		                       expiresIn: Option[Int],
		                       refreshToken: Option[String],
		                       loginInfoId: Long
		                       )

	class OAuth2Infos(tag: Tag) extends Table[DBOAuth2Info](tag, "oauth2info") {
		def * = (id, accessToken, tokenType, expiresIn, refreshToken, loginInfoId) <>(DBOAuth2Info.tupled, DBOAuth2Info.unapply)

		def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

		def accessToken = column[String]("accesstoken")

		def tokenType = column[Option[String]]("tokentype")

		def expiresIn = column[Option[Int]]("expiresin")

		def refreshToken = column[Option[String]]("refreshtoken")

		def loginInfoId = column[Long]("logininfoid")
	}

}
