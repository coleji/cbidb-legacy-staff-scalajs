package org.sailcbi.ApiEndpointFacade

import org.sailcbi.Core.{ApiClassFacade, ApiClassFacadeObject, ApiResult, RowObject}

import scala.scalajs.js

case class User (
  userId: Int,
  userName: String,
  firstName: String,
  lastName: String,
  email: String,
  active: Boolean,
  hideFromClose: Boolean
) extends ApiClassFacade

object User extends  ApiClassFacadeObject[User] {
  val endpoint: String = "users"
  val fields: Array[(String, String, Class[_])] = Array(
    ("USER_ID", "User ID", classOf[Int]),
    ("USER_NAME", "User Name", classOf[String]),
    ("NAME_FIRST", "First Name", classOf[String]),
    ("NAME_LAST", "Last Name", classOf[String]),
    ("EMAIL", "Email", classOf[String]),
    ("ACTIVE", "Active", classOf[Boolean]),
    ("HIDE_FROM_CLOSE", "Hide From Close", classOf[Boolean])
  )
  def parse(r: ApiResult): js.Array[User] = {
    def getIndexOfField(fieldName: String): Int = r.data.metaData.indexWhere(_.name == fieldName)
    val parseSingle: (RowObject => User) = {
      val indexes: Array[Int] = fields.map(f => getIndexOfField(f._1))
      (row: RowObject) => {
        println("%%%")
        println(row)
        println(row(indexes(0)))
        println(row(indexes(0)).getClass.getName)
        val s: String = row(indexes(0)).asInstanceOf[String]
        println(s)
        val i: Int = s.toInt
        println(i)
        println("yay")
        User(
          row(indexes(0)).toString.toInt,
          row(indexes(1)).toString,
          row(indexes(2)).toString,
          row(indexes(3)).toString,
          row(indexes(4)).toString,
          row(indexes(5)).toString match {
            case "Y" => true
            case _ => false
          },
          row(indexes(6)).toString match {
            case "Y" => true
            case _ => false
          }
        )
      }
    }
    r.data.rows.map(parseSingle)
  }
}
