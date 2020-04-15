package com.example.app.domain

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import com.sun.xml.internal.stream.Entity
import java.text.SimpleDateFormat
import scala.beans.BeanProperty
import scala.annotation.meta.field

class Information( @BeanProperty var email: String,
            @BeanProperty  var password: String,
            @BeanProperty  var fullname: String,
            @BeanProperty  var birthday: SimpleDateFormat,
            @BeanProperty  var sex: String,
            @BeanProperty  var phoneNumber: Int) {

  def this() = this("", "", "", new SimpleDateFormat("dd/MM/yyyy"),"", 123)
}