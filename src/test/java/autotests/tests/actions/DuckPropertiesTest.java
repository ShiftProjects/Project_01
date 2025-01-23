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

        String responseMessage = "{\n" + "  \"color\": \"" + "even" + "\",\n"
                + "  \"height\": " + 2.21 + ",\n"
                + "  \"material\": \"" + "wood" + "\",\n"
                + "  \"sound\": \"" + "quack" + "\",\n"
                + "  \"wingsState\": \"" + "FIXED"
                + "\"\n" + "}";

        long idDuck = createDuckEvenId(runner, "even", 2.21, "wood", "quack", "FIXED");

        duckProperties(runner, idDuck);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка получения характеристик утки с material = rubber")
    @CitrusTest
    public void successfulPropertiesRubberDuck(@Optional @CitrusResource TestCaseRunner runner) {

        String responseMessage = "{\n" + "  \"color\": \"" + "odd" + "\",\n"
                + "  \"height\": " + 2.21 + ",\n"
                + "  \"material\": \"" + "rubber" + "\",\n"
                + "  \"sound\": \"" + "quack" + "\",\n"
                + "  \"wingsState\": \"" + "FIXED"
                + "\"\n" + "}";

        long idDuck = createDuckOddId(runner, "odd", 2.21, "rubber", "quack", "FIXED");

        duckProperties(runner, idDuck);
        validateResponse(runner, responseMessage);
    }


}
