package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class CrudClient extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient duckService;


    //Создание утки
    public void createDuck(TestCaseRunner runner,
                           String color,
                           double height,
                           String material,
                           String sound,
                           String wingsState) {
        runner.$(http().client(duckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" + "  \"color\": \"" + color + "\",\n"
                        + "  \"height\": " + height + ",\n"
                        + "  \"material\": \"" + material + "\",\n"
                        + "  \"sound\": \"" + sound + "\",\n"
                        + "  \"wingsState\": \"" + wingsState
                        + "\"\n" + "}"));
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

    //Валидация ответа
    public void validateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
    }

    //запрос на изменение уточки
    public void duckUpdate(TestCaseRunner runner,
                           String color,
                           double height,
                           String material,
                           String sound,
                           String wingsState) {
        runner.$(http().client(duckService)
                .send()
                .put("/api/duck/update")
                .queryParam("color", color)
                .queryParam("height", String.valueOf(height))
                .queryParam("id", "${duckId}")
                .queryParam("material", material)
                .queryParam("sound", sound)
                .queryParam("wingsState", wingsState));
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