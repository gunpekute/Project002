package com.example.app.controller
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, RequestParam, ResponseBody}
import Helpers2._
import java.util.{Calendar, Date}
import java.text.SimpleDateFormat
import java.time.{LocalDate, LocalTime}
import java.util

import com.mongodb.BasicDBObject
import org.mongodb.scala._
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates

import scala.concurrent.Await
import scala.concurrent.duration.Duration


@Controller
class TestCtrl {
  @GetMapping(Array("/login"))
  @ResponseBody
  def demo={
    "<html>" +
      "<body>" +
      "<h1>Log in</h1>" +
        "<form action=\"save\">" +
          "<label>Username:</label> <br>" +
            "<input type=\"text\" id=\"username\" name=\"username\"> <br>" +
          "<label>Password:</label> <br>" +
            "<input type=\"text\" id=\"password\" name=\"password\"> <br>" +
          "<input type=\"submit\" value=\"Submit\"> <br>" +
        "</form>"+
      "<form action=\"register\">" +
      "<input type=\"submit\" value=\"Register\">" +
      "</form>"+
      "</body>" +
    "</html>"
  }

  @GetMapping(Array("/save"))
  @ResponseBody
  def test(@RequestParam("username") username : String, @RequestParam("password") password : String):String ={
    val mongoClient =  MongoClient("mongodb+srv://admin:gunpekute779@cluster0-uryix.mongodb.net/test?retryWrites=true&w=majority")
    val database = mongoClient.getDatabase("db1")
    val nodes = database.getCollection("user")
    val result = nodes.find(and(equal("username", username),equal("password", password))).first()
    val waitDuration = Duration(10, "seconds")
    Thread.sleep(1)
    val res = Await.result(result.toFuture(), waitDuration)
    if (res == null) {
      "<html>" +
        "<body>" +
        "<h1>Log in</h1>" +
        "<form action=\"save\">" +
        "<label>Username:</label> <br>" +
        "<input type=\"text\" id=\"username\" name=\"username\"> <br>" +
        "<label>Password:</label> <br>" +
        "<input type=\"text\" id=\"password\" name=\"password\"> <br>" +
        "<input type=\"submit\" value=\"Submit\">" +
        "</form>"+
        "<form action=\"register\">" +
        "<input type=\"submit\" value=\"Register\">" +
        "</form>"+
        "</body>" +
        "</html>" + "Wrong username or password"
    }
    else {
      if (res.getBoolean("admin") == false && res.getBoolean("active") == true) {
        var now : LocalDate = LocalDate.now()
        println(now)
        nodes.findOneAndUpdate(equal("username", username),Updates.set("loginDay", now)).printHeadResult()
        var result = "";
        var birthFormat = ""
        println(res.getString("birthday"))
        println(res.get("birthday").isEmpty)
        if ( res.get("birthday").isEmpty == false)  {
          var birth : Date = new SimpleDateFormat("yyyy-MM-dd").parse(res.getString("birthday"))
          birthFormat = new SimpleDateFormat("yyyy-MM-dd").format(birth)
        }

        result += "<html>" +
          "<body>" +
          "<h1>Information</h1>" +
          "<form action=\"update\">" +
          "<label>Username:</label> <br>" +
          "<input type=\"text\" id=\"username\" name=\"username\" value=\"" +
          res.getString("username")+"\" readonly=\"readonly\"> <br>"+
          "<label>Password:</label> <br>" +
          "<input type=\"text\" id=\"password\" name=\"password\" value=\""+res.getString("password")+"\"  > <br>" +
          "<label>Full name:</label> <br>" +
          "<input type=\"text\" id=\"fullname\" name=\"fullname\" value=\""+res.getString("fullname")+"\"  > <br>" +
          "<label>Birthday:</label> <br>" +
          "<input type=\"date\" id=\"birthday\" name=\"birthday\" value=\""+birthFormat+"\"  > <br>" +
          "<label>Sex:</label> <br>"
        if (res.getString("sex") == "male") {
          result += "<input type=\"radio\" id=\"male\" name=\"sex\" value=\"male\" checked> " +
            "<label for=\"male\">Male</label><br>"+
            "<input type=\"radio\" id=\"female\" name=\"sex\" value=\"female\">"
        }
        else {
          result += "<input type=\"radio\" id=\"male\" name=\"sex\" value=\"male\" checked>  " +
            "<label for=\"male\">Male</label><br>"+
            "<input type=\"radio\" id=\"female\" name=\"sex\" value=\"female\" checked>"
        }
        result += "<label for=\"female\">Female</label><br>"+
          "<label>Phone number:</label> <br>" +
          "<input type=\"text\" id=\"phonenumber\" name=\"phonenumber\" value=\""+res.getString("phonenumber")+"\"  > <br>" +
          "<input type=\"submit\" value=\"Update\">" +
          "</form>"+
          "</body>" +
          "</html>"
        result
      }
      else if (res.getBoolean("admin") == false && res.getBoolean("active") == false) {
        "<html>" +
          "<body>" +
          "<h1>Log in</h1>" +
          "<form action=\"save\">" +
          "<label>Username:</label> <br>" +
          "<input type=\"text\" id=\"username\" name=\"username\"> <br>" +
          "<label>Password:</label> <br>" +
          "<input type=\"text\" id=\"password\" name=\"password\"> <br>" +
          "<input type=\"submit\" value=\"Submit\">" +
          "</form>"+
          "<form action=\"register\">" +
          "<input type=\"submit\" value=\"Register\">" +
          "</form>"+
          "</body>" +
          "</html>" + "Your account has been deactivated"
      }
      else {
        "<html>" +
          "<body>" +
          "<h1>Welcome Admin</h1>" +
          "<form action=\"listuser\">" +
          "<input type=\"submit\" value=\"List User\">" +
          "</form>"+
          "<form action=\"statistic\">" +
          "<input type=\"submit\" value=\"Statistic\">" +
          "</form>"+
          "</body>" +
          "</html>"
      }

    }
  }

