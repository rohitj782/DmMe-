package com.rohitrj.dmme.ui.authentication.signup

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*

import com.rohitrj.dmme.R
import com.rohitrj.dmme.data.User
import kotlinx.android.synthetic.main.sign_up_fragment.*

const val TAG = "signUpFragment"

class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_up_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        // TODO: Use the ViewModel

        buttonSignUp.setOnClickListener {
            proceedSignIn()
        }

    }

    private fun proceedSignIn() {

        val email: String
        val password: String
        val name: String

        email = editTextEmailSignUp.text.toString()
        password = editTextPasswordSignUp.text.toString()
        name = editTextName.text.toString()

        if (name.isEmpty()) {
            editTextName.error = "Name required."
            editTextName.requestFocus()
            return
        }

        if (email.isEmpty()) {
            editTextEmailSignUp.error = "Email required."
            editTextEmailSignUp.requestFocus()
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmailSignUp.error = "Enter a valid email."
            editTextEmailSignUp.requestFocus()
            return

        }

        if (password.isEmpty()) {
            editTextPasswordSignUp.error = "Password required."
            editTextPasswordSignUp.requestFocus()
            return
        }

        progressBarSignUp.visibility = View.VISIBLE
        buttonSignUp.isEnabled = false

        val query = FirebaseDatabase.getInstance()
            .reference.child("users").child("auth")
            .orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    editTextEmailSignUp.error = "Email already registered."
                    editTextEmailSignUp.requestFocus()
                    progressBarSignUp.visibility = View.INVISIBLE
                    buttonSignUp.isEnabled = true
                    return
                } else {

                    val databaseReference: DatabaseReference = FirebaseDatabase.getInstance()
                        .reference.child("users").child("auth")
                    val user = User(name, email, password)
                    databaseReference.push().setValue(user).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Log.i(TAG, "SignUp success")
                            progressBarSignUp.visibility = View.INVISIBLE
                            buttonSignUp.isEnabled = true
                        } else {
                            Log.i(TAG, "SignUp failed")
                            progressBarSignUp.visibility = View.INVISIBLE
                            buttonSignUp.isEnabled = true
                        }
                    }

                }
            }

            override fun onCancelled(p0: DatabaseError) {
                progressBarSignUp.visibility = View.INVISIBLE
                buttonSignUp.isEnabled = true
            }
        })


    }

}
