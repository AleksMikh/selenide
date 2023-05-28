import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;


public class CardDeliveryTest {
    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    String planningDate = generateDate(3);

    @Test
    void positiveTest() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(generateDate(3));
        $("[data-test-id=name] input").setValue("Алексеев Михаил");
        $("[data-test-id=phone] input").setValue("+79266565566");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=notification] .notification__content").shouldHave(text("Встреча успешно забронирована на " + generateDate(3)), Duration.ofSeconds(15)).shouldBe(visible);
    }

    @Test
    void emptyCity() {
        open("http://localhost:9999");
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=city].input_invalid").should(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void cityNotSupported() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Арбузякино");
        $("[data-test-id=date] input").doubleClick().sendKeys(generateDate(3));
        $("[data-test-id=name] input").setValue("Алексеев Михаил");
        $("[data-test-id=phone] input").setValue("+79266565566");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void invalidData() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(generateDate(0));
        $("[data-test-id=name] input").setValue("Алексеев Михаил");
        $("[data-test-id=phone] input").setValue("+79266565566");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub").shouldHave(Condition.exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void invalidData2() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys("28.13.2023");
        $("[data-test-id=name] input").setValue("Алексеев Михаил");
        $("[data-test-id=phone] input").setValue("+79266565566");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub").shouldHave(Condition.exactText("Неверно введена дата"));
    }

    @Test
    void emptyName() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(generateDate(3));
        $("[data-test-id=phone] input").setValue("+79266565566");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void invalidName() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(generateDate(3));
        $("[data-test-id=name] input").setValue("Alekseev Mikhail");
        $("[data-test-id=phone] input").setValue("+79266565566");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=name].input_invalid .input__sub").shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void emptyPhone() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(generateDate(3));
        $("[data-test-id=name] input").setValue("Алексеев Михаил");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void invalidPhone() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(generateDate(3));
        $("[data-test-id=name] input").setValue("Алексеев Михаил");
        $("[data-test-id=phone] input").setValue("+792665655666666");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void emptyCheckbox() {
        open("http://localhost:9999");
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick().sendKeys(generateDate(3));
        $("[data-test-id=name] input").setValue("Алексеев Михаил");
        $("[data-test-id=phone] input").setValue("+79266565566");
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=agreement].input_invalid .checkbox__text").shouldHave(Condition.exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

}
