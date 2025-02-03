package autotests.tests.crud;

import autotests.clients.CrudClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckCreateTest extends CrudClient {


    @Test(description = "Проверка создания утки с material = rubber")
    @CitrusTest
    public void successfulCreationRubberDuck(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("yellow")
                .height(2.21)
                .sound("quack")
                .material("rubber")
                .wingsState(WingState.ACTIVE);

        String responseMessage = "{\n"
                + "  \"id\": \"@ignore@\"" + ",\n"
                + "  \"color\": \"" + testDuck.color() + "\",\n"
                + "  \"height\": " + testDuck.height() + ",\n"
                + "  \"material\": \"" + testDuck.material() + "\",\n"
                + "  \"sound\": \"" + testDuck.sound() + "\",\n"
                + "  \"wingsState\": \"" + testDuck.wingsState()
                + "\"\n" + "}";

        createDuck(runner, testDuck);
        validateResponseString(runner, responseMessage);
    }

    @Test(description = "Проверка создания утки с material = wood")
    @CitrusTest
    public void successfulCreationWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("yellow")
                .height(2.21)
                .sound("quack")
                .material("wood")
                .wingsState(WingState.ACTIVE);

        String responseMessage = "{\n"
                + "  \"id\": \"@ignore@\"" + ",\n"
                + "  \"color\": \"" + testDuck.color() + "\",\n"
                + "  \"height\": " + testDuck.height() + ",\n"
                + "  \"material\": \"" + testDuck.material() + "\",\n"
                + "  \"sound\": \"" + testDuck.sound() + "\",\n"
                + "  \"wingsState\": \"" + testDuck.wingsState()
                + "\"\n" + "}";

        createDuck(runner, testDuck);
        validateResponseString(runner, responseMessage);
    }

}
