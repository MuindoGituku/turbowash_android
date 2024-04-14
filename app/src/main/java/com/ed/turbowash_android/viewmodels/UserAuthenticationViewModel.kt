/**
 * @author Group 3 - Muindo Gituku, Emre Deniz, Nkemjika Obi
 * @date Apr, 2024
 */

package com.ed.turbowash_android.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserAuthenticationViewModel @Inject constructor(private val application: Application) : AndroidViewModel(application) {
    private val _authenticationState = MutableLiveData<Boolean>()
    val authenticationState: LiveData<Boolean> = _authenticationState

    fun authenticateWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authenticationState.value = true
                } else {
                    Log.w("GoogleSignIn", "signInWithCredential:failure", task.exception)
                    _authenticationState.value = false
                }
            }
    }

    fun logoutAuthenticatedUser() {
        FirebaseAuth.getInstance().signOut()
        GoogleSignIn.getClient(application, GoogleSignInOptions.DEFAULT_SIGN_IN).apply {
            signOut().addOnCompleteListener {
                revokeAccess().addOnCompleteListener {
                    _authenticationState.value = false
                }
            }
        }
    }

}

