package models;

import api.CourierApi;
import io.restassured.response.Response;

public class CourierAuthService { //для получения id курьера из БД
    private final CourierApi courierApi;

    public CourierAuthService(CourierApi courierApi) {
        this.courierApi = courierApi;
    }

    public int getCourierId(CourierCredentials credentials) {
        Response loginResponse = courierApi.loginCourier(credentials);
        if (loginResponse.statusCode() != 200) {
            throw new RuntimeException("Не удалось авторизовать курьера. Код ответа: " + loginResponse.statusCode());
        }
        int courierId = loginResponse.jsonPath().getInt("id");

        if (courierId <= 0) {
            throw new RuntimeException("Некорректный ID курьера: " + courierId);
        }
        return courierId;
    }
}