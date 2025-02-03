package autotests.tests.crud;

import autotests.clients.CrudClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static io.qameta.allure.SeverityLevel.BLOCKER;


@Epic("Тесты на duck-CRUD-controller")
@Feature("Эндпоинт /api/duck/delete")
public class DuckDeleteTest extends CrudClient {


    @Test(description = "Проверка удаления уточки")
    @CitrusTest
    @Severity(BLOCKER)
    public void successfulDuckDelete(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 2.21;
        String material = "wood";
        String sound = "quack";
        String wings_state = "ACTIVE";

        runner.variable("duckId", "citrus:randomNumber(10, true)"); //задаём случайный ID

        runner.$(doFinally().actions(context ->
                deleteDuckDB(runner, "${duckId}"))); // удаление утки из БД по завершениии

        createDuckDB(runner, "${duckId}", color, height, material, sound, wings_state);

        duckDeleteAPI(runner, "${duckId}");

        // проверка ответа сервера
        validateResponseResources(runner, "crud/getDuckDeleteTest/successfulDeleted.json");

        // проверка отсутствия утки в БД
        validateNotPresentDuckInDB(runner, "${duckId}");

    }


}
