package autotests.tests.actions;

import autotests.clients.ActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckSwimTest extends ActionsClient {

    private static class TestDuck {
        String color = "yellow";
        double height = 0.15;
        String material = "rubber";
        String sound = "quack";
        String wingsState = "FIXED";
    }

    @Test(description = "Проверка того, что уточка поплыла")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        String responseMessage = "{\n" + "  \"message\": \"I'm swimming\"\n" + "}";
        TestDuck testDuck = new TestDuck();
        long idDuck;

        createDuck(runner,
                testDuck.color,
                testDuck.height,
                testDuck.material,
                testDuck.sound,
                testDuck.wingsState);
        idDuck = getIntegerDuckId(runner);
        duckSwim(runner, idDuck);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка ответа на запрос Плыть уточке с несуществующим ID")
    @CitrusTest
    public void duckWithoutIdSwimming(@Optional @CitrusResource TestCaseRunner runner) {
        TestDuck testDuck = new TestDuck();
        long idDuck;

        createDuck(runner,
                testDuck.color,
                testDuck.height,
                testDuck.material,
                testDuck.sound,
                testDuck.wingsState);
        idDuck = getIntegerDuckId(runner) + 1;
        String responseMessage = "{\n"
                + "  \"message\": \"duck with id=" + idDuck + " is not found\"\n" + "}";
        duckSwim(runner, idDuck);
        validateResponse(runner, responseMessage);
    }


}
