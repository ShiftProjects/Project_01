package autotests.tests.actions;

import autotests.clients.ActionsClient;
import autotests.payloads.Message;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;

@Epic("Тесты на duck-action-controller")
@Feature("Эндпоинт /api/duck/action/fly")
public class DuckFlyTest extends ActionsClient {


    @Test(description = "Проверка того, что уточка полетела")
    @CitrusTest
    public void successfulFly(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 2.21;
        String material = "wood";
        String sound = "quack";
        String wings_state = "ACTIVE";

        runner.variable("duckId", "citrus:randomNumber(10, true)"); //задаём случайный ID

        runner.$(doFinally().actions(context ->
                deleteDuckDB(runner, "${duckId}"))); // удаление утки из БД по завершениии

        createDuckDB(runner, "${duckId}", color, height, material, sound, wings_state);

        duckFly(runner);

        // проверка ответа сервера
        validateResponseResources(runner, "actions/getDuckFlyTest/successfulFly.json");
    }

    @Test(description = "Проверка того, что уточка не может лететь со связанными крыльями")
    @CitrusTest
    public void successfulNotFly(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 2.21;
        String material = "wood";
        String sound = "quack";
        String wings_state = "FIXED";

        runner.variable("duckId", "citrus:randomNumber(10, true)"); //задаём случайный ID

        runner.$(doFinally().actions(context ->
                deleteDuckDB(runner, "${duckId}"))); // удаление утки из БД по завершениии

        createDuckDB(runner, "${duckId}", color, height, material, sound, wings_state);

        duckFly(runner);

        // проверка ответа сервера
        validateResponseResources(runner, "actions/getDuckFlyTest/unsuccessfulFly.json");
    }

    @Test(description = "Проверка ответа на запрос Лететь уточке с крыльями в неопределенном состоянии")
    @CitrusTest
    public void successfulUndefinedStateOfWings(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 2.21;
        String material = "wood";
        String sound = "quack";
        String wings_state = "UNDEFINED";

        runner.variable("duckId", "citrus:randomNumber(10, true)"); //задаём случайный ID
        Message responseMessage = new Message().message("Wings are not detected");

        runner.$(doFinally().actions(context ->
                deleteDuckDB(runner, "${duckId}"))); // удаление утки из БД по завершениии

        createDuckDB(runner, "${duckId}", color, height, material, sound, wings_state);

        duckFly(runner);

        // проверка ответа сервера
        validateResponsePayload(runner, responseMessage);
    }


}
