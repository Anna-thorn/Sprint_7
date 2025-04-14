package api;

import io.restassured.response.Response;
import models.Orders;

import static io.restassured.RestAssured.given;

public class OrdersApi {
    private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";
    private static final String CREATE_ORDER_ENDPOINT = "/api/v1/orders"; // post
    private static final String GET_ORDERS_ENDPOINT = "/api/v1/orders"; // get

    public Response createOrder(Orders orders) {
        return given()
                .baseUri(BASE_URL)
                .contentType("application/json")
                .body(orders)
                .when()
                .post(CREATE_ORDER_ENDPOINT);
    }
    public Response getOrdersList() {
        return given()
                .baseUri(BASE_URL)
                .when()
                .get(GET_ORDERS_ENDPOINT);
    }
}