  @GetMapping(Array("/update"))
  @ResponseBody
  def update(@RequestParam("username") username : String,
             @RequestParam("password") password : String,
             @RequestParam("fullname") fullname : String,
             @RequestParam("birthday") birthday : String,
             @RequestParam("sex") sex : String,
             @RequestParam("phonenumber") phonenumber : String):String = {
    val mongoClient =  MongoClient("mongodb+srv://admin:gunpekute779@cluster0-uryix.mongodb.net/test?retryWrites=true&w=majority")
    val database = mongoClient.getDatabase("db1")
    val nodes = database.getCollection("user")

    nodes.findOneAndUpdate(equal("username", username),Updates.set("password", password)).printHeadResult()
    nodes.findOneAndUpdate(equal("username", username),Updates.set("fullname", fullname)).printHeadResult()
    nodes.findOneAndUpdate(equal("username", username),Updates.set("birthday", birthday)).printHeadResult()
    nodes.findOneAndUpdate(equal("username", username),Updates.set("sex", sex)).printHeadResult()
    nodes.findOneAndUpdate(equal("username", username),Updates.set("phonenumber", phonenumber)).printHeadResult()
    Thread.sleep(1)
    "Update success"
  }

  @GetMapping(Array("/register"))
  @ResponseBody
  def register():String = {
    "<html>" +
      "<body>" +
      "<h1>Register</h1>" +
      "<form action=\"afterRegister\">" +
      "<label>Username:</label> <br>" +
      "<input type=\"text\" id=\"username\" name=\"username\"> <br>" +
      "<label>Password:</label> <br>" +
      "<input type=\"text\" id=\"password\" name=\"password\"> <br>" +
      "<label>Full name:</label> <br>" +
      "<input type=\"text\" id=\"fullname\" name=\"fullname\"> <br>" +
      "<label>Birthday:</label> <br>" +
      "<input type=\"date\" id=\"birthday\" name=\"birthday\"> <br>" +
      "<label>Sex:</label> <br>" +
      "<input type=\"radio\" id=\"male\" name=\"sex\" value=\"male\"> " +
      "<label for=\"male\">Male</label><br>"+
      "<input type=\"radio\" id=\"female\" name=\"sex\" value=\"female\">"+
      "<label for=\"female\">Female</label><br>"+
      "<label>Phone number:</label> <br>" +
      "<input type=\"text\" id=\"phonenumber\" name=\"phonenumber\"> <br>" +
      "<input type=\"submit\" value=\"Register\"> <br>" +
      "</form>"+
      "</body>" +
      "</html>"
  }

