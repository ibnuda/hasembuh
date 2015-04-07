package models

import java.util.UUID

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}

case class User(
	               userID: UUID,
	               loginInfo: LoginInfo,
	               username: String) extends Identity
