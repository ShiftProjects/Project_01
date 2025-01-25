package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.atomic.AtomicInteger;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class ActionsClient extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;


    //Создание утки
    public void createDuck(TestCaseRunner runner, Object body) {
        runner.$(http().client(duckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    //Создание утки с чётным ID (evenFlag = true) или нечётным ID (evenFlag = false)
    public void createDuckEvenId(TestCaseRunner runner, boolean evenFlag, Object body) {
        long id;
        do {
            createDuck(runner, body);
            getDuckId(runner);
            id = getIntegerDuckId(runner);
        }
        while ((id % 2 != 0) == evenFlag);
    }

    //Получение тестовой переменной ID уточки
    public void getDuckId(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
    }

    //Преобразование контекстной переменной "${duckId}" в тип long
    public long getIntegerDuckId(TestCaseRunner runner) {
        AtomicInteger id = new AtomicInteger();
        runner.$(action -> {
            id.set(Integer.parseInt(action.getVariable("${duckId}")));
        });
        return id.longValue();
    }

    //Инкремент контекстной переменной "${duckId}"
    public void setIncrementDuckId(TestCaseRunner runner) {
        runner.$(action -> {
            action.setVariable("${duckId}", getIntegerDuckId(runner) + 1);
        });
    }

    //Валидация ответа с передачей ответа String’ой
    public void validateResponseString(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(responseMessage));
    }

    //Валидация ответа с передачей ответа из папки Payload
    public void validateResponsePayload(TestCaseRunner runner, Object body) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }

    //Валидация ответа с передачей ответа из папки Resources
    public void validateResponseResources(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ClassPathResource(responseMessage)));
    }


    //Формирование звука
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


    //Показать характеристики
    public void duckProperties(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", "${duckId}"));
    }

    //крякай, уточка!
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
    public void duckSwim(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", "${duckId}"));
    }

    //лети, уточка!
    public void duckFly(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", "${duckId}"));
    }

}
