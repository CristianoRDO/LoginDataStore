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
     * Função responsável por realizar o logout do usuário.
     *
     * Ao ser chamada, ela invoca a função 'logoutUser' no repositório, que é responsável por modificar apenas o valor de
     * 'stayLoggedIn', definindo-o como 'false'. Isso evita a recuperação e o salvamento de dados desnecessários, como o
     * email e a senha, otimizando o processo.
     *
     * Após a execução da função no repositório, a variável '_loggedOut' é atualizada para 'true', indicando que o usuário
     * foi deslogado com sucesso.
     */

    fun logout() {
        viewModelScope.launch {
            repository.logoutUser()
            _loggedOut.value = true
        }
    }

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

    /*fun logout() {
        viewModelScope.launch {
            val (saveLogin) = repository.loginPreferences.first()
            val (email, password) = repository.dataPreferences.first()
            repository.savePreferences(email, password, saveLogin, false)

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