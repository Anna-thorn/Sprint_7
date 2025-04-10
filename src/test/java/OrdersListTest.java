import api.OrdersApi;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import static org.hamcrest.Matchers.*;

public class OrdersListTest {

    @Test
    @DisplayName("Получение списка заказов")
    @Description("В теле ответа должен возвращается список заказов")
    public void testGetOrdersList() {
        Response response = new OrdersApi().getOrdersList();

        response.then()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}