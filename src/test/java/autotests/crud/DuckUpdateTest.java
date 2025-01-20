package autotests.crud;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckUpdateTest  extends TestNGCitrusSpringSupport {

    private static class TestDuck {
        String color = "green";
        String height = "0.15";
        String material = "rubber";
        String sound = "quack";
        String wingsState = "FIXED";
    }

    @Test(description = "Проверка изменения цвета и высоты уточки")
    @CitrusTest
    public void successfulUpdateOfColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        TestDuck testDuck = new TestDuck();
        AtomicInteger id = new AtomicInteger();

        String idDuck = createDuck(runner, testDuck);
        runner.$(action -> {
            id.set(Integer.parseInt(action.getVariable("duckId")));
        });
        String responseMessage = "{\n"
                + "  \"message\": \"Duck with id = " + id.get() + " is updated\"\n" + "}";

        testDuck.color = "red";
        testDuck.height = "15.67";
        duckUpdate(runner, idDuck, testDuck);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка изменения цвета и звука уточки")
    @CitrusTest
    public void successfulUpdateOfColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        TestDuck testDuck = new TestDuck();
        AtomicInteger id = new AtomicInteger();

        String idDuck = createDuck(runner, testDuck);
        runner.$(action -> {
            id.set(Integer.parseInt(action.getVariable("duckId")));
        });
        String responseMessage = "{\n"
                + "  \"message\": \"Duck with id = " + id.get() + " is updated\"\n" + "}";

        testDuck.color = "black";
        testDuck.sound = "woof";
        duckUpdate(runner, idDuck, testDuck);
        validateResponse(runner, responseMessage);
    }


    //запрос на изменение уточки
    private void duckUpdate(TestCaseRunner runner, String id, TestDuck testDuck) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .put("/api/duck/update")
                        .queryParam("color", testDuck.color)
                        .queryParam("height", testDuck.height)
                        .queryParam("id", id)
                        .queryParam("material", testDuck.material)
                        .queryParam("sound", testDuck.sound)
                        .queryParam("wingsState", testDuck.wingsState));
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
