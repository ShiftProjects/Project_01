package autotests.tests.crud;

import autotests.clients.CrudClient;
import autotests.payloads.Duck;
import autotests.payloads.Message;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckUpdateTest extends CrudClient {


    @Test(description = "Проверка изменения цвета и высоты уточки")
    @CitrusTest
    public void successfulUpdateOfColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("yellow")
                .height(2.21)
                .sound("quack")
                .material("wood")
                .wingsState(WingState.ACTIVE);

        createDuck(runner, testDuck);
        createTestVariableDuckId(runner);

        testDuck.color("black");
        testDuck.height(15.81);

        Message responseMessage = new Message()
                .message("Duck with id = " + "${duckId}" + " is updated");

        duckUpdate(runner, testDuck);
        validateResponsePayload(runner, responseMessage);
    }

    @Test(description = "Проверка изменения цвета и звука уточки")
    @CitrusTest
    public void successfulUpdateOfColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("yellow")
                .height(2.21)
                .sound("quack")
                .material("wood")
                .wingsState(WingState.ACTIVE);

        createDuck(runner, testDuck);
        createTestVariableDuckId(runner);

        testDuck.color("black");
        testDuck.sound("woof");

        String responseMessage = "{\n"
                + "  \"message\": \"Duck with id = " + "${duckId}" + " is updated\"\n" + "}";

        duckUpdate(runner, testDuck);
        validateResponseString(runner, responseMessage);
    }


}
