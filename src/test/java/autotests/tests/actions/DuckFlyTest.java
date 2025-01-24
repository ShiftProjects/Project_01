package autotests.tests.actions;

import autotests.clients.ActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckFlyTest extends ActionsClient {


    @Test(description = "Проверка того, что уточка полетела")
    @CitrusTest
    public void successfulFly(@Optional @CitrusResource TestCaseRunner runner) {
        String responseMessage = "{\n" + "  \"message\": \"I'm flying\"\n" + "}";

        createDuck(runner, "yellow", 2.21, "rubber", "quack", "ACTIVE");
        getDuckId(runner);
        duckFly(runner);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка того, что уточка не может лететь со связанными крыльями")
    @CitrusTest
    public void successfulNotFly(@Optional @CitrusResource TestCaseRunner runner) {
        String responseMessage = "{\n" + "  \"message\": \"I can't fly\"\n" + "}";

        createDuck(runner, "yellow", 2.21, "rubber", "quack", "FIXED");
        getDuckId(runner);
        duckFly(runner);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка ответа на запрос Лететь уточке с крыльями в неопределенном состоянии")
    @CitrusTest
    public void successfulUndefinedStateOfWings(@Optional @CitrusResource TestCaseRunner runner) {
        String responseMessage = "{\n" + "  \"message\": \"Wings are not detected\"\n" + "}";

        createDuck(runner, "yellow", 2.21, "rubber", "quack", "UNDEFINED");
        getDuckId(runner);
        duckFly(runner);
        validateResponse(runner, responseMessage);
    }


}
