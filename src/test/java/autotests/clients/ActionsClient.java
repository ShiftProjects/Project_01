package autotests.clients;

import autotests.BaseTest;
import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = {EndpointConfig.class})
public class ActionsClient extends BaseTest {


    //Создание утки в БД
    @Step("SQL запрос создания уточки в БД")
    public void createDuckDB(TestCaseRunner runner, String id,
                             String color, double height,
                             String material, String sound,
                             String wings_state) {
        statementSQL(runner, testDB, "INSERT INTO duck "
                + "(id, color, height, material, sound, wings_state)\n"
                + "VALUES ('" + id + "', '" + color + "', '" + height + "', '"
                + material + "', '" + sound + "', '" + wings_state + "');");
    }

    //Удаление утки из БД
    @Step("SQL запрос удаления уточки из БД")
    public void deleteDuckDB(TestCaseRunner runner, String id) {
        statementSQL(runner, testDB, "DELETE FROM duck WHERE ID=" + id);
    }


    //Валидация ответа с передачей ответа String’ой
    @Step("Валидация ответа с передачей ожидаемого ответа String’ой")
    public void validateResponseString(TestCaseRunner runner, String responseMessage) {
        responseReceiveString(runner, duckService, HttpStatus.OK, MediaType.APPLICATION_JSON_VALUE,
                responseMessage);
    }


    //Валидация ответа с передачей ответа из папки Payload
    @Step("Валидация ответа с передачей ожидаемого ответа из папки Payload")
    public void validateResponsePayload(TestCaseRunner runner, Object body) {
        responseReceivePayload(runner, duckService, HttpStatus.OK, MediaType.APPLICATION_JSON_VALUE,
                new ObjectMappingPayloadBuilder(body, new ObjectMapper()));
    }


    //Валидация ответа с передачей ответа из папки Resources
    @Step("Валидация ответа с передачей ожидаемого ответа из папки Resources")
    public void validateResponseResources(TestCaseRunner runner, String responseMessage) {
        responseReceiveResources(runner, duckService, HttpStatus.OK, MediaType.APPLICATION_JSON_VALUE,
                new ClassPathResource(responseMessage));
    }

    //Генерация случайного числа, чётного (evenFlag = true) или нечётного (evenFlag = false)
    @Step("Генерация чётного/нечётного числа")
    public int generateEvenOddNumber(boolean evenFlag) {
        int randomValue;
        int minValue = 1;
        int maxValue = 1000000000;

        do {
            randomValue = minValue + (int) (Math.random() * (maxValue - minValue + 1));
        }
        while ((randomValue % 2 != 0) == evenFlag);
        return randomValue;
    }


    //Формирование звука
    @Step("Формирование звука")
    public String getSound(int repetitionCount,
                           int soundCount,
                           String sound) {
        StringBuilder quackRepetition = new StringBuilder(sound);
        for (int i = 1; i < soundCount; i++) {
            quackRepetition.append("-").append(sound);
        }
        StringBuilder quackSound = new StringBuilder(quackRepetition);
        for (int i = 1; i < repetitionCount; i++) {
            quackSound.append(", ").append(quackRepetition);
        }
        return quackSound.toString();
    }


    //Показать свойства уточки
    @Step("GET запрос на получение свойств уточки")
    public void duckProperties(TestCaseRunner runner) {
        sendGetRequest(runner, duckService, "/api/duck/action/properties?id=${duckId}");
    }

    //крякай, уточка!
    @Step("GET запрос 'крякай'")
    public void duckQuack(TestCaseRunner runner,
                             int repetitionCount,
                             int soundCount) {
        sendGetRequest(runner, duckService, "/api/duck/action/quack?id=${duckId}" +
                "&repetitionCount=" + repetitionCount +
                "&soundCount=" + soundCount);
    }

    //плыви, уточка!
    @Step("GET запрос 'плыви'")
    public void duckSwim(TestCaseRunner runner) {
        sendGetRequest(runner, duckService, "/api/duck/action/swim?id=${duckId}");
    }

    //лети, уточка!
    @Step("GET запрос 'лети'")
    public void duckFly(TestCaseRunner runner) {
        sendGetRequest(runner, duckService, "/api/duck/action/fly?id=${duckId}");
    }

}
