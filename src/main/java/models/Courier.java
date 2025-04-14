package models;

public class Courier {// для создания курьера
    // поля
    private String login;
    private String password;
    private String firstName; // необязательное поле

    // конструктор
    public Courier() {}

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    // геттеры
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public String getFirstName() { return firstName; }

    // сеттеры
    public Courier setLogin(String login) {
        this.login = login;
        return this;
    }
    public Courier setPassword(String password) {
        this.password = password;
        return this;
    }
    public Courier setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
}