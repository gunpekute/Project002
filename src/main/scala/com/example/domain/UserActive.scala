package com.example.domain

import javax.persistence.{Entity, GeneratedValue, GenerationType, Id}

import scala.beans.BeanProperty

@Entity
class UserActive(name: String, activeValue: Boolean) {
    @BeanProperty
    var username: String = name
    @BeanProperty
    var active: Boolean = activeValue


  }