package autotests.tests.actions;

import autotests.clients.ActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckPropertiesTest extends ActionsClient {

    @Test(description = "Проверка получения характеристик утки с material = wood")
    @CitrusTest
    public void successfulPropertiesWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "even";
        double height = 2.21;
        String material = "wood";
        String sound = "quack";
        String wingsState = "FIXED";

        String responseMessage = "{\n" + "  \"color\": \"" + color + "\",\n"
                + "  \"height\": " + height + ",\n"
                + "  \"material\": \"" + material + "\",\n"
                + "  \"sound\": \"" + sound + "\",\n"
                + "  \"wingsState\": \"" + wingsState
                + "\"\n" + "}";

        createDuckEvenId(runner, true, color, height, material, sound, wingsState);
        duckProperties(runner);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка получения характеристик утки с material = rubber")
    @CitrusTest
    public void successfulPropertiesRubberDuck(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "odd";
        double height = 2.21;
        String material = "rubber";
        String sound = "quack";
        String wingsState = "FIXED";

        String responseMessage = "{\n" + "  \"color\": \"" + color + "\",\n"
                + "  \"height\": " + height + ",\n"
                + "  \"material\": \"" + material + "\",\n"
                + "  \"sound\": \"" + sound + "\",\n"
                + "  \"wingsState\": \"" + wingsState
                + "\"\n" + "}";

        createDuckEvenId(runner, true, color, height, material, sound, wingsState);
        duckProperties(runner);
        validateResponse(runner, responseMessage);
    }


}
