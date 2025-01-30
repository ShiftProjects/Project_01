package autotests.clients;

import autotests.BaseTest;
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
public class ActionsClient extends BaseTest {


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
