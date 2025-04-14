package generators;

import models.Courier;
import org.apache.commons.lang3.RandomStringUtils;

public class CourierGenerator {// создание рандомного курьера
    /* Требования к полям - взяты из документации к диплому по ручному тестированию
       логин: англ. буквы (2-10 символов)
       пароль: 4 цифры
       имя: англ./рус. буквы (2-10 символов)
     */
    private static final String ENGLISH_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String RUSSIAN_LETTERS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private static final String ALL_LETTERS = ENGLISH_LETTERS + RUSSIAN_LETTERS;

    private static final int LOGIN_MIN_LENGTH = 2;
    private static final int LOGIN_MAX_LENGTH = 10;
    private static final int PASSWORD_LENGTH = 4;
    private static final int FIRST_NAME_MIN_LENGTH = 2;
    private static final int FIRST_NAME_MAX_LENGTH = 10;

    // Генерация случайного курьера со ВСЕМИ полями
    public static Courier createWithAllFields() {
        return new Courier()
                .setLogin(generateEnglishString(LOGIN_MIN_LENGTH, LOGIN_MAX_LENGTH))
                .setPassword(generateDigits(PASSWORD_LENGTH))
                .setFirstName(generateName(FIRST_NAME_MIN_LENGTH, FIRST_NAME_MAX_LENGTH));
    }

    // Генерация курьера только с ОБЯЗАТЕЛЬНЫМИ полями (без firstName)
    public static Courier createWithRequiredFields() {
        return new Courier()
                .setLogin(generateEnglishString(LOGIN_MIN_LENGTH, LOGIN_MAX_LENGTH))
                .setPassword(generateDigits(PASSWORD_LENGTH));
    }

    //--- Вспомогательные методы ---//
    // Генерация случайной строки из английских букв
    private static String generateEnglishString(int minLength, int maxLength) {
        if (minLength > maxLength) {
            throw new IllegalArgumentException("minLength must be <= maxLength");
        }
        int length = minLength + (int)(Math.random() * (maxLength - minLength + 1));
        return RandomStringUtils.randomAlphabetic(length);
    }

    // Генерация строки из цифр (ровно length символов)
    private static String generateDigits(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Password length must be positive");
        }
        return RandomStringUtils.randomNumeric(length);
    }

    // Генерация имени (англ. + рус. буквы)
    private static String generateName(int minLength, int maxLength) {
        if (minLength > maxLength) {
            throw new IllegalArgumentException("minLength must be <= maxLength");
        }
        int length = minLength + (int)(Math.random() * (maxLength - minLength + 1));
        return RandomStringUtils.random(length, ALL_LETTERS);
    }
}