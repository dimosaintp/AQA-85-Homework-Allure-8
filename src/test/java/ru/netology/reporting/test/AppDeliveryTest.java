package ru.netology.reporting.test;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.reporting.data.DataGen;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class AppDeliveryTest {
    private final DataGen.UserInfo validUser = DataGen.Registration.userGen("ru");
    private final int daysToAddForFirstMeeting = 4;
    private final String firstMeetingDate = DataGen.dateGen(daysToAddForFirstMeeting);

    @BeforeAll
    static void setUpAll () {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll () {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan meeting")
    void shouldSuccessfulPlanMeeting() {
        var validUser = DataGen.Registration.userGen("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGen.dateGen(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGen.dateGen(daysToAddForSecondMeeting);
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(String.valueOf(validUser.getName()));
        $("[data-test-id='phone'] input").setValue(String.valueOf(validUser.getPhone()));
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text(" Встреча успешно запланирована на  " + firstMeetingDate));
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();
        $("[data-test-id='replan-notification']").shouldBe(visible);
        $(withText("Перепланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content").shouldBe(visible)
                .shouldBe(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification'] .notification__content").shouldBe(visible)
                .shouldHave(text("Встреча успешно запланирована на  " + secondMeetingDate));
    }

    @Test
    @DisplayName("Should get error if enter incorrect phone number")
    void ShouldGetErrorIfEnterIncorrectPhoneNumber() {
        $("[data-test-id='city'] input").setValue(validUser.getCity());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(String.valueOf(validUser.getName()));
        $("[data-test-id='phone'] input").setValue(DataGen.genWrongPhone("en"));
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $("[data-test-id='phone'] .input_invalid .input__sub").shouldHave(exactText("Неверный формат номера мобильного телефона"));
    }
}