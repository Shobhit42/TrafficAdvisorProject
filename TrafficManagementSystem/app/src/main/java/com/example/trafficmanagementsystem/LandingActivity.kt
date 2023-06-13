package com.example.trafficmanagementsystem

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.trafficmanagementsystem.Constants.KEY_FIRST_TIME_TOGGLE
import com.example.trafficmanagementsystem.Constants.KEY_FIRST_TIME_TOGGLE_AVATAR
import com.example.trafficmanagementsystem.Constants.KEY_NAME
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_landing.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class LandingActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPref : SharedPreferences


    @field:Named("First")
    @JvmField
    @Inject
    var isFirstAppOpener = true

    private var selectedImg = "";

    lateinit var bitmap : Bitmap

    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var authListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        if(!isFirstAppOpener){
            val intent = Intent(this , RouteActivity::class.java)
            startActivity(intent)
        }

        window?.decorView?.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        window.statusBarColor = ContextCompat.getColor(this , R.color.purple_200)

        firebaseAuth = FirebaseAuth.getInstance()
        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                if (user.isEmailVerified) {
                    val intent = Intent(this, MainActivity::class.java);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "gpt", Toast.LENGTH_LONG).show()
                }
            }
        }
        FirebaseAuth.getInstance().addAuthStateListener(authListener)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this , gso)

        btnGoogleLogin.setOnClickListener{
            val success = writePersonalDataToSharedPref()
            if(success){
                btnGoogleLogin.visibility = View.INVISIBLE
                google_progress_bar.visibility = View.VISIBLE
                signInGoogle()
            }else{
                Toast.makeText(this, "Please Enter All Fields", Toast.LENGTH_SHORT).show()
            }

        }

        female1.setOnClickListener {
            makeImageViewNormal()
            female1.scaleX = 1.2F
            female1.scaleY = 1.2F
            selectedImg = "female1"
            bitmap = (female1.drawable as BitmapDrawable).bitmap
        }

        female2.setOnClickListener {
            makeImageViewNormal()
            female2.scaleX = 1.2F
            female2.scaleY = 1.2F
            selectedImg = "female2"
            bitmap = (female2.drawable as BitmapDrawable).bitmap
        }

        female3.setOnClickListener {
            makeImageViewNormal()
            female3.scaleX = 1.2F
            female3.scaleY = 1.2F
            selectedImg = "female3"
            bitmap = (female3.drawable as BitmapDrawable).bitmap
        }

        male1.setOnClickListener {
            makeImageViewNormal()
            male1.scaleX = 1.2F
            male1.scaleY = 1.2F
            selectedImg = "male1"
            bitmap = (male1.drawable as BitmapDrawable).bitmap
        }

        male2.setOnClickListener {
            makeImageViewNormal()
            male2.scaleX = 1.2F
            male2.scaleY = 1.2F
            selectedImg = "male2"
            bitmap = (male2.drawable as BitmapDrawable).bitmap
        }

        male3.setOnClickListener {
            makeImageViewNormal()
            male3.scaleX = 1.2F
            male3.scaleY = 1.2F
            selectedImg = "male3"
            bitmap = (male3.drawable as BitmapDrawable).bitmap
        }

    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if(result.resultCode == Activity.RESULT_OK){     // it checks the which account has been selected
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }else{
            btnGoogleLogin.visibility = View.VISIBLE
            google_progress_bar.visibility = View.INVISIBLE
            Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_SHORT).show()
            val intent = Intent(this , RouteActivity::class.java)
            startActivity(intent)
            //FancyToast.makeText(this,"Email Id was not selected",FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account : GoogleSignInAccount? = task.result
            if(account != null){
                updateUI(account)
            }
        }else{
            btnGoogleLogin.visibility = View.VISIBLE
            google_progress_bar.visibility = View.INVISIBLE
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken , null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                GlobalScope.launch {
                    delay(1000) // Pause for 1 second
                    val intent = Intent(this@LandingActivity, MainActivity::class.java)
                    startActivity(intent)
                    runOnUiThread {
                        Toast.makeText(this@LandingActivity, "Successfully Logged In", Toast.LENGTH_LONG).show()
                        //FancyToast.makeText(this@YourActivity, "Successfully Logged In", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show()
                    }
                }
                //FancyToast.makeText(this,"Successfully Logged In",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true).show();
            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(firebaseAuth.currentUser?.isEmailVerified == true){
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent);
        }
    }

    override fun onStart() {
        super.onStart()
        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                if (user.isEmailVerified) {
                    val intent = Intent(this, MainActivity::class.java);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "gpt", Toast.LENGTH_LONG).show()
                }
            }
        }
        FirebaseAuth.getInstance().addAuthStateListener(authListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().removeAuthStateListener(authListener)
    }

    private fun writePersonalDataToSharedPref() : Boolean {
        val name = edName.text.toString()
        if(name.isEmpty()){
            return false;
        }
        if(selectedImg.isEmpty()){
            return false;
        }
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val base64String = android.util.Base64.encodeToString(byteArray, Base64.DEFAULT)
        sharedPref.edit()
            .putString(KEY_NAME , name)
            .putString(Constants.KEY_IMG, base64String)
            .putBoolean(KEY_FIRST_TIME_TOGGLE_AVATAR, false)
            .apply()

        return true
    }

    private fun makeImageViewNormal() {
        female1.scaleX = 1F
        female1.scaleY = 1F

        female2.scaleX = 1F
        female2.scaleY = 1F

        female3.scaleX = 1F
        female3.scaleY = 1F

        male1.scaleX = 1F
        male1.scaleY = 1F

        male2.scaleX = 1F
        male2.scaleY = 1F

        male3.scaleX = 1F
        male3.scaleY = 1F
    }

}
