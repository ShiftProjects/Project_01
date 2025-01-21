package autotests.tests.actions;

import autotests.clients.ActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckFlyTest extends ActionsClient {
    private static class TestDuck {
        String color = "yellow";
        double height = 2.21;
        String material = "rubber";
        String sound = "quack";
        String wingsState = "FIXED";
    }

    @Test(description = "Проверка того, что уточка полетела")
    @CitrusTest
    public void successfulFly(@Optional @CitrusResource TestCaseRunner runner) {
        String responseMessage = "{\n" + "  \"message\": \"I am flying :)\"\n" + "}";
        TestDuck testDuck = new TestDuck();
        testDuck.wingsState = "ACTIVE";
        long idDuck;

        createDuck(runner,
                testDuck.color,
                testDuck.height,
                testDuck.material,
                testDuck.sound,
                testDuck.wingsState);
        idDuck = getIntegerDuckId(runner);
        duckFly(runner, idDuck);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка того, что уточка не может лететь со связанными крыльями")
    @CitrusTest
    public void successfulNotFly(@Optional @CitrusResource TestCaseRunner runner) {
        String responseMessage = "{\n" + "  \"message\": \"I can not fly :C\"\n" + "}";
        TestDuck testDuck = new TestDuck();
        testDuck.wingsState = "FIXED";
        long idDuck;

        createDuck(runner,
                testDuck.color,
                testDuck.height,
                testDuck.material,
                testDuck.sound,
                testDuck.wingsState);
        idDuck = getIntegerDuckId(runner);
        duckFly(runner, idDuck);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка ответа на запрос Лететь уточке с крыльями в неопределенном состоянии")
    @CitrusTest
    public void successfulUndefinedStateOfWings(@Optional @CitrusResource TestCaseRunner runner) {
        String responseMessage = "{\n" + "  \"message\": \"Wings are not detected :(\"\n" + "}";
        TestDuck testDuck = new TestDuck();
        testDuck.wingsState = "UNDEFINED";
        long idDuck;

        createDuck(runner,
                testDuck.color,
                testDuck.height,
                testDuck.material,
                testDuck.sound,
                testDuck.wingsState);
        idDuck = getIntegerDuckId(runner);
        duckFly(runner, idDuck);
        validateResponse(runner, responseMessage);
    }


}
