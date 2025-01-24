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
        String color = "yellow";
        double height = 2.21;
        String material = "rubber";
        String sound = "quack";
        String wingsState = "ACTIVE";

        String responseMessage = "{\n"
                + "  \"id\": \"@ignore@\"" + ",\n"
                + "  \"color\": \"" + color + "\",\n"
                + "  \"height\": " + height + ",\n"
                + "  \"material\": \"" + material + "\",\n"
                + "  \"sound\": \"" + sound + "\",\n"
                + "  \"wingsState\": \"" + wingsState
                + "\"\n" + "}";

        createDuck(runner, color, height, material, sound, wingsState);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка создания утки с material = wood")
    @CitrusTest
    public void successfulCreationWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 2.21;
        String material = "wood";
        String sound = "quack";
        String wingsState = "ACTIVE";

        String responseMessage = "{\n"
                + "  \"id\": \"@ignore@\"" + ",\n"
                + "  \"color\": \"" + color + "\",\n"
                + "  \"height\": " + height + ",\n"
                + "  \"material\": \"" + material + "\",\n"
                + "  \"sound\": \"" + sound + "\",\n"
                + "  \"wingsState\": \"" + wingsState
                + "\"\n" + "}";

        createDuck(runner, color, height, material, sound, wingsState);
        validateResponse(runner, responseMessage);
    }

}
