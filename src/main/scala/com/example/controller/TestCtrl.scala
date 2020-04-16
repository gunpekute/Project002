package com.example.app.controller
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, RequestParam, ResponseBody}
import java.util.Date
import java.text.SimpleDateFormat
import java.time.LocalDate

import com.example.domain.UserActive
import com.example.domain.DbsExecute

import org.springframework.ui.{Model}
import org.mongodb.scala._

@Controller
class TestCtrl {
  val dbs = new DbsExecute()

  @GetMapping(Array("/login"))
  def demo(model: Model )={
    "login"
  }

  @GetMapping(Array("/save"))
  def test(model: Model, @RequestParam("username") username : String, @RequestParam("password") password : String):String ={
    val res = dbs.checkLogin(username,password)
    if (res == null) {
      model.addAttribute("error","Wrong username or password")
      "login"

    }
    else {
      if (res.getBoolean("admin") == false && res.getBoolean("active") == true) {
        var now : LocalDate = LocalDate.now()
        println(now)
        var birthFormat = ""
        println(res.getString("birthday"))
        if ( res.get("birthday").isEmpty == false && res.getString("birthday") != "")  {
          var birth : Date = new SimpleDateFormat("yyyy-MM-dd").parse(res.getString("birthday"))
          birthFormat = new SimpleDateFormat("yyyy-MM-dd").format(birth)
        }

        dbs.loginDay(username,now)
        model.addAttribute("username",username)
        model.addAttribute("password",password)
        model.addAttribute("fullname",res.getString("fullname"))
        model.addAttribute("birthday",birthFormat)
        model.addAttribute("phonenumber",res.getString("phonenumber"))
        model.addAttribute("sex",res.getString("sex"))

        "information"

      }
      else if (res.getBoolean("admin") == false && res.getBoolean("active") == false) {
        model.addAttribute("error","Your account has been deactivated")
        "login"
      }
      else {
        "admin"
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

    dbs.updateInfor(username,password,fullname,birthday,sex,phonenumber)
    "Update success"
  }

  @GetMapping(Array("/register"))
  def register():String = {
    "register"
  }

  @GetMapping(Array("/afterRegister"))
  def afterRegister(model: Model,
                    @RequestParam("username") username : String,
                    @RequestParam("password") password : String,
                    @RequestParam("fullname") fullname : String,
                    @RequestParam("birthday") birthday : String,
                    @RequestParam("sex") sex : String,
                    @RequestParam("phonenumber") phonenumber : String):String ={
    val emailFormat = "[a-zA-Z][a-zA-Z0-9_]*@[a-zA-Z0-9](.[a-zA-Z0-9]*)*"
    val passFormat = "[a-zA-Z0-9]+"
    val phoneNumberFormat = "[0-9]{9,11}"

    model.addAttribute("username",username)
    model.addAttribute("password",password)
    model.addAttribute("fullname",fullname)
    model.addAttribute("birthday",birthday)
    model.addAttribute("sex",sex)
    model.addAttribute("phonenumber",phonenumber)
    if (username.matches(emailFormat) == false ||
        phonenumber.matches(phoneNumberFormat) == false ||
      password.matches(passFormat) == false) {
      model.addAttribute("error","Format of email, password or phone is not correct or blank")
      "register"
    }
    else {
      val res = dbs.checkExist(username)
      if (res != null) {
        model.addAttribute("error","Email has been used")
        "register"
      }
      else {
        dbs.register(username,password,fullname,birthday,sex,phonenumber)
        model.addAttribute("error","Register success, please login again")
        "login"
      }

    }

  }

  @GetMapping(Array("/listuser"))
  def test(model: Model):String = {
    val res = dbs.listUser()
    var lstUser : Array[UserActive] = Array()
    res.foreach(user =>
    {
      var userAct = new UserActive(user.getString("username"), user.getBoolean("active"))
      lstUser =  lstUser :+ userAct

    })
    println(lstUser)
    model.addAttribute("requireList",lstUser)
    "listUser"
  }

  @GetMapping(Array("/activeuser"))
  @ResponseBody
  def test(@RequestParam(value = "active",required = false,defaultValue = "false") lstUserActive: String ):String = {
    dbs.activeUser(lstUserActive)
    "Update success"
  }

  @GetMapping(Array("/statistic"))
  def statistic(model: Model):String = {
    var loginArr = dbs.userLog("loginDay")
    var registerArr = dbs.userLog("registerDay")
    model.addAttribute("loginArr",loginArr)
    model.addAttribute("registerArr",registerArr)
    "statictis"
  }

}