package api;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import models.Courier;
import models.CourierCredentials;
import static io.restassured.RestAssured.given;

public class CourierApi { // методы API для курьера
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    // ручки для работы с курьером
    private static final String CREATE_ENDPOINT = "/api/v1/courier"; // post
    private static final String LOGIN_ENDPOINT = "/api/v1/courier/login"; //post
    private static final String DELETE_ENDPOINT = "/api/v1/courier/"; // :id delete

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(courier)
                .when()
                .post(CREATE_ENDPOINT);
    }

    @Step("Логин курьера в системе") // возвращает ID для удаления
    public Response loginCourier(CourierCredentials credentials) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(credentials)
                .when()
                .post(LOGIN_ENDPOINT);
    }

    @Step("Удаление курьера")
    public Response deleteCourier(int courierId) {
        return given()
                .filter(new AllureRestAssured())
                .baseUri(BASE_URL)
                .contentType("application/json")
                .when()
                .delete(DELETE_ENDPOINT + courierId);
    }
}