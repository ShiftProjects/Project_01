package autotests.tests.crud;

import autotests.clients.CrudClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

//Тест не валидирует результат после использования запроса DELETE.
//После DELETE необходимо перезапастить сервис.
public class DuckGetAllIds extends CrudClient {
    @Test(description = "Проверка получения ID всех уточек (не работает после DELETE")
    @CitrusTest
    public void successfulDuckDelete(@Optional @CitrusResource TestCaseRunner runner) {
        //String responseMessage = "[1,2,3,4,5]";

        createDuck(runner, "yellow", 2.21, "wood", "quack", "ACTIVE");
        getDuckId(runner);
        String responseMessage = arrayString(runner);
        getAllIds(runner);
        validateResponse(runner, responseMessage);
    }
}
