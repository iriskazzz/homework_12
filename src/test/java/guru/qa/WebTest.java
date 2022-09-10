package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@DisplayName("Веб тесты")
public class WebTest {

  //@Disabled("SD-5678") - выключаем тест и указываем причину/номер бага
  //@DisplayName("Результаты поиска не пустые для запроса 'Selenide'")

  @ValueSource(strings = {"Selenide", "Allure java"})
  @ParameterizedTest(name = "Результаты поиска не пустые для запроса {0}")
    //Параметизированный тест
  void commonSearchTest(String testData) {
    open("https://ya.ru");
    $("#text").setValue(testData);
    $("button[type='submit']").click();
    $$("li.serp-item").shouldBe(CollectionCondition.sizeGreaterThan(0));
  }

  @CsvSource(value = {
          "Selenide,  это фреймворк для автоматизированного тестирования веб-приложений",
          "Allure java, framework успешно применяется в работе автоматизатора"
  })
  @ParameterizedTest(name = "Результаты поиска содержат текст \"{1}\" для запроса \"{0}\"")
  void commonComplexSearchTest(String testData, String expectedResult) {
    open("https://ya.ru");
    $("#text").setValue(testData);
    $("button[type='submit']").click();
    $$("li.serp-item").filter(not(text("Реклама")))
            .first()
            .shouldHave(text(expectedResult));
  }

  static Stream<Arguments> dataProviderForSelenideSiteMenuTest() {
    return Stream.of(
            Arguments.of(Lang.EN, List.of("Quick start", "Docs", "FAQ", "Blog", "Javadoc", "Users", "Quotes")),
            Arguments.of(Lang.RU, List.of("С чего начать?", "Док", "ЧАВО", "Блог", "Javadoc", "Пользователи", "Отзывы"))
    );
  }

  @MethodSource("dataProviderForSelenideSiteMenuTest")
  @ParameterizedTest(name = "Для локали {0} отображаются кнопки {1}")
  void selenideSiteMenuTest(Lang lang, List<String> expectedButtons) {
    open("https://selenide.org");
    $$("#languages a").find(text(lang.name())).click();
    $$(".main-menu-pages a")
            .filter(visible)
            .shouldHave(texts(expectedButtons));
  }

  @EnumSource(Lang.class)
  @ParameterizedTest(name = "Для локали {0} отображается картинка")
  void selenideSiteEnumMenuTest(Lang lang) {
    open("https://selenide.org");
    $$("#languages a").find(text(lang.name())).click();
    $("#selenide-logo").shouldBe(visible);
  }
}
