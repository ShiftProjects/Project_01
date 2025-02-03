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

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

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

    //Создание тестовой переменной ID уточки
    public void createTestVariableDuckId(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
    }

    //Создание тестовой переменной с массивом ID всех уточек
    public void createTestVariableAllIds(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$", "arr"))
        );
    }

    //Преобразование контекстной переменной в тип long
    public long getLongTestVariable(TestCaseRunner runner, String testVariable) {
        AtomicLong id = new AtomicLong();
        runner.$(action -> {
            id.set(Long.parseLong(action.getVariable(testVariable)));
        });
        return id.longValue();
    }

    //Преобразование контекстной переменной в тип String
    public String getStringTestVariable(TestCaseRunner runner, String testVariable) {
         StringBuilder str = new StringBuilder();
        runner.$(action -> {
            str.append(action.getVariable(testVariable));
        });
        return str.toString();
    }

    //Преобразование String в массив long
    public long[] stringToLongArr(String str){
        return Stream.of(str.replaceAll("[\\[\\]\\s]", "")
                .split(",")).mapToLong(Long::parseLong).toArray();
    }


    //Валидация ответа с передачей ответа String’ой
    public void validateResponseString(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
    }

    //Проверка элемента в массиве
    public boolean elementPresentInArray(long[] array, long element){
        return Arrays.stream(array).anyMatch(id -> id == element);
    }

    //Присутствие ID утки в списке возвращаемом по запросу getDuckAllIds
    public boolean presentDuckId(TestCaseRunner runner) {
        getDuckAllIds(runner);
        createTestVariableAllIds(runner); //создаётся тестовая переменная ${arr}
        String strArr = getStringTestVariable(runner, "${arr}"); // strArr из ${arr}
        long[] arrId = stringToLongArr(strArr); //массив ID всех уток
        return elementPresentInArray(arrId, getLongTestVariable(runner, "${duckId}"));
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
    public void duckDelete(TestCaseRunner runner, long id) {
        runner.$(http().client(duckService)
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", String.valueOf(id)));
    }

    //запрос на получение списка ID всех уточек
    public void getDuckAllIds(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/getAllIds"));
    }

    //Формирование строки-массива из "${duckId}" элементов
    public String arrayString(TestCaseRunner runner) {
        StringBuilder arrString = new StringBuilder("[1");
        long idDuck = getLongTestVariable(runner, "${duckId}");
        for (long i = 2; i <= idDuck; i++) {
            arrString.append(",").append(i);
        }
        arrString.append("]");
        return arrString.toString();
    }

}