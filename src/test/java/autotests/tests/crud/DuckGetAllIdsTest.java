package autotests.tests.crud;

import autotests.clients.CrudClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.actions.EchoAction.Builder.echo;

//Тест не валидирует результат после использования запроса DELETE.
//После DELETE необходимо перезапастить сервис.
public class DuckGetAllIdsTest extends CrudClient {
    @Test(description = "Проверка получения ID всех уточек (не работает после DELETE")
    @CitrusTest
    public void successfulGetAllIds(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("yellow")
                .height(2.21)
                .sound("quack")
                .material("wood")
                .wingsState(WingState.ACTIVE);
        //String responseMessage = "[1,2,3,4,5]";

        createDuck(runner, testDuck);
        createTestVariableDuckId(runner);
        String responseMessage = arrayString(runner);
        getDuckAllIds(runner);
        validateResponseString(runner, responseMessage);
    }


}
