package autotests.tests.crud;

import autotests.clients.CrudClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckUpdateTest extends CrudClient {


    @Test(description = "Проверка изменения цвета и высоты уточки")
    @CitrusTest
    public void successfulUpdateOfColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {

        createDuck(runner, "yellow", 2.21, "wood", "quack", "ACTIVE");
        long idDuck = getIntegerDuckId(runner);

        String responseMessage = "{\n"
                + "  \"message\": \"Duck with id = " + idDuck + " is updated\"\n" + "}";

        duckUpdate(runner, idDuck, "red", 15.67, "wood", "quack", "ACTIVE");
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка изменения цвета и звука уточки")
    @CitrusTest
    public void successfulUpdateOfColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        createDuck(runner, "yellow", 2.21, "wood", "quack", "ACTIVE");
        long idDuck = getIntegerDuckId(runner);

        String responseMessage = "{\n"
                + "  \"message\": \"Duck with id = " + idDuck + " is updated\"\n" + "}";

        duckUpdate(runner, idDuck, "black", 2.21, "wood", "woof", "ACTIVE");
        validateResponse(runner, responseMessage);
    }


}
