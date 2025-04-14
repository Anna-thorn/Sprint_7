import io.qameta.allure.Issue;
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

public class CourierCreationApiTest {
    private static final String INSUFFICIENT_DATA_FOR_CREATION = "Недостаточно данных для создания учетной записи";
    private static final String DUPLICATE_LOGIN_MESSAGE = "Этот логин уже используется";

    private CourierApi courierApi;
    private int createdCourierId;

    @Before
    public void setUp() {
        courierApi = new CourierApi();
    }

    // проверка ручки post/api/v1/courier
    @Test
    @DisplayName("Создание курьера (заполнены все поля)")
    @Description("Поля заполнены валидными значениями")
    public void createCourierSuccessfully() {
        Courier courier = CourierGenerator.createWithAllFields(); // генерация тестового курьера
        createAndVerifyCourier(courier);
    }

    @Test
    @DisplayName("Создание курьера (только обязательные поля)")
    @Description("Поля заполнены валидными значениями")
    public void createCourierWithRequiredFieldsSuccessfully() {
        Courier courier = CourierGenerator.createWithRequiredFields(); // генерация тестового курьера
        createAndVerifyCourier(courier);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Попытка создания курьера с уже существующим логином должна возвращать ошибку")
    @Issue("13m-155")
    public void cannotCreateDuplicateCourier() {
        Courier firstCourier = CourierGenerator.createWithAllFields();
        Response firstResponse = courierApi.createCourier(firstCourier);
        assertEquals(SC_CREATED, firstResponse.statusCode());

        // задержка перед дублированием (1-2 секунды)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Courier duplicateCourier = new Courier()
                .setLogin(firstCourier.getLogin())
                .setPassword(firstCourier.getPassword())
                .setFirstName(firstCourier.getFirstName());

        Response duplicateResponse = courierApi.createCourier(duplicateCourier);
        assertEquals("Ожидается 409 Conflict", SC_CONFLICT, duplicateResponse.statusCode());
        assertEquals(DUPLICATE_LOGIN_MESSAGE, duplicateResponse.jsonPath().getString("message"));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Попытка создания курьера без логина должна возвращать ошибку")
    public void cannotCreateCourierWithoutLogin() {
        Courier courier = CourierGenerator.createWithAllFields().setLogin(null); // генерация тестового курьера без логина
        createAndVerifyCourierWithoutLoginOrPassword(courier);
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Попытка создания курьера без пароля должна возвращать ошибку")
    public void cannotCreateCourierWithoutPassword() {
        Courier courier = CourierGenerator.createWithAllFields().setPassword(null); // генерация тестового курьера без пароля
        createAndVerifyCourierWithoutLoginOrPassword(courier);
    }

    @Test
    @DisplayName("Создание курьера без логина и пароля")
    @Description("Попытка создания курьера без логина и пароля возвращать ошибку")
    public void cannotCreateCourierWithoutRequiredFields(){
        Courier courier = CourierGenerator.createWithAllFields().setLogin(null).setPassword(null); // генерация тестового курьера без обязательных полей
        createAndVerifyCourierWithoutLoginOrPassword(courier);
    }

    // вспомогательный метод для создания курьера
    private void createAndVerifyCourier(Courier courier) {
    Response createResponse = courierApi.createCourier(courier); // создание курьера через API
    // проверки кода и тела ответа
    assertEquals("Код ответа должен быть '201 Created'",
                 SC_CREATED,
                 createResponse.statusCode());
    assertTrue("Тело ответа должно содержать 'ok: true'",
               createResponse.jsonPath().getBoolean("ok"));
    // получение ID через авторизацию и сохранение
        Response loginResponse = courierApi.loginCourier(
                CourierCredentials.credentialsFromCourier(courier)
        );
        createdCourierId = loginResponse.jsonPath().getInt("id");
    }

    // вспомогательный метод для создания курьера без логина или пароля
    private void createAndVerifyCourierWithoutLoginOrPassword(Courier courier) {
        Response createResponse = courierApi.createCourier(courier); // создание курьера через API
        // проверки кода и тела ответа
        assertEquals("Неверный код ответа",
                SC_BAD_REQUEST,
                createResponse.statusCode());
        assertEquals("Неверное сообщение об ошибке",
                INSUFFICIENT_DATA_FOR_CREATION,
                createResponse.jsonPath().getString("message"));
    }

    @After
    public void tearDown() {
        if (createdCourierId != 0) {
            courierApi.deleteCourier(createdCourierId);
        }
    }
}