package autotests.actions;

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

public class DuckPropertiesTest extends TestNGCitrusSpringSupport {
    private static class TestDuck {
        String color = "black";
        String height = "1.5";
        String material = "rubber";
        String sound = "quack";
        String wingsState = "FIXED";
    }

    @Test(description = "Проверка получения характеристик утки с material = wood")
    @CitrusTest
    public void successfulPropertiesWoodDuck(@Optional @CitrusResource TestCaseRunner runner){
        TestDuck testDuck = new TestDuck();
        testDuck.material = "wood";
        String idDuck;
        AtomicInteger id = new AtomicInteger();

        //Гарантированное создание утки с чётным ID
        do {
            idDuck = createDuck(runner, testDuck);
            runner.$(action -> {
                id.set(Integer.parseInt(action.getVariable("duckId")));
            });
        }
        while (id.get() % 2 != 0);
        duckProperties(runner, idDuck);
        validateResponse(runner,
                "{\n" + "  \"color\": \"" + testDuck.color + "\",\n"
                        + "  \"height\": " + testDuck.height + ",\n"
                        + "  \"material\": \"" + testDuck.material + "\",\n"
                        + "  \"sound\": \"" + testDuck.sound + "\",\n"
                        + "  \"wingsState\": \"" + testDuck.wingsState
                        + "\"\n" + "}");
    }

    @Test(description = "Проверка получения характеристик утки с material = rubber")
    @CitrusTest
    public void successfulPropertiesRubberDuck(@Optional @CitrusResource TestCaseRunner runner){
        TestDuck testDuck = new TestDuck();
        testDuck.material = "rubber";
        String idDuck;
        AtomicInteger id = new AtomicInteger();

        //Гарантированное создание утки с нечётным ID
        do {
            idDuck = createDuck(runner, testDuck);
            runner.$(action -> {
                id.set(Integer.parseInt(action.getVariable("duckId")));
            });
        }
        while (id.get() % 2 == 0);
        duckProperties(runner, idDuck);
        validateResponse(runner,
                "{\n" + "  \"color\": \"" + testDuck.color + "\",\n"
                        + "  \"height\": " + testDuck.height + ",\n"
                        + "  \"material\": \"" + testDuck.material + "\",\n"
                        + "  \"sound\": \"" + testDuck.sound + "\",\n"
                        + "  \"wingsState\": \"" + testDuck.wingsState
                        + "\"\n" + "}");
    }

    //Показать характеристики
    public void duckProperties(TestCaseRunner runner, String id) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id));
    }

    //Валидация ответа
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
