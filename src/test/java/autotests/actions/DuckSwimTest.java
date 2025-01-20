package autotests.actions;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckSwimTest extends TestNGCitrusSpringSupport {

    private static class TestDuck {
        String color = "yellow";
        String height = "0.15";
        String material = "rubber";
        String sound = "quack";
        String wingsState = "FIXED";
    }

    @Test(description = "Проверка того, что уточка поплыла")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        String responseMessage = "{\n" + "  \"message\": \"I'm swimming\"\n" + "}";
        TestDuck testDuck = new TestDuck();

        String idDuck = createDuck(runner, testDuck);
        duckSwim(runner, idDuck);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка ответа на запрос Плыть уточке с несуществующим ID")
    @CitrusTest
    public void duckWithoutIdSwimming(@Optional @CitrusResource TestCaseRunner runner) {
        String idDuck = "123456789";
                String responseMessage = "{\n"
                        + "  \"message\": \"duck with id=" + idDuck + " is not found\"\n" + "}";

        duckSwim(runner, idDuck);
        validateResponse(runner, responseMessage);
    }

    //плыви, уточка!
    public void duckSwim(TestCaseRunner runner, String id) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", id));
    }

    //валидация ответа
    public void validateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
    }

    //Создание утки
    private String createDuck(TestCaseRunner runner, TestDuck testDuck) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" + "  \"color\": \"" + testDuck.color + "\",\n"
                        + "  \"height\": " + testDuck.height + ",\n"
                        + "  \"material\": \"" + testDuck.material + "\",\n"
                        + "  \"sound\": \"" + testDuck.sound + "\",\n"
                        + "  \"wingsState\": \"" + testDuck.wingsState
                        + "\"\n" + "}"));
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
        return "${duckId}";
    }
}
