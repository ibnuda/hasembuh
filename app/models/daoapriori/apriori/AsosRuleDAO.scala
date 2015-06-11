package models.daoapriori.apriori

import models.DataRule.AsosRule

trait AsosRuleDAO {
  def save(asosRule: AsosRule)
  def save(listAsosRule: List[AsosRule])
}
