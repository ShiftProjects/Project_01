package autotests.tests.crud;

import autotests.clients.CrudClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.springframework.test.util.AssertionErrors;
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
        createTestVariableDuckId(runner);
        long id = getLongTestVariable(runner, "${duckId}");
        duckDelete(runner, id);
        validateResponseResources(runner, responseMessage);

        //Проверка, что ID удалённой утки не возвращается по запросу всех ID
        AssertionErrors.assertFalse("Уточка с id = "
                + getStringTestVariable(runner, "${duckId}")
                + " не удалена", presentDuckId(runner));
    }


}
