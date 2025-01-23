package autotests.tests.actions;

import autotests.clients.ActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckSwimTest extends ActionsClient {


    @Test(description = "Проверка того, что уточка поплыла")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        String responseMessage = "{\n" + "  \"message\": \"I'm swimming\"\n" + "}";
        long idDuck;

        createDuck(runner, "yellow", 2.21, "rubber", "quack", "ACTIVE");
        idDuck = getIntegerDuckId(runner);
        duckSwim(runner, idDuck);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка ответа на запрос Плыть уточке с несуществующим ID")
    @CitrusTest
    public void duckWithoutIdSwimming(@Optional @CitrusResource TestCaseRunner runner) {
        long idDuck;

        createDuck(runner, "yellow", 2.21, "rubber", "quack", "ACTIVE");
        idDuck = getIntegerDuckId(runner) + 1;
        String responseMessage = "{\n"
                + "  \"message\": \"duck with id=" + idDuck + " is not found\"\n" + "}";
        duckSwim(runner, idDuck);
        validateResponse(runner, responseMessage);
    }


}
