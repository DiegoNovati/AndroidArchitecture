package uk.co.itmms.specifications.steps

import cucumber.api.PendingException
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When
import junit.framework.TestCase.fail


class StepsLogin {

    @Given("^I have set to use the fake backend$")
    @Throws(Throwable::class)
    fun iHaveSetToUseTheFakeBackend() {

    }

    @When("^I login with \"([^\"]*)\" and \"([^\"]*)\"$")
    @Throws(Throwable::class)
    fun iLoginWithAnd(username: String, password: String) {
    }

    @Then("^the login is successful$")
    @Throws(Throwable::class)
    fun theLoginIsSuccessful() {
        fail("Not implemented")
    }
}