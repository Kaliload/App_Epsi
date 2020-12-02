package com.epsi.app

import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MyFragment.OnFragmentInteractionListener {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)



    setContentView(R.layout.activity_main)

    val title = arrayOf("titre1", "titre2", "titre3", "titre4")
    val imageId = arrayOf(
      "http://lorempixel.com/180/241/",
      "http://lorempixel.com/180/242/",
      "http://lorempixel.com/180/243/",
      "http://lorempixel.com/180/244/"
    )
    val editor = arrayOf("editor1", "editor2", "editor3", "editor4")
    val releaseDate = arrayOf("02/12/2021", "02/12/2022", "02/12/2023", "02/12/2024")
    val listAdapter = Adapter(this@MainActivity, title, imageId, editor, releaseDate)
    val listView = findViewById<ListView>(R.id.list_panel)
    listView.adapter = listAdapter

      tv_hello_world.setOnClickListener {
        tv_hello_world.apply {
          text = "Nouveau texte"
          setBackgroundColor(resources.getColor(R.color.colorAccent))
        }
      }

    alert_dialog_button.setOnClickListener {
      val builder = AlertDialog.Builder(this)
      builder.setTitle(R.string.alert_dialog_title)
        .setMessage(R.string.alert_dialog_msg)
        .setPositiveButton(R.string.yes,
          DialogInterface.OnClickListener { _, _ ->
            Toast.makeText(this, "Yes selected", Toast.LENGTH_LONG).show()
          })
        .setNegativeButton(R.string.no,
          DialogInterface.OnClickListener { _, _ ->
          })
      // Create the AlertDialog object and return it
      builder.create().show()
    }
    frag_dialog_button.setOnClickListener{
      val frag = MyFragment()
      val managerFrag = this.supportFragmentManager
      managerFrag.beginTransaction().add(R.id.frag, frag).commit()
    }
    tab_activity_button.setOnClickListener {
      val i = Intent(this, TabActivity::class.java)
      startActivity(i)
    }

    //Si on clique sur s'enregistrer :
    registration_button.setOnClickListener {
      //On s'assure qu'aucun champs n'est vide. Si c'est le cas, on appel la fonction UserRegisterFunction.
      // On mettra le contenu des champs en paramètre. Par ailleurs, on précisera l'action afin que PHP sache quelle fonction utiliser ensuite.
      if(username_view.text.toString() != "" && email_view.text.toString() != "" && password_view.text.toString() != ""){
        userRegisterFunction(username_view.text.toString(), email_view.text.toString(), password_view.text.toString(), "registration")
      }
      else{
        Toast.makeText(this, "Merci de remplir tous les champs", Toast.LENGTH_LONG).show()
      }
    }
    get_users_button.setOnClickListener {
      getUsers("getUsers")
    }
    qrcode.setOnClickListener {
      startActivity(Intent(this, QRcode::class.java))
    }
  }

  override fun closeFrag(){
    val managerFrag = this.supportFragmentManager
    managerFrag.beginTransaction().remove(managerFrag.fragments[0]).commit()
  }
  override fun onBackPressed() {
    val managerFrag = this.supportFragmentManager
    if(managerFrag.fragments.size > 0){
      closeFrag()
    }
    else{
      super.onBackPressed()
    }
  }

  //Notre fonction prend donc 4 paramètres.
  private fun userRegisterFunction(username: String, email: String, password: String, action: String) {
    //Dans cette fonction, on définit une nouvelle classe de type AsyncTask (la tentative de connexion doit se faire en arrière plan.
    class UserRegisterFunctionClass : AsyncTask<String, Void, String>() {
      lateinit var progressDialog: ProgressDialog
      //Avant d'initialiser notre tâche, on affiche un progress dialog tout simple
      override fun onPreExecute() {
        super.onPreExecute()
        progressDialog = ProgressDialog.show(this@MainActivity, "Loading Data", null, true, true)
      }
      //Une fois notre tâche effectuée, on supprime le progress Dialog et on affiche un Toast pour informer l'utilisateur.
      override fun onPostExecute(httpResponseMsg: String) {
        super.onPostExecute(httpResponseMsg)
        progressDialog.dismiss()
        Toast.makeText(this@MainActivity, httpResponseMsg, Toast.LENGTH_LONG).show()
      }
      //Notre tâche va consister à utiliser une classe (que nous allons créer juste après)
      //Cette classe nous permettra d'effectuer des requête HTTP/POST avec en argument un hashmap et l'url qui s'occupera de notre requête.
      override fun doInBackground(vararg params: String): String {
        val hashMap: HashMap<String?, String?> = HashMap()
        val httpParse = HttpParse()
        hashMap["username"] = params[0]
        hashMap["email"] = params[1]
        hashMap["password"] = params[2]
        hashMap["action"] = params[3]
        //On retourne la réponse (Success/Error)
        return httpParse.postRequest(hashMap, "http://192.168.1.12/EPSI/userAdministration.php")
      }
    }

    //On créer une variable qui contient une instance de la classe que l'on vient de créer.
    val userRegisterFunctionClass = UserRegisterFunctionClass()
    //Puis on execute notre tâches avec en arguments ceux qu'on reçoit dans notre fonction
    userRegisterFunctionClass.execute(username, email, password, action)
  }
  private fun getUsers(action: String){
    class GetUserClass : AsyncTask<String, Void, String>() {
      lateinit var progressDialog: ProgressDialog
      //Avant d'initialiser notre tâche, on affiche un progress dialog tout simple
      override fun onPreExecute() {
        super.onPreExecute()
        progressDialog = ProgressDialog.show(this@MainActivity, "Loading Data", null, true, true)
      }
      //Une fois notre tâche effectuée, on supprime le progress Dialog et on affiche un Toast pour informer l'utilisateur.
      override fun onPostExecute(httpResponseMsg: String) {
        //On appelle la méthode mère, puis on met notre code
        super.onPostExecute(httpResponseMsg)
        //On supprime la boite de chargement
        progressDialog.dismiss()

        //On prépare nos tableaux pour notre adapter
        //Pour des raisons de simplicité, on va utiliser des listes mutables.
        //Celles-ci permettent l'ajout d'élément à la volée,
        //On retranscrira ces listes mutables en véritable Array plus loin.
        val id = mutableListOf<String>()
        val imageId = mutableListOf<String>()
        val username = mutableListOf<String>()
        val email = mutableListOf<String>()

        //On initialise notre moteur Gson
        val gson = Gson()
        //Puis à partir du résultat fournit par notre serveur PHP (qui est de type string à l'heure actuelle)
        //On génère automatiquement un tableau qui contiendra des objets de types User
        val users: Array<User> = gson.fromJson(httpResponseMsg, Array<User>::class.java)
        //On parcours ensuite chaque User du tableau
        //Et on ajoute ses attributs au tableau correspondant (sauf pour les images qui sont là pour faire joli)
        for (user: User in users){
          id.add(user.id.toString())
          username.add(user.username!!)
          email.add(user.email!!)
          imageId.add("http://lorempixel.com/180/240/")
        }

        //Une fois que l'on a traité tous les objets, on met à jour notre adapter.
        //On pense à retranscrire nos liste mutables en Tableaux fixe avec toTypedArray()
        val listAdapter = Adapter(this@MainActivity, username.toTypedArray(), imageId.toTypedArray(), email.toTypedArray(), id.toTypedArray())
        list_panel.adapter = listAdapter
      }

      //Notre tâche va consister à utiliser une classe (que nous allons créer juste après)
      //Cette classe nous permettra d'effectuer des requête HTTP/POST avec en argument un hashmap et l'url qui s'occupera de notre requête.
      override fun doInBackground(vararg params: String): String {
        val hashMap: HashMap<String?, String?> = HashMap()
        val httpParse = HttpParse()
        hashMap["action"] = params[0]
        //On retourne la réponse (Success/Error)
        return httpParse.postRequest(hashMap, "http://192.168.1.12/EPSI/userAdministration.php")
      }
    }

    //On créer une variable qui contient une instance de la classe que l'on vient de créer.
    val userRegisterFunctionClass = GetUserClass()
    //Puis on execute notre tâches avec en arguments ceux qu'on reçoit dans notre fonction
    userRegisterFunctionClass.execute(action)
  }
}
