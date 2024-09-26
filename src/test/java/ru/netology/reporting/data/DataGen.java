package ru.netology.reporting.data;

import lombok.Value;
import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.Random;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class DataGen {
    private DataGen() {
    }

    public static String dateGen(int shift) {
        return LocalDate.now().plusDays(shift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public static String cityGen() {
        var cities = new String[]{"Москва", "Санкт-Петербург", "Новосибирск", "Екатеринбург", "Нижний Новгород",
                "Казань", "Челябинск", "Омск", "Ростов-на-Дону", "Уфа", "Самара", "Красноярск", "Воронеж",
                "Пермь", "Волгоград", "Краснодар", "Тула", "Ижевск", "Ульяновск", "Барнаул"};
        return cities[new Random().nextInt(cities.length)];
    }

    public static String nameGen(String locale) {
        var faker = new Faker(new Locale(locale));
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String phoneGen(String locale) {
        var faker = new Faker(new Locale(locale));
        return faker.phoneNumber().phoneNumber();
    }

    public static String genWrongPhone(String locale) {
        var faker = new Faker(new Locale(locale));
        return faker.phoneNumber().phoneNumber();
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo userGen(String locale) {
            return new UserInfo(cityGen(), nameGen(locale), phoneGen(locale));
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}