package br.edu.ifsp.dmo1.logindatastore.ui.logged

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import br.edu.ifsp.dmo1.logindatastore.data.DataStoreRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoggedViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = DataStoreRepository(application)

    private val _loggedOut = MutableLiveData<Boolean>()
    val loggedOut: LiveData<Boolean> = _loggedOut

    fun logout(){
        viewModelScope.launch {
            repository.savePreferences("", 0L, false, false)
            _loggedOut.value = true
        }
    }

    /*
     * Função de logout alternativa (comentada)
     *
     * Esta versão armazena os dados de login dependendo da escolha do usuário:
     * - Se o usuário optou por salvar o login, os dados (email e senha) são armazenados novamente.
     * - Se o usuário não optou por salvar o login, as preferências são apagadas.
     *
     * Coleta as preferências de 'saveLogin' e 'stayLoggedIn' para determinar o comportamento:
     * - Se 'saveLogin' for verdadeiro, coleta o email e senha armazenados e os salva novamente, com a opção 'stayLoggedIn' desmarcada.
     * - Caso contrário, apaga as preferências (dados de login e opções).
     *
     * Como não sabia se essa funcionalidade era necessária e também não sabia se havia implementado corretamente,
     * decidi deixá-la como função secundária, apenas por curiosidade.
     */

    /*fun logout() {
        viewModelScope.launch {
            val (saveLogin, stayLoggedIn) = repository.loginPreferences.first()

            if (saveLogin) {
                val (email, password) = repository.dataPreferences.first()
                repository.savePreferences(email, password, saveLogin, false)
            } else {
                repository.savePreferences("", 0L, false, false)
            }
            _loggedOut.value = true
        }
    }*/
}