package autotests.tests.actions;

import autotests.clients.ActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Feature("Эндпоинт /api/duck/action/properties")
public class DuckPropertiesTest extends ActionsClient {

    @Test(description = "Проверка получения характеристик утки с material = wood")
    @CitrusTest
    public void successfulPropertiesWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "even";
        double height = 2.21;
        String material = "wood";
        String sound = "quack";
        String wings_state = "ACTIVE";

        runner.variable("duckId", generateEvenOddNumber(true)); //задаём случайный ID
        runner.$(doFinally().actions(context ->
                deleteDuckDB(runner, "${duckId}"))); // удаление утки из БД по завершениии

        String responseMessage = "{\n"
                + "  \"color\": \"" + color + "\",\n"
                + "  \"height\": " + height + ",\n"
                + "  \"material\": \"" + material + "\",\n"
                + "  \"sound\": \"" + sound + "\",\n"
                + "  \"wingsState\": \"" + wings_state + "\"\n" + "}";

        createDuckDB(runner, "${duckId}", color, height, material, sound, wings_state);

        duckProperties(runner);

        // проверка ответа сервера
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

        runner.variable("duckId", generateEvenOddNumber(false)); //задаём случайный ID
        runner.$(doFinally().actions(context ->
                deleteDuckDB(runner, "${duckId}"))); // удаление утки из БД по завершениии

        createDuckDB(runner, "${duckId}", testDuck.color(), testDuck.height(),
                testDuck.material(), testDuck.sound(), String.valueOf(testDuck.wingsState()));

        duckProperties(runner);

        // проверка ответа сервера
        validateResponsePayload(runner, testDuck);
    }


}
