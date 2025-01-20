package autotests.crud;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckCreateTest extends TestNGCitrusSpringSupport {

    private static class TestDuck {
        String color = "yellow";
        String height = "0.3";
        String material = "rubber";
        String sound = "quack";
        String wingsState = "FIXED";
    }

    @Test(description = "Проверка создания утки с material = rubber")
    @CitrusTest
    public void successfulCreationRubberDuck(@Optional @CitrusResource TestCaseRunner runner){
        TestDuck testDuck = new TestDuck();
        testDuck.material = "rubber";
        createDuck(runner, testDuck);
        validateResponse(runner, testDuck);
    }

    @Test(description = "Проверка создания утки с material = wood")
    @CitrusTest
    public void successfulCreationWoodDuck(@Optional @CitrusResource TestCaseRunner runner){
        TestDuck testDuck = new TestDuck();
        testDuck.material = "wood";
        createDuck(runner, testDuck);
        validateResponse(runner, testDuck);
    }



    //Валидация ответа
    public void validateResponse(TestCaseRunner runner, TestDuck testDuck) {
        String responseMessage = "{\n"
                + "  \"id\": \"@ignore@\"" + ",\n"
                + "  \"color\": \"" + testDuck.color + "\",\n"
                + "  \"height\": " + testDuck.height + ",\n"
                + "  \"material\": \"" + testDuck.material + "\",\n"
                + "  \"sound\": \"" + testDuck.sound + "\",\n"
                + "  \"wingsState\": \"" + testDuck.wingsState
                + "\"\n" + "}";
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
    }

    //Создание утки
    private void createDuck(TestCaseRunner runner, TestDuck testDuck) {
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
    }
}
