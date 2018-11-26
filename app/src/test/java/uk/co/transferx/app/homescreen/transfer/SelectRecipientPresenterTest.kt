package uk.co.transferx.app.homescreen.transfer

import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import uk.co.transferx.app.data.dto.RecipientDto
import uk.co.transferx.app.data.repository.recipientsrepository.RecipientRepository
import uk.co.transferx.app.ui.transfer.presenters.SelectRecipientPresenter
import uk.co.transferx.app.util.schedulers.ImmediateSchedulerProvider

/**
 * Created by Catalin Ghita on 15.11.2018.
 */
class SelectRecipientPresenterTest {
    // SUT
    lateinit var selectRecipientPresenter: SelectRecipientPresenter

    @Mock
    lateinit var recipientRepository: RecipientRepository

    @Mock
    lateinit var view: SelectRecipientPresenter.SelectRecipientView

    // Mock Data
    private val rawListRecipients =
            listOf<RecipientDto>(RecipientDto("4", "Tina", "Turner","4","4","4"),
                    RecipientDto("4", "Abigail", "Johnson","4","4","4") )

    private val orderedListRecipients =
            listOf<RecipientDto>(RecipientDto("4", "Abigail", "Johnson","4","4","4"),
                    RecipientDto("4", "Tina", "Turner","4","4","4"))


    private val filteredListRecipients =
            listOf<RecipientDto>(RecipientDto("4", "Abigail", "Johnson","4","4","4") )


    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        selectRecipientPresenter = SelectRecipientPresenter(recipientRepository,
                ImmediateSchedulerProvider())
    }

    @Test
    fun testAttachView_successResponse() {
        prepareSuccessfulViewAttach()

        // Verify that correct API calls are made at this stage
        Mockito.verify(recipientRepository).recipients

        // Make sure the view acts accordingly by displaying lexicographically ordered list instead
        // of the list that the API has sent
        Mockito.verify(view).showRecipientList(orderedListRecipients)
    }

    @Test
    fun testAttachView_failureResponse() {
        Mockito.`when`(recipientRepository.recipients).thenReturn(Single.error(Throwable()))

        // Attach UI
        selectRecipientPresenter.attachUI(view)

        // Verify that correct API calls are made at this stage
        Mockito.verify(recipientRepository).recipients

        // Make sure the view acts accordingly
        Mockito.verify(view).showError()
    }

    @Test
    fun testShowFilteredResponse() {
        prepareSuccessfulViewAttach()

        // By now, correct API calls have been made, thus let's test the filtered behaviour by
        // querying for Abigail recipient through user search view
        selectRecipientPresenter.displayFilterRecipientDtos("Abigail")

        // Make sure the view acts accordingly by displaying a filtered list
        Mockito.verify(view).showRecipientList(filteredListRecipients)
    }


    @Test
    fun testShowNextTransferStep() {
       prepareSuccessfulViewAttach()

        selectRecipientPresenter.goToNextTransferStep(orderedListRecipients[0])

        // Make sure the view acts accordingly by launching next transfer with correct payload
        Mockito.verify(view).goToNextStep(orderedListRecipients[0])
    }

    @Test
    fun testShowCancel() {
        prepareSuccessfulViewAttach()

        selectRecipientPresenter.cancelTransfer()

        // Make sure the view acts accordingly by canceling transfer activity
        Mockito.verify(view).goBack()
    }

    @Test
    fun testShowAddRecipientScreen() {
        prepareSuccessfulViewAttach()

        selectRecipientPresenter.goToAddRecipient()

        // Make sure the view acts accordingly by canceling transfer activity
        Mockito.verify(view).goToAddRecipient()
    }

    private fun prepareSuccessfulViewAttach(){
        // Prepare successful mock response from API
        Mockito.`when`(recipientRepository.recipients).thenReturn(Single.just(rawListRecipients))

        // Attach UI
        selectRecipientPresenter.attachUI(view)
    }

}