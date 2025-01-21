package autotests.tests.crud;

import autotests.clients.CrudClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckUpdateTest extends CrudClient {

    private static class TestDuck {
        String color = "green";
        double height = 0.15;
        String material = "rubber";
        String sound = "quack";
        String wingsState = "FIXED";
    }

    @Test(description = "Проверка изменения цвета и высоты уточки")
    @CitrusTest
    public void successfulUpdateOfColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        TestDuck testDuck = new TestDuck();
        long idDuck;

        createDuck(runner,
                testDuck.color,
                testDuck.height,
                testDuck.material,
                testDuck.sound,
                testDuck.wingsState);
        idDuck = getIntegerDuckId(runner);

        String responseMessage = "{\n"
                + "  \"message\": \"Duck with id = " + idDuck + " is updated\"\n" + "}";

        testDuck.color = "red";
        testDuck.height = 15.67;
        duckUpdate(runner,
                idDuck,
                testDuck.color,
                testDuck.height,
                testDuck.material,
                testDuck.sound,
                testDuck.wingsState);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка изменения цвета и звука уточки")
    @CitrusTest
    public void successfulUpdateOfColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        TestDuck testDuck = new TestDuck();
        long idDuck;

        createDuck(runner,
                testDuck.color,
                testDuck.height,
                testDuck.material,
                testDuck.sound,
                testDuck.wingsState);
        idDuck = getIntegerDuckId(runner);

        String responseMessage = "{\n"
                + "  \"message\": \"Duck with id = " + idDuck + " is updated\"\n" + "}";

        testDuck.color = "black";
        testDuck.sound = "woof";
        duckUpdate(runner,
                idDuck,
                testDuck.color,
                testDuck.height,
                testDuck.material,
                testDuck.sound,
                testDuck.wingsState);
        validateResponse(runner, responseMessage);
    }


}
