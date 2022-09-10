package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class RZDTests {

  //1
  @ValueSource(strings = {"Пассажирам", "Грузовые перевозки", "Компания", "Работа в РЖД", "Контакты"})
  @ParameterizedTest(name = "При открытии раздела {0} доступны кнопки для работы с сайтом")
  void checkHeader(String menuSection) {
    open("https://rzd.ru/");
    $$(".header_site li").find(text(menuSection)).click();
    $(".header_specials").shouldBe(visible);
  }

  //2
  @CsvSource(value = {
          "Пенза, Казань, 08.10.2022",
          "Пенза, Москва, 30.09.2022"
  })
  @ParameterizedTest(name = "Проверка что есть поезда по маршруту {0} - {1} {2}")
  void checkTimetable(String cityFrom, String cityTo, String dateFrom) {
    open("https://rzd.ru/");
    $$(".header_site li").find(text("Пассажирам")).click();
    $("#direction-from").setValue(cityFrom);
    $("#direction-to").setValue(cityTo);
    $("#datepicker-from").setValue(dateFrom);
    $(".rzd-button").click();
    $$(".results-cards rzd-search-results-card-railway-flat-card")
            .shouldBe(CollectionCondition.sizeGreaterThan(0));
  }

  //3
  static Stream<Arguments> dataProviderForCheckMenuSection() {
    return Stream.of(
            Arguments.of("Пассажирам",
                    List.of("Пассажирам", "Грузовые перевозки", "Компания", "Работа в РЖД", "Контакты"),
                    List.of("Купить билет", "Поезда и маршруты", "Вокзалы", "Тарифы и акции", "Услуги и сервисы",
                            "РЖД Бонус", "Правила", "Информация", "Карта сайта")),
            Arguments.of("Версия для слабовидящих",
                    List.of("Пассажирам", "Грузовые перевозки", "Компания", "Работа в РЖД", "Контакты"),
                    List.of("Купить билет", "Поезда и маршруты", "Вокзалы", "Тарифы и акции", "Услуги и сервисы",
                            "РЖД Бонус", "Правила", "Информация", "Карта сайта"))
    );
  }

  @MethodSource("dataProviderForCheckMenuSection")
  @ParameterizedTest(name = "Проверка отображения разделов {1} и {2} меню для режима {0}")
  void checkMenuSection(String version, List<String> menuHeader, List<String> menuActions) {
    open("https://rzd.ru/");
    $(".header").$(byText(version)).click();
    $$(".header_site li").shouldHave(texts(menuHeader));
    $$(".header_actions li").filter(visible).shouldHave(texts(menuActions));
  }

}
