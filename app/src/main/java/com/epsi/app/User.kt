package com.epsi.app

//On commence par créer la classe la plus simple qui soit, dans un fichier a part :
class User {
  var id = 0
  var username: String? = null
  var email: String? = null
  override fun toString(): String {
    return "[ id: $id, name: $username, email: $email ]"
  }
}
