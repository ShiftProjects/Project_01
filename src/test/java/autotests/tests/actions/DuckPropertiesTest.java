package autotests.tests.actions;

import autotests.clients.ActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckPropertiesTest extends ActionsClient {
    private static class TestDuck {
        String color = "black";
        double height = 1.5;
        String material = "rubber";
        String sound = "quack";
        String wingsState = "FIXED";
    }

    @Test(description = "Проверка получения характеристик утки с material = wood")
    @CitrusTest
    public void successfulPropertiesWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        TestDuck testDuck = new TestDuck();
        testDuck.material = "wood";
        testDuck.color = "even";

        String responseMessage = "{\n" + "  \"color\": \"" + testDuck.color + "\",\n"
                + "  \"height\": " + testDuck.height + ",\n"
                + "  \"material\": \"" + testDuck.material + "\",\n"
                + "  \"sound\": \"" + testDuck.sound + "\",\n"
                + "  \"wingsState\": \"" + testDuck.wingsState
                + "\"\n" + "}";

        long idDuck = createDuckEvenId(runner,
                testDuck.color,
                testDuck.height,
                testDuck.material,
                testDuck.sound,
                testDuck.wingsState);

        duckProperties(runner, idDuck);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка получения характеристик утки с material = rubber")
    @CitrusTest
    public void successfulPropertiesRubberDuck(@Optional @CitrusResource TestCaseRunner runner) {
        TestDuck testDuck = new TestDuck();
        testDuck.material = "rubber";
        testDuck.color = "odd";

        String responseMessage = "{\n" + "  \"color\": \"" + testDuck.color + "\",\n"
                + "  \"height\": " + testDuck.height + ",\n"
                + "  \"material\": \"" + testDuck.material + "\",\n"
                + "  \"sound\": \"" + testDuck.sound + "\",\n"
                + "  \"wingsState\": \"" + testDuck.wingsState
                + "\"\n" + "}";

        long idDuck = createDuckOddId(runner,
                testDuck.color,
                testDuck.height,
                testDuck.material,
                testDuck.sound,
                testDuck.wingsState);

        duckProperties(runner, idDuck);
        validateResponse(runner, responseMessage);
    }


}
