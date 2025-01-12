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

    /*
    * Função de logout principal (comentada)
    *
    * Esta função realiza o logout recuperando os valores armazenados no Data Store
    * (como email, senha e preferências de login) e os salva novamente, alterando
    * a opção 'manter logado' (stayLoggedIn) para falso.
    *
    * Comportamento:
    * - Recupera os valores armazenados no Data Store: email, senha e a preferência de salvar login (saveLogin).
    * - Armazena novamente o email, senha e a opção de salvar login, desativando 'manter logado'.
    */

    fun logout() {
        viewModelScope.launch {
            val (saveLogin) = repository.loginPreferences.first()
            val (email, password) = repository.dataPreferences.first()
            repository.savePreferences(email, password, saveLogin, false)

            _loggedOut.value = true
        }
    }

    /*
     * Função de logout básica
     *
     * Esta função realiza o logout de forma simples, apagando todos os dados armazenados no Data Store,
     * independentemente de qualquer opção ou preferência marcada pelo usuário.
     *
     * Comportamento:
     * - Remove as informações de login (email e senha) e desativa todas as preferências,
     *   incluindo 'saveLogin' e 'stayLoggedIn'.
     * - Atualiza o estado para indicar que o logout foi concluído.
     *
     */

    /*fun logout(){
        viewModelScope.launch {
            repository.savePreferences("", 0L, false, false)
            _loggedOut.value = true
        }
    }*/

    /*
     * Função de logout alternativa (comentada)
     *
     * Esta função realiza o logout recuperando os valores armazenados no Data Store,
     * como email, senha e preferências de login, e verifica se a opção 'manter logado'
     * (stayLoggedIn) está ativada antes de realizar um novo salvamento.
     *
     * Comportamento:
     * - Recupera as preferências armazenadas no Data Store: 'saveLogin' e 'stayLoggedIn'.
     * - Caso 'stayLoggedIn' esteja ativado, realiza um novo salvamento das preferências,
     *   desativando a opção 'manter logado'.
     * - Caso contrário, evita operações desnecessárias e mantém os valores como estão.
     *
     * Observação:
     * Essa abordagem garante que o salvamento das preferências só ocorra quando necessário,
     * reduzindo operações redundantes e otimizando o processo de logout.
     */

    /*fun logout() {
        viewModelScope.launch {
            val (saveLogin, stayLoggedIn) = repository.loginPreferences.first()
            val (email, password) = repository.dataPreferences.first()

            if (stayLoggedIn) {
                repository.savePreferences(email, password, saveLogin, false)
            }

            _loggedOut.value = true
        }
    }*/
}