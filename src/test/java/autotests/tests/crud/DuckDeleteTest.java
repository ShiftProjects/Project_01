package autotests.tests.crud;

import autotests.clients.CrudClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckDeleteTest extends CrudClient {


    @Test(description = "Проверка удаления уточки")
    @CitrusTest
    public void successfulDuckDelete(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("yellow")
                .height(2.21)
                .sound("quack")
                .material("wood")
                .wingsState(WingState.ACTIVE);
        String responseMessage = "crud/getDuckDeleteTest/successfulDeleted.json";

        createDuck(runner, testDuck);
        getDuckId(runner);
        duckDelete(runner);
        validateResponseResources(runner, responseMessage);
    }


}
