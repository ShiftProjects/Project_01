package autotests.tests.crud;

import autotests.clients.CrudClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckDeleteTest extends CrudClient {



    @Test(description = "Проверка удаления уточки")
    @CitrusTest
    public void successfulDuckDelete(@Optional @CitrusResource TestCaseRunner runner) {
        String responseMessage = "{\n" + "  \"message\": \"Duck is deleted\"\n" + "}";

        createDuck(runner, "yellow", 2.21, "wood", "quack", "ACTIVE");
        getDuckId(runner);
        duckDelete(runner, "${duckId}");
        validateResponse(runner, responseMessage);
    }


}
