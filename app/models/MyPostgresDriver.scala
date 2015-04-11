package models

import slick.driver.PostgresDriver
import com.github.tminglei.slickpg._

trait MyPostgresDriver extends PostgresDriver with PgArraySupport with PgHStoreSupport {
	override lazy val Implicit = new ImplicitsPlus {}
	override val simple = new SimpleQLPlus {}

	trait ImplicitsPlus extends Implicits with ArrayImplicits with HStoreImplicits
	trait SimpleQLPlus extends SimpleQL with ImplicitsPlus
}

object MyPostgresDriver extends MyPostgresDriver
