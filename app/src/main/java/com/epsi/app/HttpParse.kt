package com.epsi.app

import android.util.Log
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

//Une classe indépendante, qui n'a pas de classe mère
class HttpParse {
  //On déclare les variables dont on va avoir besoin, (même si on ne les initialises pas)
  private var finalHttpData = ""
  private lateinit var result: String
  private lateinit var bufferedWriter: BufferedWriter
  private lateinit var outputStream: OutputStream
  private lateinit var bufferedReader: BufferedReader
  private var stringBuilder = StringBuilder()
  private lateinit var url: URL
  //Puis on commence directement la méthode pour faire des requêtes POST.
  //Comme mentionné juste avant, on mettra deux arguments,
  // le hashmap qui contiendra les infos à faire passer en POST
  //Et l'url de la page php qui va s'occuper de la requête :
  fun postRequest(Data: HashMap<String?, String?>, HttpUrlHolder: String?): String {
    try {
      //On passe notre url type String en vrai url et on ouvre une connection HTTP dessus.
      //On définit par ailleurs les timeout, la méthod utilisées, le streams d'entrée et le buffer.
      url = URL(HttpUrlHolder)
      val httpURLConnection = url.openConnection() as HttpURLConnection
      httpURLConnection.readTimeout = 14000
      httpURLConnection.connectTimeout = 14000
      httpURLConnection.requestMethod = "POST"
      httpURLConnection.doInput = true
      httpURLConnection.doOutput = true
      outputStream = httpURLConnection.outputStream
      bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))
      //Une fois le buffer parametré, on va parser nos données (pour quelles aient une allure type action=xxx&username=yyy...)
      //Pour cela on utilisera une fonction que l'on écrira un peu plus bas.
      bufferedWriter.write(finalDataParse(Data))
      bufferedWriter.flush()
      bufferedWriter.close()
      outputStream.close()
      //Enfin, si on a une reponse HTTP OK, on garde la réponse en mémoire pour l'afficher dans un Toast.
      //Sinon, on affichera une erreur dans le Toast
      if (httpURLConnection.responseCode == HttpURLConnection.HTTP_OK) {
        bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
        finalHttpData = bufferedReader.readLine()
      } else {
        finalHttpData = "Something Went Wrong"
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
    //On retourne ce qui sera le contenue du Toast.
    return finalHttpData
  }

  //Pour parser nos données, on va simplement parcourir notre HashMap.
  //Puis en utilisant un StringBuilder, on va concaténer des esperluettes, les clés, des égals et les valeurs
  //Cela créera un string du style &action=xxx&username=yyy&email=zzz ...
  private fun finalDataParse(hashMap2: HashMap<String?, String?>): String {
    for ((key, value) in hashMap2.entries) {
      stringBuilder.append("&")
      stringBuilder.append(URLEncoder.encode(key, "UTF-8"))
      stringBuilder.append("=")
      stringBuilder.append(URLEncoder.encode(value, "UTF-8"))
    }
    //Enfin, on convertit ce résultat avant de le retourner.
    result = stringBuilder.toString()
    return result
  }
}
