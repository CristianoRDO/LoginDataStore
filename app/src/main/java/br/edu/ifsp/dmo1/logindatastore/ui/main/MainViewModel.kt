package br.edu.ifsp.dmo1.logindatastore.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope

import br.edu.ifsp.dmo1.logindatastore.data.DataStoreRepository
import br.edu.ifsp.dmo1.logindatastore.data.User
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = DataStoreRepository(application)

    val loginPreferences: LiveData<Pair<Boolean, Boolean>> = repository.loginPreferences.asLiveData()
    val dataPreferences: LiveData<Pair<String, Long>> = repository.dataPreferences.asLiveData()

    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean> = _loggedIn

    /*
     * Esta função realiza o login do usuário, autenticando o email e a senha.
     * Também salva as preferências do usuário com base nos parâmetros fornecidos.
     *
     * Percebi que talvez não fosse necessário verificar o parâmetro "stayLoggedIn".
     * Caso o usuário não opte por salvar os dados de login ("saveLogin" seja falso)
     * mas marque a opção de "stayLoggedIn", as credenciais permanecerão salvas
     * mesmo após o logout, o que pode gerar inconsistências.
     *
     * No entanto, como não tinha certeza se poderia realizar essa alteração,
     * mantive a versão original (implementada no roteiro).
     */

    fun login(email: String, passwd: Long, saveLogin: Boolean, stayLoggedIn: Boolean) {
        if (User.autenticate(email, passwd)) {
            _loggedIn.value = true
            if (saveLogin || stayLoggedIn) // if(saveLogin) -> Apenas isso.
                savePreferences(email, passwd, saveLogin, stayLoggedIn)
            else
                savePreferences("", 0L, saveLogin, stayLoggedIn)
        } else {
            _loggedIn.value = false
        }
    }

    private fun savePreferences(email: String, password: Long, saveLogin: Boolean, stayLoggedIn: Boolean) {
        viewModelScope.launch {
            repository.savePreferences(email, password, saveLogin, stayLoggedIn)
        }
    }

}