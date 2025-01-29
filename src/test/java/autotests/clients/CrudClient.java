package autotests.clients;

import autotests.EndpointConfig;
import autotests.payloads.Duck;
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
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
@Epic("Тесты на duck-CRUD-controller")
public class CrudClient extends TestNGCitrusSpringSupport {

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

    //Проверка наличия уточки в БД
    @Step("Проверка наличия уточки в БД")
    protected void validateDuckInDB(TestCaseRunner runner, String id,
                                    String color, double height,
                                    String material, String sound,
                                    String wingsState) {
        runner.$(query(testDB)
                .statement("SELECT * FROM duck WHERE ID=" + id)
                .validate("COLOR", color)
                .validate("HEIGHT", String.valueOf(height))
                .validate("MATERIAL", material)
                .validate("SOUND", sound)
                .validate("WINGS_STATE", wingsState));
    }

    //Проверка отсутствия уточки в БД
    @Step("Проверка отсутствия уточки в БД")
    protected void validateNotPresentDuckInDB(TestCaseRunner runner, String id) {
        runner.$(query(testDB)
                .statement("SELECT id FROM duck WHERE ID=" + id)
                .validate("ID", "NULL"));
    }

    //Создание утки через API
    @Step("POST запрос на создание уточки")
    public void createDuckAPI(TestCaseRunner runner, Object body) {
        runner.$(http().client(duckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper())));
    }


    //Создание тестовой переменной с массивом ID всех уточек
    @Step("Запись массива ID всех уточек из ответа в тестовую переменную")
    public void createTestVariableAllIds(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$", "arr"))
        );
    }

    //Преобразование контекстной переменной в тип long
    @Step("Преобразование контекстной переменной в тип long")
    public long getLongTestVariable(TestCaseRunner runner, String testVariable) {
        AtomicLong id = new AtomicLong();
        runner.$(action -> {
            id.set(Long.parseLong(action.getVariable(testVariable)));
        });
        return id.longValue();
    }

    //Преобразование контекстной переменной в тип String
    @Step("Преобразование контекстной переменной в тип String")
    public String getStringTestVariable(TestCaseRunner runner, String testVariable) {
        StringBuilder str = new StringBuilder();
        runner.$(action -> {
            str.append(action.getVariable(testVariable));
        });
        return str.toString();
    }

    //Преобразование String в массив long
    @Step("Преобразование массива из String в long")
    public long[] stringToLongArr(String str) {
        return Stream.of(str.replaceAll("[\\[\\]\\s]", "")
                .split(",")).mapToLong(Long::parseLong).toArray();
    }


    //Валидация ответа с передачей ответа String’ой
    @Step("Валидация ответа с передачей ожидаемого ответа String’ой")
    public void validateResponseString(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
    }

    //Валидация ответа с передачей ответа String’ой с сохранением ID
    @Step("Валидация ответа с передачей ожидаемого ответа String’ой")
    public void validateResponseStringAndSaveId(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage)
                .extract(fromBody().expression("$.id", "duckId")));
    }

    //Проверка элемента в массиве
    @Step("Проверка элемента в массиве long")
    public boolean elementPresentInArray(long[] array, long element) {
        return Arrays.stream(array).anyMatch(id -> id == element);
    }

    //Присутствие ID утки в списке возвращаемом по запросу getDuckAllIds
    @Step("Присутствие ID утки в полученном списке")
    public boolean presentDuckId(TestCaseRunner runner) {
        getDuckAllIds(runner);
        createTestVariableAllIds(runner); //создаётся тестовая переменная ${arr}
        String strArr = getStringTestVariable(runner, "${arr}"); // strArr из ${arr}
        if (strArr.equals("[]")) return false;
        long[] arrId = stringToLongArr(strArr); //массив ID всех уток
        return elementPresentInArray(arrId, getLongTestVariable(runner, "${duckId}"));
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

    //Валидация ответа с передачей ответа из папки Payload с сохранением ID
    @Step("Валидация ответа с передачей ожидаемого ответа из папки Payload")
    public void validateResponsePayloadAndSaveId(TestCaseRunner runner, Object body) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper()))
                .extract(fromBody().expression("$.id", "duckId")));
    }

    //Валидация ответа с передачей ответа из папки Resources
    @Step("Валидация ответа с передачей ожидаемого ответа из папки Resources")
    public void validateResponseResources(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ClassPathResource(responseMessage)));
    }


    //запрос на изменение уточки
    @Step("PUT запрос на изменение уточки")
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

    //запрос на удаление уточки через API
    @Step("DELETE запрос на удаление уточки")
    public void duckDeleteAPI(TestCaseRunner runner, String id) {
        runner.$(http().client(duckService)
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", id));
    }

    //запрос на получение списка ID всех уточек
    @Step("GET запрос на получение ID всех уточек")
    public void getDuckAllIds(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/getAllIds"));
    }

    //Формирование строки-массива из "${duckId}" элементов
    @Step("Формирование строки в виде массива из n-элементов")
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