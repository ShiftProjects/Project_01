package autotests.tests.actions;

import autotests.clients.ActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.Message;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckFlyTest extends ActionsClient {


    @Test(description = "Проверка того, что уточка полетела")
    @CitrusTest
    public void successfulFly(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("yellow")
                .height(2.21)
                .sound("quack")
                .material("rubber")
                .wingsState(WingState.ACTIVE);

        createDuck(runner, testDuck);
        createTestVariableDuckId(runner);
        duckFly(runner);
        validateResponseResources(runner, "actions/getDuckFlyTest/successfulFly.json");
    }

    @Test(description = "Проверка того, что уточка не может лететь со связанными крыльями")
    @CitrusTest
    public void successfulNotFly(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("yellow")
                .height(2.21)
                .sound("quack")
                .material("rubber")
                .wingsState(WingState.FIXED);

        createDuck(runner, testDuck);
        createTestVariableDuckId(runner);
        duckFly(runner);
        validateResponseResources(runner, "actions/getDuckFlyTest/unsuccessfulFly.json");
    }

    @Test(description = "Проверка ответа на запрос Лететь уточке с крыльями в неопределенном состоянии")
    @CitrusTest
    public void successfulUndefinedStateOfWings(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("yellow")
                .height(2.21)
                .sound("quack")
                .material("rubber")
                .wingsState(WingState.UNDEFINED);
        Message responseMessage = new Message()
                .message("Wings are not detected");

        createDuck(runner, testDuck);
        createTestVariableDuckId(runner);
        duckFly(runner);
        validateResponsePayload(runner, responseMessage);
    }


}
