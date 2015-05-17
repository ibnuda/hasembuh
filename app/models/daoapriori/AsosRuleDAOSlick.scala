package models.daoapriori

import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._
import models.daoapriori.DBTableDefinitions.{AsosRule, slickAsosRule}

/**
 * Created by mel on 5/17/15.
 */

class AsosRuleDAOSlick extends AsosRuleDAO {
  import play.api.Play.current

  def save(asosRule: AsosRule) = {
    DB withTransaction { implicit session =>
      slickAsosRule.insert(asosRule)
    }
  }

  def save(listAsosRule: List[AsosRule]) = {
    for (asos <- listAsosRule) save(asos)
  }
}
