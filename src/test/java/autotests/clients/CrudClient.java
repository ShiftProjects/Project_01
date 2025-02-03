package autotests.clients;

import autotests.BaseTest;
import autotests.EndpointConfig;
import autotests.payloads.Duck;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;

@ContextConfiguration(classes = {EndpointConfig.class})
public class CrudClient extends BaseTest {


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

    //Проверка наличия уточки в БД
    @Step("Проверка наличия уточки в БД")
    public void validateDuckInDB(TestCaseRunner runner, String id,
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
    public void validateNotPresentDuckInDB(TestCaseRunner runner, String id) {
        runner.$(query(testDB)
                .statement("SELECT COUNT(*) FROM duck WHERE ID=" + id)
                .validate("COUNT(*)", "0"));
    }

    //Валидация ответа с передачей ответа String’ой
    @Step("Валидация ответа с передачей ожидаемого ответа String’ой")
    public void validateResponseString(TestCaseRunner runner, String responseMessage) {
        responseReceiveString(runner, duckService, HttpStatus.OK, MediaType.APPLICATION_JSON_VALUE,
                responseMessage);
    }

    //Валидация ответа с передачей ответа String’ой с сохранением ID
    @Step("Валидация ответа с передачей ожидаемого ответа String’ой")
    public void validateResponseStringAndSaveId(TestCaseRunner runner, String responseMessage) {
        responseReceiveStringAndSaveId(runner, duckService, HttpStatus.OK, MediaType.APPLICATION_JSON_VALUE,
                responseMessage, "$.id", "duckId");
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


    //Создание утки через API с передачей тела из папки Payload
    @Step("POST запрос на создание уточки")
    public void createDuckAPI(TestCaseRunner runner, Object body) {
        postSendAPIPayload(runner, duckService, "/api/duck/create",
                MediaType.APPLICATION_JSON_VALUE,
                new ObjectMappingPayloadBuilder(body, new ObjectMapper()));
    }


    //запрос на изменение уточки
    @Step("PUT запрос на изменение уточки")
    public void duckUpdate(TestCaseRunner runner, Duck duck) {
        putSendAPI(runner, duckService,
                "/api/duck/update?color=" + duck.color() +
                        "&height=" + duck.height() + "&id=${duckId}" +
                        "&material=" + duck.material() + "&sound=" + duck.sound() +
                        "&wingsState=" + duck.wingsState());
    }

    //запрос на удаление уточки через API
    @Step("DELETE запрос на удаление уточки")
    public void duckDeleteAPI(TestCaseRunner runner, String id) {
        deleteSendAPI(runner, duckService, "/api/duck/delete?id=" + id);
    }

    //запрос на получение списка ID всех уточек
    @Step("GET запрос на получение ID всех уточек")
    public void getDuckAllIds(TestCaseRunner runner) {
        sendGetRequest(runner, duckService, "/api/duck/getAllIds");
    }


}