package generators;

import models.Orders;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class OrdersGenerator {
    /* Требования к полям - взяты из документации к диплому по ручному тестированию
       firstName: рус. буквы, пробел, тире (2-15 символов)
       lastName: рус. буквы (2-15 символов)
       address: рус. буквы, цифры, пробел, тире, точка, запятая (2-15 символов)
       metroStation: зашит на стороне бэкенда (в API)
       phone: цифры, плюс (11 символов без +, 12 символов с +)
       rentTime: дата начинается с завтра
       deliveryDate: от 1 до 7 суток
       comment: рус. буквы, цифры, пробел, тире, точка, запятая (0-24 символов) - необязателен
       color: BLACK или GREY - необязателен
     */
    private static final String RUSSIAN_LETTERS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = " -.,";
    private static final String[] COLORS = {"BLACK", "GREY"};

    private static final Random random = new Random();

    public static Orders generateOrder(boolean withComment, boolean withColor) {
        Orders order = new Orders();

        order.setFirstName(generateRussianString(2, 15, true));
        order.setLastName(generateRussianString(2, 15, false));
        order.setAddress(generateAddress());
        order.setMetroStation(random.nextInt(10) + 1); // номер станции (1-10)
        order.setPhone(generatePhone());
        order.setRentTime(random.nextInt(7) + 1); // 1-7 дней
        order.setDeliveryDate(generateDeliveryDate());

        if (withComment) {
            order.setComment(generateComment());
        }

        if (withColor) {
            // 50% chance для одного цвета, 50% для двух
            if (random.nextBoolean()) {
                order.setColors(new String[]{COLORS[random.nextInt(COLORS.length)]});
            } else {
                order.setColors(COLORS.clone()); // оба цвета
            }
        }

        return order;
    }

    // вспомогательные методы
    // генерация для имени/фамилии
    private static String generateRussianString(int minLength, int maxLength, boolean withSpaceAndDash) {
        int length = random.nextInt(maxLength - minLength + 1) + minLength;
        StringBuilder sb = new StringBuilder(length);
        String allowedChars = RUSSIAN_LETTERS;
        if (withSpaceAndDash) allowedChars += " -";
        for (int i = 0; i < length; i++) {
            sb.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
        }
        return sb.toString();
    }

    // генерация адреса
    private static String generateAddress() {
        int length = random.nextInt(14) + 2; // 2-15 символов
        StringBuilder sb = new StringBuilder(length);
        String allowedChars = RUSSIAN_LETTERS + DIGITS + SPECIAL_CHARS;
        for (int i = 0; i < length; i++) {
            sb.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
        }
        return sb.toString();
    }

    // генерация телефона
    private static String generatePhone() {
        boolean withPlus = random.nextBoolean();
        String number = String.format("%010d", random.nextInt(1_000_000_000));
        return withPlus ? "+7" + number : "7" + number;
    }

    // генерация даты доставки
    private static String generateDeliveryDate() {
        return LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE); // yyyy-MM-dd
    }

    // генерация комментария
    private static String generateComment() {
        int length = random.nextInt(25); // 0-24 символа
        if (length == 0) return null;
        StringBuilder sb = new StringBuilder(length);
        String allowedChars = RUSSIAN_LETTERS + DIGITS + SPECIAL_CHARS;
        for (int i = 0; i < length; i++) {
            sb.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
        }
        return sb.toString();
    }
}