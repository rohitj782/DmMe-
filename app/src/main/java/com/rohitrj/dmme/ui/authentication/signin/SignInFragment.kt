package com.rohitrj.dmme.ui.authentication.signin

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.database.*

import com.rohitrj.dmme.R
import com.rohitrj.dmme.data.User
import com.rohitrj.dmme.ui.authentication.IsLoggedIn
import com.rohitrj.dmme.ui.authentication.signup.TAG
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sign_in_fragment.*
import kotlinx.android.synthetic.main.sign_up_fragment.*

class SignInFragment : Fragment() {

    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        // TODO: Use the ViewModel

        activity!!.bottomNavigation.visibility = View.GONE

        textViewSignUp.setOnClickListener {
            openSignUp(it)
        }

        buttonSignIn.setOnClickListener {
            proceedSignIn(it)
        }
    }

    private fun proceedSignIn(view: View) {


        val email: String
        val password: String

        email = editTextSignIn.text.toString()
        password = editTextPassword.text.toString()

        if (email.isEmpty()) {
            editTextSignIn.error = "Email required."
            editTextSignIn.requestFocus()
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextSignIn.error = "Enter a valid email."
            editTextSignIn.requestFocus()
            return
        }

        if (password.isEmpty()) {
            editTextPassword.error = "Password required."
            editTextPassword.requestFocus()
            return
        }
        progressBarSignIn.visibility = View.VISIBLE
        buttonSignIn.isEnabled = false

        val query = FirebaseDatabase.getInstance()
            .reference.child("users").child("auth")
            .orderByChild("email").equalTo(email)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    var user = User()
                    for (dataSnapshot: DataSnapshot in p0.children)
                        user = dataSnapshot.getValue(User::class.java)!!
                    if (password.equals(user.password)) {
                        context?.let { it1 -> IsLoggedIn().loginUser(email, it1) }
                        progressBarSignIn.visibility = View.INVISIBLE
                        buttonSignIn.isEnabled = true
                        Navigation.findNavController(view).
                            navigate(SignInFragmentDirections.openHome())
                    } else {
                        editTextPassword.error = "Incorrect password."
                        editTextPassword.requestFocus()
                        progressBarSignIn.visibility = View.INVISIBLE
                        buttonSignIn.isEnabled = true
                        return
                    }

                } else {
                    editTextSignIn.error = "User doesn't exist."
                    progressBarSignIn.visibility = View.INVISIBLE
                    buttonSignIn.isEnabled = true
                    editTextSignIn.requestFocus()
                    return
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                progressBarSignIn.visibility = View.INVISIBLE
                buttonSignIn.isEnabled = true
            }
        })
    }

    private fun openSignUp(view: View) {
        Navigation.findNavController(view).navigate(SignInFragmentDirections.openSignUp())
    }

}
