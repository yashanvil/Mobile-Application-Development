package uni.leeds.comp3222

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val user = FirebaseAuth.getInstance().currentUser
        val statusText = findViewById<TextView>(R.id.loginStatus)

        if (user != null) {
            statusText.text = getString(R.string.signed_in_status) + user.email
            val signOutButton = findViewById<Button>(R.id.signOutButton)
            signOutButton.visibility = View.VISIBLE
            signOutButton.setOnClickListener {
                logoutUser()
            }
        } else {
            statusText.text = getString(R.string.not_signed_in_status)
            val signInButton = findViewById<Button>(R.id.signInButton)
            signInButton.visibility = View.VISIBLE
            signInButton.setOnClickListener {
                launchFirebaseAuthUI()
            }
        }
    }

    private fun launchFirebaseAuthUI() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
        // [END auth_fui_create_intent]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                finish()
                //startActivity(intent)
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.

                findViewById<TextView>(R.id.loginStatus).text =
                    getString(R.string.failed_login_status)

            }
        }
    }

    private fun logoutUser() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                finish()
                //startActivity(intent)
            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.

    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}
