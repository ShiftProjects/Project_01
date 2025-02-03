package autotests.tests.actions;

import autotests.clients.ActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;


@Epic("Тесты на duck-action-controller")
@Feature("Эндпоинт /api/duck/action/swim")
public class DuckSwimTest extends ActionsClient {


    @Test(description = "Проверка того, что уточка поплыла")
    @CitrusTest
    public void successfulSwim(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 2.21;
        String material = "wood";
        String sound = "quack";
        String wings_state = "ACTIVE";

        runner.variable("duckId", "citrus:randomNumber(10, true)"); //задаём случайный ID

        runner.$(doFinally().actions(context ->
                deleteDuckDB(runner, "${duckId}"))); // удаление утки из БД по завершениии

        createDuckDB(runner, "${duckId}", color, height, material, sound, wings_state);

        duckSwim(runner);
        validateResponseResources(runner, "actions/getDuckSwimTest/successfulSwim.json");
    }

    @Test(description = "Проверка ответа на запрос Плыть уточке с несуществующим ID")
    @CitrusTest
    public void duckWithoutIdSwimming(@Optional @CitrusResource TestCaseRunner runner) {
        runner.variable("duckId", "citrus:randomNumber(10, true)"); //задаём случайный ID
        duckSwim(runner);
        validateResponseString(runner, "{\n"
                + "  \"message\": \"Duck with id = " + "${duckId}" + " is not found\"\n" + "}");
    }


}
