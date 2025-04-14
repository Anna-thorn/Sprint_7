import api.OrdersApi;
import generators.OrdersGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Orders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderColorParameterizedTest {
    private final String colorOption;
    private final boolean withComment;

    public OrderColorParameterizedTest(String colorOption, boolean withComment) {
        this.colorOption = colorOption;
        this.withComment = withComment;
    }

    @Parameterized.Parameters(name = "Color option: {0}, With comment: {1}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {"BLACK", true},
                {"GREY", false},
                {"BOTH", true},
                {"NONE", false}
        };
    }

    @Test
    @DisplayName("Создание заказа. Проверка поля 'Цвет'")
    @Description("Можно указать один из цветов — BLACK или GREY, или оба цвета")
    public void testCreateOrderWithDifferentColorOptions() {
        Orders order = OrdersGenerator.generateOrder(withComment, false);
        OrdersApi ordersApi = new OrdersApi();

        switch (colorOption) {
            case "BLACK":
                order.setColors(new String[]{"BLACK"});
                break;
            case "GREY":
                order.setColors(new String[]{"GREY"});
                break;
            case "BOTH":
                order.setColors(new String[]{"BLACK", "GREY"});
                break;
            case "NONE":
                break;
        }

        Response response = ordersApi.createOrder(order);

       response.then()
                .assertThat()
                .statusCode(SC_CREATED)
                .body("track", notNullValue())
                .extract()
                .path("track");
    }
}