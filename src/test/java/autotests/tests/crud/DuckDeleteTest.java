package autotests.tests.crud;

import autotests.clients.CrudClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckDeleteTest extends CrudClient {

    private static class TestDuck {
        String color = "delete";
        double height = 0.15;
        String material = "rubber";
        String sound = "quack";
        String wingsState = "FIXED";
    }

    @Test(description = "Проверка удаления уточки")
    @CitrusTest
    public void successfulDuckDelete(@Optional @CitrusResource TestCaseRunner runner) {
        String responseMessage = "{\n" + "  \"message\": \"Duck is deleted\"\n" + "}";
        TestDuck testDuck = new TestDuck();
        long idDuck;

        createDuck(runner,
                testDuck.color,
                testDuck.height,
                testDuck.material,
                testDuck.sound,
                testDuck.wingsState);
        idDuck = getIntegerDuckId(runner);
        duckDelete(runner, idDuck);
        validateResponse(runner, responseMessage);
    }


}
