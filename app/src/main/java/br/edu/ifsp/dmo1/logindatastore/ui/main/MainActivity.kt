package br.edu.ifsp.dmo1.logindatastore.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.dmo1.logindatastore.R
import br.edu.ifsp.dmo1.logindatastore.databinding.ActivityMainBinding
import br.edu.ifsp.dmo1.logindatastore.ui.logged.LoggedActivity

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private var flag = false // Flag usada para garantir que a navegação para a 'LoggedActivity' ocorra apenas uma vez.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.loggedIn.observe(this, Observer {
            if (it) {
                navigateToLoggedActivity()

            } else {
                Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.loginPreferences.observe(this, Observer {
            val (saveLogin, stayLoggedIn) = it
            if (stayLoggedIn) {
                navigateToLoggedActivity()
            }
            binding.checkboxSaveLogin.isChecked = saveLogin
            binding.checkboxStayLoggedin.isChecked = stayLoggedIn
        })

        viewModel.dataPreferences.observe(this, Observer {
            val (email, password) = it
            binding.textEmail.setText(email)
            if (email.isNotEmpty())
                binding.textPassword.setText(password.toString())
        })
    }

    private fun setupListeners() {
        binding.buttonLogin.setOnClickListener {
            handleLogin()
        }
    }

    private fun handleLogin() {
        val email = binding.textEmail.text.toString()
        val passwd = binding.textPassword.text.toString()
        val saveLogin = binding.checkboxSaveLogin.isChecked
        val stayLoggedIn = binding.checkboxStayLoggedin.isChecked

        binding.textEmail.setText("")
        binding.textPassword.setText("")
        binding.checkboxSaveLogin.isChecked = false
        binding.checkboxStayLoggedin.isChecked = false

        if (email.isNotBlank() && passwd.isNotBlank()) {
            val passwdConvert = passwd.toLongOrNull()

            if (passwdConvert != null) {
                viewModel.login(email, passwdConvert, saveLogin, stayLoggedIn)
            } else {
                Toast.makeText(this, getString(R.string.invalid_password_format), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToLoggedActivity() {

        Log.d("MainActivity", "navigateToLoggedActivity() foi chamada") // Exibir no Log a Exibição das chamadas

        /*
         * Acredito que a flag está sendo usada para garantir que a navegação para a 'LoggedActivity' aconteça apenas uma vez.
         * Isso evita múltiplas tentativas de navegação enquanto a atividade já foi iniciada.
         *
         * Principal motivo:
         *
         * Após um login ser realizado corretamente, os dois Observers configurados no método `setupObservers()` podem ser notificados:
         * 1. O Observer de `viewModel.loggedIn` é acionado quando o valor de `loggedIn` muda para `true`, chamando a função `navigateToLoggedActivity()`.
         * 2. O Observer de `viewModel.loginPreferences` é acionado quando as preferências de login são atualizadas e `stayLoggedIn` está definido como `true`,
         * também chamando `navigateToLoggedActivity()`.
         *
         * Esse comportamento pode levar a múltiplas chamadas consecutivas de `navigateToLoggedActivity()`, resultando na tentativa de abrir
         * a mesma Activity (`LoggedActivity`) mais de uma vez, o que pode gerar problemas, como sobreposição de telas ou comportamento inesperado.
         *
         * Para evitar esse problema, foi implementada a variável `flag`. Ela funciona como uma proteção, garantindo que a navegação para a
         * `LoggedActivity` ocorra apenas uma vez. Quando a função é chamada pela primeira vez, a `flag` é definida como `true`, impedindo
         * novas chamadas subsequentes enquanto a atividade atual ainda está em execução.
         *
         * Outra situação possível (difícil, mas possível): Se o usuário clicar em 'Entrar' e houver uma demora na execução da Activity, pode ser que o usuário clique novamente,
         * por acreditar que ação não foi iniciada, nesse caso, sem uma flag, outra Activity seria iniciada, o que não é desejado.
         */

        if (!flag) {
            flag = true
            val mIntent = Intent(this, LoggedActivity::class.java)
            startActivity(mIntent)
            finish()
        }
    }
}