package br.edu.ifsp.dmo1.logindatastore.ui.logged

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.dmo1.logindatastore.R
import br.edu.ifsp.dmo1.logindatastore.databinding.ActivityLoggedBinding
import br.edu.ifsp.dmo1.logindatastore.ui.main.MainActivity

class LoggedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoggedBinding
    private lateinit var viewModel: LoggedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoggedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LoggedViewModel::class.java)

        setupListeners()
        setupObservers()
    }

    private fun setupObservers(){
        viewModel.loggedOut.observe(this, Observer {
            if(it){
                navigateToMainActivity()
                Toast.makeText(this, getString(R.string.success_logout), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupListeners(){
        binding.buttonLogout.setOnClickListener {
            handleLogout()
        }
    }

    private fun handleLogout(){
        viewModel.logout()
    }

    private fun navigateToMainActivity() {
        val mIntent = Intent(this, MainActivity::class.java)
        startActivity(mIntent)
        finish()
    }

}