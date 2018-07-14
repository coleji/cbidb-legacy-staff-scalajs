package org.sailcbi.Pages.HomePage

import org.sailcbi.CbiUtil.Currency
import org.sailcbi.Core.Model

case class HomePageModel(
  discountedPrice: Option[Currency]
) extends Model