  @GetMapping(Array("/afterRegister"))
  @ResponseBody
  def afterRegister(@RequestParam("username") username : String,
                    @RequestParam("password") password : String,
                    @RequestParam("fullname") fullname : String,
                    @RequestParam("birthday") birthday : String,
                    @RequestParam("sex") sex : String,
                    @RequestParam("phonenumber") phonenumber : String):String ={
    val emailFormat = "[a-zA-Z][a-zA-Z0-9_]*@[a-zA-Z0-9](.[a-zA-Z0-9]*)*"
    val phoneNumberFormat = "[0-9]{9,11}"
    if (username.matches(emailFormat) == false || phonenumber.matches(phoneNumberFormat) == false) {
      var result = "";
      result += "<html>" +
        "<body>" +
        "<h1>Register</h1>" +
        "<form action=\"afterRegister\">" +
        "<label>Username:</label> <br>" +
        "<input type=\"text\" id=\"username\" name=\"username\" value=\""+username+"\"  > <br>" +
        "<label>Password:</label> <br>" +
        "<input type=\"text\" id=\"password\" name=\"password\" value=\""+password+"\"  > <br>" +
        "<label>Full name:</label> <br>" +
        "<input type=\"text\" id=\"fullname\" name=\"fullname\" value=\""+fullname+"\"  > <br>" +
        "<label>Birthday:</label> <br>" +
        "<input type=\"date\" id=\"birthday\" name=\"birthday\" value=\""+birthday+"\"  > <br>" +
        "<label>Sex:</label> <br>"
      if (sex == "male") {
        result += "<input type=\"radio\" id=\"male\" name=\"sex\" value=\"male\" checked> " +
          "<label for=\"male\">Male</label><br>"+
          "<input type=\"radio\" id=\"female\" name=\"sex\" value=\"female\">"
      }
      else {
        result += "<input type=\"radio\" id=\"male\" name=\"sex\" value=\"male\" checked>  " +
          "<label for=\"male\">Male</label><br>"+
          "<input type=\"radio\" id=\"female\" name=\"sex\" value=\"female\" checked>"
      }
      result += "<label for=\"female\">Female</label><br>"+
        "<label>Phone number:</label> <br>" +
        "<input type=\"text\" id=\"phonenumber\" name=\"phonenumber\" value=\""+phonenumber+"\"  > <br>" +
        "<input type=\"submit\" value=\"Register\"> <br>" +
        "</body>" +
        "</html>" + "Wrong email or phone format"
      result
    }
    else {
      val mongoClient =  MongoClient("mongodb+srv://admin:gunpekute779@cluster0-uryix.mongodb.net/test?retryWrites=true&w=majority")
      val database = mongoClient.getDatabase("db1")
      val nodes = database.getCollection("user")
      val result = nodes.find(equal("username", username)).first()
      val waitDuration = Duration(10, "seconds")
      Thread.sleep(1)
      val res = Await.result(result.toFuture(), waitDuration)
      if (res != null) {
        var result = "";
        result += "<html>" +
          "<body>" +
          "<h1>Register</h1>" +
          "<form action=\"afterRegister\">" +
          "<label>Username:</label> <br>" +
          "<input type=\"text\" id=\"username\" name=\"username\" value=\""+username+"\"  > <br>" +
          "<label>Password:</label> <br>" +
          "<input type=\"text\" id=\"password\" name=\"password\" value=\""+password+"\"  > <br>" +
          "<label>Full name:</label> <br>" +
          "<input type=\"text\" id=\"fullname\" name=\"fullname\" value=\""+fullname+"\"  > <br>" +
          "<label>Birthday:</label> <br>" +
          "<input type=\"date\" id=\"birthday\" name=\"birthday\" value=\""+birthday+"\"  > <br>" +
          "<label>Sex:</label> <br>"
        if (sex == "male") {
          result += "<input type=\"radio\" id=\"male\" name=\"sex\" value=\"male\" checked> " +
            "<label for=\"male\">Male</label><br>"+
            "<input type=\"radio\" id=\"female\" name=\"sex\" value=\"female\">"
        }
        else {
          result += "<input type=\"radio\" id=\"male\" name=\"sex\" value=\"male\" checked>  " +
            "<label for=\"male\">Male</label><br>"+
            "<input type=\"radio\" id=\"female\" name=\"sex\" value=\"female\" checked>"
        }
        result += "<label for=\"female\">Female</label><br>"+
          "<label>Phone number:</label> <br>" +
          "<input type=\"text\" id=\"phonenumber\" name=\"phonenumber\" value=\""+phonenumber+"\"  > <br>" +
          "<input type=\"submit\" value=\"Register\"> <br>" +
          "</body>" +
          "</html>" + "Email have been used"
        result
      }
      else {
        var now : LocalDate = LocalDate.now()
        println(now)
        val doc: Document = Document("username" -> username,
          "password" -> password, "admin" -> false,"fullname" -> fullname,
          "birthday" -> birthday,"sex" -> sex,"phonenumber" -> phonenumber, "active" -> true
          )
        val result = nodes.insertOne(doc).results()
        nodes.findOneAndUpdate(equal("username", username),Updates.set("registerDay", now)).printHeadResult()
        Thread.sleep(1)
        "Register success"+
        "<html>" +
          "<body>" +
          "<form action=\"login\">" +
          "<input type=\"submit\" value=\"Login\"> <br>" +
          "</form>"+
          "</body>" +
          "</html>"

      }

    }

  }

