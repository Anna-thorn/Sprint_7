package models;

public class CourierCredentials {// для авторизации курьера
    // поля
    private String login;
    private String password;

    // конструктор
    public CourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }
    public static CourierCredentials credentialsFromCourier(Courier courier) {
        return new CourierCredentials(courier.getLogin(), courier.getPassword());
    }
}