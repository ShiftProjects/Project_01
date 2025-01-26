package autotests.tests.actions;

import autotests.clients.ActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckSwimTest extends ActionsClient {


    @Test(description = "Проверка того, что уточка поплыла")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("yellow")
                .height(2.21)
                .sound("quack")
                .material("rubber")
                .wingsState(WingState.ACTIVE);
        String responseMessage = "actions/getDuckSwimTest/successfulSwim.json";

        createDuck(runner, testDuck);
        setTestVariableDuckId(runner);
        duckSwim(runner);
        validateResponseResources(runner, responseMessage);
    }

    @Test(description = "Проверка ответа на запрос Плыть уточке с несуществующим ID")
    @CitrusTest
    public void duckWithoutIdSwimming(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("yellow")
                .height(2.21)
                .sound("quack")
                .material("rubber")
                .wingsState(WingState.ACTIVE);
        createDuck(runner, testDuck);
        setTestVariableDuckId(runner);
        setIncrementTestVariable(runner, "${duckId}");
        String responseMessage = "{\n"
                + "  \"message\": \"duck with id=" + "${duckId}" + " is not found\"\n" + "}";
        duckSwim(runner);
        validateResponseString(runner, responseMessage);
    }


}
