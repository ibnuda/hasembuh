package models.daoapriori

import models.daoapriori.DBTableDefinitions.AsosRule

trait AsosRuleDAO {
  def save(asosRule: AsosRule)
  def save(listAsosRule: List[AsosRule])
}
