package autotests.tests.crud;

import autotests.clients.CrudClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckCreateTest extends CrudClient {


    @Test(description = "Проверка создания утки с material = rubber")
    @CitrusTest
    public void successfulCreationRubberDuck(@Optional @CitrusResource TestCaseRunner runner) {
        String responseMessage = "{\n"
                + "  \"id\": \"@ignore@\"" + ",\n"
                + "  \"color\": \"" + "yellow" + "\",\n"
                + "  \"height\": " + 2.21 + ",\n"
                + "  \"material\": \"" + "rubber" + "\",\n"
                + "  \"sound\": \"" + "quack" + "\",\n"
                + "  \"wingsState\": \"" + "ACTIVE"
                + "\"\n" + "}";

        createDuck(runner, "yellow", 2.21, "rubber", "quack", "ACTIVE");
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка создания утки с material = wood")
    @CitrusTest
    public void successfulCreationWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        String responseMessage = "{\n"
                + "  \"id\": \"@ignore@\"" + ",\n"
                + "  \"color\": \"" + "yellow" + "\",\n"
                + "  \"height\": " + 2.21 + ",\n"
                + "  \"material\": \"" + "wood" + "\",\n"
                + "  \"sound\": \"" + "quack" + "\",\n"
                + "  \"wingsState\": \"" + "ACTIVE"
                + "\"\n" + "}";

        createDuck(runner, "yellow", 2.21, "wood", "quack", "ACTIVE");
        validateResponse(runner, responseMessage);
    }

}
