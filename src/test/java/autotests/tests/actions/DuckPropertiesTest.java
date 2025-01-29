package autotests.tests.actions;

import autotests.clients.ActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DuckPropertiesTest extends ActionsClient {

    @Test(description = "Проверка получения характеристик утки с material = wood")
    @CitrusTest
    public void successfulPropertiesWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("even")
                .height(2.21)
                .sound("quack")
                .material("wood")
                .wingsState(WingState.FIXED);

        String responseMessage = "{\n"
                + "  \"color\": \"" + testDuck.color() + "\",\n"
                + "  \"height\": " + testDuck.height() + ",\n"
                + "  \"material\": \"" + testDuck.material() + "\",\n"
                + "  \"sound\": \"" + testDuck.sound() + "\",\n"
                + "  \"wingsState\": \"" + testDuck.wingsState()
                + "\"\n" + "}";

        createDuckEvenId(runner, true, testDuck);
        duckProperties(runner);
        validateResponseString(runner, responseMessage);
    }

    @Test(description = "Проверка получения характеристик утки с material = rubber")
    @CitrusTest
    public void successfulPropertiesRubberDuck(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("odd")
                .height(2.21)
                .sound("quack")
                .material("rubber")
                .wingsState(WingState.FIXED);

        createDuckEvenId(runner, true, testDuck);
        duckProperties(runner);
        validateResponsePayload(runner, testDuck);
    }


}
