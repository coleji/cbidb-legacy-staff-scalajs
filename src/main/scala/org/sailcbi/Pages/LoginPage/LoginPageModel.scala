package org.sailcbi.Pages.LoginPage

import org.sailcbi.CbiUtil.Currency
import org.sailcbi.Core.Model

case class LoginPageModel(
  path: String,
  userName: Option[String],
  password: Option[String],
  jpPrice: Currency,
  lastSeason: Int,
  loginRequestPending: Boolean
) extends Model