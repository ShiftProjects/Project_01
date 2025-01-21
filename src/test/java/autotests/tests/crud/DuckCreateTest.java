package autotests.tests.crud;

import autotests.clients.CrudClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckCreateTest extends CrudClient {

    private static class TestDuck {
        String color = "yellow";
        double height = 0.3;
        String material = "rubber";
        String sound = "quack";
        String wingsState = "FIXED";
    }

    @Test(description = "Проверка создания утки с material = rubber")
    @CitrusTest
    public void successfulCreationRubberDuck(@Optional @CitrusResource TestCaseRunner runner) {
        TestDuck testDuck = new TestDuck();
        testDuck.material = "rubber";
        String responseMessage = "{\n"
                + "  \"id\": \"@ignore@\"" + ",\n"
                + "  \"color\": \"" + testDuck.color + "\",\n"
                + "  \"height\": " + testDuck.height + ",\n"
                + "  \"material\": \"" + testDuck.material + "\",\n"
                + "  \"sound\": \"" + testDuck.sound + "\",\n"
                + "  \"wingsState\": \"" + testDuck.wingsState
                + "\"\n" + "}";

        createDuck(runner,
                testDuck.color,
                testDuck.height,
                testDuck.material,
                testDuck.sound,
                testDuck.wingsState);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка создания утки с material = wood")
    @CitrusTest
    public void successfulCreationWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        TestDuck testDuck = new TestDuck();
        testDuck.material = "wood";
        String responseMessage = "{\n"
                + "  \"id\": \"@ignore@\"" + ",\n"
                + "  \"color\": \"" + testDuck.color + "\",\n"
                + "  \"height\": " + testDuck.height + ",\n"
                + "  \"material\": \"" + testDuck.material + "\",\n"
                + "  \"sound\": \"" + testDuck.sound + "\",\n"
                + "  \"wingsState\": \"" + testDuck.wingsState
                + "\"\n" + "}";

        createDuck(runner,
                testDuck.color,
                testDuck.height,
                testDuck.material,
                testDuck.sound,
                testDuck.wingsState);
        validateResponse(runner, responseMessage);
    }

}
