package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType; //.contentType(MediaType.APPLICATION_JSON_VALUE)
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.atomic.AtomicLong;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
@Epic("Тесты на duck-action-controller")
public class ActionsClient extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;

    @Autowired
    protected SingleConnectionDataSource testDB;


    //Создание утки в БД
    @Step("SQL запрос создания уточки в БД")
    public void createDuckDB(TestCaseRunner runner, String id,
                             String color, double height,
                             String material, String sound,
                             String wings_state) {
        runner.$(sql(testDB)
                .statement("INSERT INTO duck "
                        + "(id, color, height, material, sound, wings_state)\n"
                        + "VALUES ('" + id + "', '" + color + "', '" + height + "', '"
                        + material + "', '" + sound + "', '" + wings_state + "');"));
    }

    //Удаление утки из БД
    @Step("SQL запрос удаления уточки из БД")
    public void deleteDuckDB(TestCaseRunner runner, String id) {
        runner.$(sql(testDB)
                .statement("DELETE FROM duck WHERE ID=" + id));
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


    //Валидация ответа с передачей ответа String’ой
    @Step("Валидация ответа с передачей ожидаемого ответа String’ой")
    public void validateResponseString(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
    }

    //Валидация ответа с передачей ответа из папки Payload
    @Step("Валидация ответа с передачей ожидаемого ответа из папки Payload")
    public void validateResponsePayload(TestCaseRunner runner, Object body) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    //Валидация ответа с передачей ответа из папки Resources
    @Step("Валидация ответа с передачей ожидаемого ответа из папки Resources")
    public void validateResponseResources(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE) //spring
                .body(new ClassPathResource(responseMessage)));
    }


    //Формирование звука
    @Step("Формирование звука")
    public String getQuackSound(int repetitionCount,
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
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", "${duckId}"));
    }

    //крякай, уточка!
    @Step("GET запрос 'крякай'")
    public void duckQuack(TestCaseRunner runner,
                          int repetitionCount,
                          int soundCount) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", "${duckId}")
                .queryParam("repetitionCount", String.valueOf(repetitionCount))
                .queryParam("soundCount", String.valueOf(soundCount)))
        ;
    }

    //плыви, уточка!
    @Step("GET запрос 'плыви'")
    public void duckSwim(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", "${duckId}"));
    }

    //лети, уточка!
    @Step("GET запрос 'лети'")
    public void duckFly(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", "${duckId}"));
    }

}
