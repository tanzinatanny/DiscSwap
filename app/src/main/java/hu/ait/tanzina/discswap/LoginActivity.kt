package hu.ait.tanzina.discswap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance();
    }

    fun loginClick(v: View) {
        if (!isFormValid()) {
            return
        }

        mAuth.signInWithEmailAndPassword(
            etEmail.text.toString(), etPassword.text.toString()
        ).addOnSuccessListener {
            Toast.makeText(
                this@LoginActivity, R.string.login_success,
                Toast.LENGTH_LONG).show()

            //show next activity
            nextActivity()

        }.addOnFailureListener {

            Toast.makeText(
                this@LoginActivity,": ${R.string.login_fail} : ${it.message}",
                Toast.LENGTH_LONG).show()
        }

    }

    fun registerClick(v: View) {
        if (!isFormValid()) {
            return
        }

        mAuth.createUserWithEmailAndPassword(
            etEmail.text.toString(), etPassword.text.toString()
        ).addOnSuccessListener {
            val user = it.user

            user?.updateProfile(
                UserProfileChangeRequest.Builder()
                .setDisplayName(userNameFromEMail(etEmail.text.toString()))
                .build()
            )

            Toast.makeText(
                this@LoginActivity, R.string.reg_success,
                Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(
                this@LoginActivity, "${R.string.reg_fail}: ${it.message}",
                  Toast.LENGTH_LONG).show()
        }

    }

    fun userNameFromEMail(email: String) = email.substringBefore("@")

    private fun isFormValid(): Boolean {
        return when {
            etEmail.text.isEmpty() -> {
                etEmail.error = "This field can not be empty"
                false
            }
            etPassword.text.isEmpty() -> {
                etPassword.error = "This field can not be empty"
                false
            }
            else -> true
        }
    }

    private fun nextActivity(){
        startActivity(
            Intent(this@LoginActivity,
                GameActivity::class.java)
        )
    }

}
