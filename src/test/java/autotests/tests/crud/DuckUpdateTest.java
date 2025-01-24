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
        String color = "yellow";
        double height = 2.21;
        String material = "wood";
        String sound = "quack";
        String wingsState = "ACTIVE";
        String color2 = "red";
        double height2 = 15.81;

        createDuck(runner, color, height, material, sound, wingsState);
        getDuckId(runner);

        String responseMessage = "{\n"
                + "  \"message\": \"Duck with id = " + "${duckId}" + " is updated\"\n" + "}";

        duckUpdate(runner, color2, height2, material, sound, wingsState);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка изменения цвета и звука уточки")
    @CitrusTest
    public void successfulUpdateOfColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 2.21;
        String material = "wood";
        String sound = "quack";
        String wingsState = "ACTIVE";
        String color2 = "black";
        String sound2 = "woof";

        createDuck(runner, color, height, material, sound, wingsState);
        getDuckId(runner);

        String responseMessage = "{\n"
                + "  \"message\": \"Duck with id = " + "${duckId}" + " is updated\"\n" + "}";

        duckUpdate(runner, color2, height, material, sound2, wingsState);
        validateResponse(runner, responseMessage);
    }


}
