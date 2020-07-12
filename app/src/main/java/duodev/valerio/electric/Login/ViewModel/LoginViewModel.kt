package duodev.valerio.electric.Login.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import duodev.valerio.electric.Login.Repo.LoginRepo
import duodev.valerio.electric.pojos.Users
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val loginRepo = LoginRepo()

    fun setUser(users: Users) {
        viewModelScope.launch {
            loginRepo.setUser(users)
        }
    }
}