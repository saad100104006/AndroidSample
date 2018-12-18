package uk.co.transferx.app.ui.recipients.addrecipients.presenters

import android.content.SharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import uk.co.transferx.app.data.dto.RecipientDto
import uk.co.transferx.app.data.pojo.Recipient
import uk.co.transferx.app.data.remote.RecipientsApi
import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepository
import uk.co.transferx.app.data.repository.tokenmanager.TokenManager
import uk.co.transferx.app.ui.base.BasePresenter
import uk.co.transferx.app.ui.base.UI
import uk.co.transferx.app.util.Constants.*
import uk.co.transferx.app.util.Util
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection


class AddRecipientsPresenter @Inject
constructor(private val recipientsApi: RecipientsApi,
            private val tokenManager: TokenManager,
            private val recipientRepository: RecipientRepository,
            sharedPreferences: SharedPreferences) : BasePresenter<AddRecipientsPresenter.AddRecipientsUI>(sharedPreferences) {
    var recipient: RecipientDto? = null
        set(recipient) {
            if (recipient == null) return
            field = recipient
            firstName = this.recipient?.firstName
            lastName = this.recipient?.lastName
            phone = this.recipient?.phone
            country = this.recipient?.country
        }

    private var disposable: Disposable? = null
    private var firstName: String? = null
    private var lastName: String? = null
    private var country: String? = null
    private var phone: String? = null

    fun saveUserToApi() {
        disposable = tokenManager.token
                .flatMap { (accessToken) ->
                    recipientsApi.addRecipient(accessToken, Recipient.Builder()
                            .firstname(firstName)
                            .lastname(lastName)
                            .country(country)
                            .phone(phone)
                            .build())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp ->
                    if (resp.code() == HttpsURLConnection.HTTP_OK && ui != null) {
                        recipientRepository.addUser(RecipientDto(resp.body()?.id, firstName, lastName, null, country, phone))
                        val firstRecipientAdded = sharedPreferences.getBoolean(FIRST_RECIPIENT_ADDED, false)
                        if (!firstRecipientAdded) {
                            this.ui.goToConfirmationScreen(FIRST_RECIPIENT_ADDED_MODE)
                            sharedPreferences.edit().putBoolean(FIRST_RECIPIENT_ADDED, true).apply()
                            sharedPreferences.edit().putBoolean(RECIPIENT_REQUIRED, false).apply()
                        } else this.ui.goToConfirmationScreen(RECIPIENT_ADDED_MODE)
                    }
                }, { this.handleError(it) })
    }

    fun refreshUserData() {
        disposable = tokenManager.token
                .flatMap { (accessToken) ->
                    recipientsApi.updateRecipient(accessToken,
                            recipient!!.id,
                            Recipient.Builder()
                                    .firstname(recipient?.firstName)
                                    .lastname(recipient?.lastName)
                                    .country(recipient?.country)
                                    .phone(recipient?.phone)
                                    .build())
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ resp ->
                    if (resp.code() == HttpURLConnection.HTTP_OK && ui != null) {
                        ui.userActionPerformed()
                        recipientRepository.upDateUser(recipient)
                        return@subscribe
                    }
                    handleError(Throwable(resp.errorBody()!!.string()))
                }, { this.handleError(it) })
    }

    fun deleteRecipient(id: String?) {
        if (id == null) {
            handleError(Throwable("Error id is null"))
            return
        }
        disposable = tokenManager.token
                .flatMap { (accessToken) -> recipientsApi.deleteRecipient(accessToken, id) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { responseBodyResponse ->
                    if (responseBodyResponse.code() == HttpURLConnection.HTTP_OK && ui != null) {
                        recipientRepository.deleteRecipient(id)
                        ui.userActionPerformed()
                    }
                }
    }

    fun sendTransfer() {
        ui?.sendTransfer(recipient)

    }

    fun setFirstName(firstName: String) {
        this.firstName = firstName
        recipient?.firstName = firstName

        validateInput()
    }

    fun setLastName(lastName: String) {
        this.lastName = lastName
        recipient?.lastName = lastName

        validateInput()
    }

    fun setCountry(country: String) {
        this.country = country
        recipient?.country = country
        validateInput()
    }

    fun setPhone(phone: String) {
        this.phone = phone
        recipient?.phone = phone
        validateInput()
    }

    private fun validateInput() {
        ui?.setEnabledButton(isInputValid)
    }

    override fun attachUI(ui: AddRecipientsUI) {
        super.attachUI(ui)
        recipient?.let { this.ui.setData(recipient!!) }
    }

    override fun detachUI() {
        super.detachUI()

        if (disposable != null && !disposable!!.isDisposed) disposable!!.dispose()
    }

    private fun handleError(throwable: Throwable) {
        ui?.showError()
    }

    private val isInputValid: Boolean
        get() = (Util.validateName(firstName) && Util.validateName(lastName)
                && Util.validateName(country) && Util.validatePhone(phone))

    interface AddRecipientsUI : UI {

        fun userActionPerformed()

        fun goToConfirmationScreen(MODE: Int)

        fun showError()

        fun setData(recipientDto: RecipientDto)

        fun setEnabledButton(enabled: Boolean)

        fun sendTransfer(recipientDto: RecipientDto?)
    }
}
