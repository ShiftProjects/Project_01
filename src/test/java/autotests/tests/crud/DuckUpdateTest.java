package autotests.tests.crud;

import autotests.clients.CrudClient;
import autotests.payloads.Duck;
import autotests.payloads.Message;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static io.qameta.allure.SeverityLevel.NORMAL;


@Feature("Эндпоинт /api/duck/update")
public class DuckUpdateTest extends CrudClient {


    @Test(description = "Проверка изменения цвета и высоты уточки")
    @CitrusTest
    public void successfulUpdateOfColorAndHeight(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 2.21;
        String material = "wood";
        String sound = "quack";
        String wings_state = "ACTIVE";

        Duck testDuck = new Duck()
                .color("red")
                .height(15.81)
                .sound("quack")
                .material("wood")
                .wingsState(WingState.ACTIVE);

        runner.variable("duckId", "citrus:randomNumber(10, true)"); //задаём случайный ID

        runner.$(doFinally().actions(context ->
                deleteDuckDB(runner, "${duckId}"))); // удаление утки из БД по завершениии

        createDuckDB(runner, "${duckId}", color, height, material, sound, wings_state);

        Message responseMessage = new Message()
                .message("Duck with id = " + "${duckId}" + " is updated");

        duckUpdate(runner, testDuck);

        // проверка ответа сервера
        validateResponsePayload(runner, responseMessage);

        // проверка утки в БД
        validateDuckInDB(runner, "${duckId}", testDuck.color(),
                testDuck.height(), testDuck.material(),
                testDuck.sound(), String.valueOf(testDuck.wingsState()));

    }

    @Test(description = "Проверка изменения цвета и звука уточки")
    @CitrusTest
    @Severity(NORMAL)
    public void successfulUpdateOfColorAndSound(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 2.21;
        String material = "wood";
        String sound = "quack";
        String wings_state = "ACTIVE";

        Duck testDuck = new Duck()
                .color("black")
                .height(2.21)
                .sound("woof")
                .material("wood")
                .wingsState(WingState.ACTIVE);

        runner.variable("duckId", "citrus:randomNumber(10, true)"); //задаём случайный ID

        runner.$(doFinally().actions(context ->
                deleteDuckDB(runner, "${duckId}"))); // удаление утки из БД по завершениии

        createDuckDB(runner, "${duckId}", color, height, material, sound, wings_state);

        String responseMessage = "{\n"
                + "  \"message\": \"Duck with id = " + "${duckId}" + " is updated\"\n" + "}";

        duckUpdate(runner, testDuck);

        // проверка ответа сервера
        validateResponseString(runner, responseMessage);

        // проверка утки в БД
        validateDuckInDB(runner, "${duckId}", testDuck.color(),
                testDuck.height(), testDuck.material(),
                testDuck.sound(), String.valueOf(testDuck.wingsState()));

    }


}
