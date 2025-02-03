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
import static io.qameta.allure.SeverityLevel.CRITICAL;

//Тест не валидирует результат c изначально непустой БД и/или в многопоточном режиме.
@Epic("Тесты на duck-CRUD-controller")
@Feature("Эндпоинт /api/duck/getAllIds")
public class DuckGetAllIdsTest extends CrudClient {
    @Test(description = "Проверка получения ID всех уточек (не работает с непустой БД")
    @CitrusTest
    @Severity(CRITICAL)
    public void successfulGetAllIds(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "yellow";
        double height = 2.21;
        String material = "wood";
        String sound = "quack";
        String wings_state = "ACTIVE";

        runner.variable("duckId", "citrus:randomNumber(10, true)"); //задаём случайный ID

        runner.$(doFinally().actions(context ->
                deleteDuckDB(runner, "${duckId}"))); // удаление утки из БД по завершениии

        createDuckDB(runner, "${duckId}", color, height, material, sound, wings_state);
        getDuckAllIds(runner);
        validateResponseString(runner, "[${duckId}]");
    }


}
