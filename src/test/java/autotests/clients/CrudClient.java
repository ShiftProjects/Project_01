package autotests.clients;

import autotests.EndpointConfig;
import autotests.payloads.Duck;
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
public class CrudClient extends TestNGCitrusSpringSupport {

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

    //Валидация ответа с передачей ответа String’ой
    public void validateResponseString(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
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


    //запрос на изменение уточки
    public void duckUpdate(TestCaseRunner runner, Duck duck) {
        runner.$(http().client(duckService)
                .send()
                .put("/api/duck/update")
                .queryParam("color", duck.color())
                .queryParam("height", String.valueOf(duck.height()))
                .queryParam("id", "${duckId}")
                .queryParam("material", duck.material())
                .queryParam("sound", duck.sound())
                .queryParam("wingsState", String.valueOf(duck.wingsState())));
    }

    //запрос на удаление уточки
    public void duckDelete(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", "${duckId}"));
    }

    //запрос на получение списка ID всех уточек
    public void getAllIds(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/getAllIds"));
    }

    //Формирование строки-массива из "${duckId}" элементов
    public String arrayString(TestCaseRunner runner) {
        StringBuilder arrString = new StringBuilder("[1");
        long idDuck = getIntegerDuckId(runner);
        for (long i = 2; i <= idDuck; i++) {
            arrString.append(",").append(i);
        }
        arrString.append("]");
        return arrString.toString();
    }

}