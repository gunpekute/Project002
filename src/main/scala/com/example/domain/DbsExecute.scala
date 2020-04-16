package com.example.domain

import java.time.LocalDate
import java.util.Date

import org.mongodb.scala.{Document, MongoClient}
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.model.Updates
import Helpers2._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class DbsExecute {
  val mongoClient =  MongoClient("mongodb+srv://admin:gunpekute779@cluster0-uryix.mongodb.net/test?retryWrites=true&w=majority")
  val database = mongoClient.getDatabase("db1")
  val nodes = database.getCollection("user")
  val waitDuration = Duration(10, "seconds")

  def checkLogin(username: String, password: String) = {
    val result = nodes.find(and(equal("username", username),equal("password", password))).first()
    val res = Await.result(result.toFuture(), waitDuration)
    res
  }

  def loginDay(username: String, loginDay: LocalDate) = {
    nodes.findOneAndUpdate(equal("username", username),Updates.set("loginDay", loginDay)).printHeadResult()
  }

  def updateInfor(username: String, password: String, fullname: String, birthday: String, sex: String, phonenumber: String ) = {
    nodes.findOneAndUpdate(equal("username", username),Updates.set("password", password)).printHeadResult()
    nodes.findOneAndUpdate(equal("username", username),Updates.set("fullname", fullname)).printHeadResult()
    nodes.findOneAndUpdate(equal("username", username),Updates.set("birthday", birthday)).printHeadResult()
    nodes.findOneAndUpdate(equal("username", username),Updates.set("sex", sex)).printHeadResult()
    nodes.findOneAndUpdate(equal("username", username),Updates.set("phonenumber", phonenumber)).printHeadResult()
  }

  def checkExist(username: String) = {
    val result = nodes.find(equal("username", username)).first()
    val res = Await.result(result.toFuture(), waitDuration)
    res
  }

  def register(username: String, password: String, fullname: String, birthday: String, sex: String, phonenumber: String) = {
    var now : LocalDate = LocalDate.now()
    println(now)
    val doc: Document = Document("username" -> username,
      "password" -> password, "admin" -> false,"fullname" -> fullname,
      "birthday" -> birthday,"sex" -> sex,"phonenumber" -> phonenumber, "active" -> true
    )
    val result = nodes.insertOne(doc).results()
    nodes.findOneAndUpdate(equal("username", username),Updates.set("registerDay", now)).printHeadResult()
  }

  def listUser() = {
    val result = nodes.find(equal("admin", false))
    val res = Await.result(result.toFuture(), waitDuration)
    res
  }

  def activeUser(lstUserActive: String) = {
    nodes.updateMany(equal("admin", false),Updates.set("active", false)).printResults()
    var activeUser = lstUserActive.split(",")
    activeUser.foreach(user => {
      nodes.updateOne(equal("username", user),Updates.set("active", true)).printResults()
    })
  }

  def userLog(action: String) = {
    var now : LocalDate = LocalDate.now()
    val todayAction = nodes.find(equal(action, now))
    val userAction = Await.result(todayAction.toFuture(), waitDuration)
    var actionArr : Array[String] = Array()
    userAction.foreach(action => {
      actionArr =  actionArr :+ action.getString("username")
    })
    actionArr
  }


}