  @GetMapping(Array("/listuser"))
  @ResponseBody
  def test():String = {
    val mongoClient = MongoClient("mongodb+srv://admin:gunpekute779@cluster0-uryix.mongodb.net/test?retryWrites=true&w=majority")
    val database = mongoClient.getDatabase("db1")
    val nodes = database.getCollection("user")
    val result = nodes.find(equal("admin", false))
    val waitDuration = Duration(10, "seconds")
    Thread.sleep(1)
    val res = Await.result(result.toFuture(), waitDuration)
    var mapUserOld : Map[String,Boolean] = Map();
    res.foreach(user =>
    {
      mapUserOld += (user.getString("username") -> user.getBoolean("active"))
      println(user.getBoolean("active"))
    })
    var lstUser = ""
    if (res != null) {
      lstUser += "<html>" +
        "<body>" +
        "<h1>List user</h1>" +
        "<form action=\"activeuser\">" +
        "<table style=\"width:30%\">" +
        "<tr><th>Username</th><th>Active</th></tr>"
      mapUserOld.keys.foreach(username =>

        if (mapUserOld.get(username) == Some(false)) {
          lstUser += "<tr><th>"+username+"</th><th>" +
            "<input type=\"checkbox\" id=\"active\" name=\"active\" value=\""+username+"\" ></th></tr> "
        }
        else {
          lstUser += "<tr><th>"+username+"</th><th>" +
            "<input type=\"checkbox\" id=\"active\" name=\"active\" value=\""+username+"\" checked></th></tr>"
        }
      )
      lstUser+=
        "<input type=\"submit\" value=\"Save\"> <br>" +
        "</form>"+
        "</body>" +
        "</html>"

    }
    lstUser
  }

  @GetMapping(Array("/activeuser"))
  @ResponseBody
  def test(@RequestParam("active") myParams: String ):String = {
    val mongoClient = MongoClient("mongodb+srv://admin:gunpekute779@cluster0-uryix.mongodb.net/test?retryWrites=true&w=majority")
    val database = mongoClient.getDatabase("db1")
    val nodes = database.getCollection("user")
    nodes.updateMany(equal("admin", false),Updates.set("active", false)).printResults()
    var activeUser = myParams.split(",")
    activeUser.foreach(user => {
      nodes.updateOne(equal("username", user),Updates.set("active", true)).printResults()
    })
    Thread.sleep(1)
    "Update success"
  }

  @GetMapping(Array("/statistic"))
  @ResponseBody
  def statistic():String = {
    var now : LocalDate = LocalDate.now()
    val mongoClient = MongoClient("mongodb+srv://admin:gunpekute779@cluster0-uryix.mongodb.net/test?retryWrites=true&w=majority")
    val database = mongoClient.getDatabase("db1")
    val nodes = database.getCollection("user")
    val todayLogin = nodes.find(equal("loginDay", now))
    val todayRegister = nodes.find(equal("registerDay", now))
    val waitDuration = Duration(10, "seconds")
    Thread.sleep(1)
    val userLogin = Await.result(todayLogin.toFuture(), waitDuration)
    val userRegister = Await.result(todayRegister.toFuture(), waitDuration)
    var res = ""
    res += "<html>" +
      "<body>" +
      "<h1>List user login today</h1> " +
      "<ul>\n  "
    userLogin.foreach(login => {
       res += "<li>" + login.getString("username") + "</li>\n "
    })
    res += "</ul>" +
      "<h1>List user register today</h1>"
    userRegister.foreach(register => {
      res += "<li>" + register.getString("username") + "</li>\n "

    })
    res += "</ul>" + "</body>" +
      "</html>"
    res


  }

}