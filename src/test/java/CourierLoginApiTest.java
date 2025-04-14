import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;
import models.Courier;
import models.CourierCredentials;
import generators.CourierGenerator;
import api.CourierApi;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CourierLoginApiTest {
    private static final String INSUFFICIENT_DATA_FOR_LOGIN = "Недостаточно данных для входа";
    private static final String ACCOUNT_NOT_FOUND_MSG = "Учетная запись не найдена";

    private CourierApi courierApi;
    private int createdCourierId;
    private Courier testCourier;

    @Before
    public void setUp() {
        courierApi = new CourierApi();
        testCourier = CourierGenerator.createWithAllFields();
        Response createResponse = courierApi.createCourier(testCourier);
        if (createResponse.statusCode() == SC_CREATED) {
            createdCourierId = courierApi.loginCourier(
                            CourierCredentials.credentialsFromCourier(testCourier))
                    .jsonPath()
                    .getInt("id");
        }
    }

    @Test
    @DisplayName("Авторизация зарегистрированного курьера")
    @Description("Запрос c существующей парой логин-пароль")
    public void entranceForRegisteredCourier() {
        Response response = courierApi.loginCourier(
                CourierCredentials.credentialsFromCourier(testCourier));
        assertEquals(SC_OK, response.statusCode());
        assertTrue(response.jsonPath().getInt("id") > 0);
    }

    @Test
    @DisplayName("Авторизация незарегистрированного курьера")
    @Description("Запрос c несуществующей парой логин-пароль")
    public void entranceForUnregisteredCourier() {
        Response response = attemptLogin("nonexistent_login", "wrong_password");
        verifyFailedLogin(response, SC_NOT_FOUND, ACCOUNT_NOT_FOUND_MSG);
    }

    @Test
    @DisplayName("Авторизация курьера без логина")
    @Description("Попытка входа без логина вызывает ошибку")
    public void loginWithoutLoginField() {
        Response response = attemptLogin(null, "valid_password");
        verifyFailedLogin(response, SC_BAD_REQUEST, INSUFFICIENT_DATA_FOR_LOGIN);
    }

    @Test
    @DisplayName("Авторизация курьера без пароля")
    @Description("Попытка входа без пароля вызывает ошибку")
    public void loginWithoutPasswordField() {
        Response response = attemptLogin(testCourier.getLogin(), "");
        verifyFailedLogin(response, SC_BAD_REQUEST, INSUFFICIENT_DATA_FOR_LOGIN);
    }

    private Response attemptLogin(String login, String password) {
        return courierApi.loginCourier(new CourierCredentials(login, password));
    }

    private void verifyFailedLogin(Response response, int expectedCode, String expectedMessage) {
        assertEquals(expectedCode, response.statusCode());
        assertEquals(expectedMessage, response.jsonPath().getString("message"));
    }

    @After
    public void tearDown() {
        if (createdCourierId != 0) {
            courierApi.deleteCourier(createdCourierId);
        }
    }
}