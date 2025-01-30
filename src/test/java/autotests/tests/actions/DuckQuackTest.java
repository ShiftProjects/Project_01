package autotests.tests.actions;

import autotests.clients.ActionsClient;
import autotests.payloads.Message;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;


@Feature("Эндпоинт /api/duck/action/quack")
public class DuckQuackTest extends ActionsClient {

    @Test(description = "Проверка того, что уточка с чётным ID крякает")
    @CitrusTest
    public void successfulQuackEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "even";
        double height = 2.21;
        String material = "wood";
        String sound = "quack";
        String wings_state = "ACTIVE";

        runner.variable("duckId", generateEvenOddNumber(true)); //задаём случайный ID
        int repetitionCount = 3; // количество повторов серий
        int soundCount = 5; // количество кряков в серии

        String quackSound = getQuackSound(repetitionCount, soundCount, sound);
        Message responseMessage = new Message()
                .sound(quackSound);

        runner.$(doFinally().actions(context ->
                deleteDuckDB(runner, "${duckId}"))); // удаление утки из БД по завершениии

        createDuckDB(runner, "${duckId}", color, height, material, sound, wings_state);

        duckQuack(runner,
                repetitionCount,
                soundCount);
        validateResponsePayload(runner, responseMessage);
    }

    @Test(description = "Проверка того, что уточка с нечётным ID крякает")
    @CitrusTest
    public void successfulQuackOddId(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "odd";
        double height = 2.21;
        String material = "wood";
        String sound = "quack";
        String wings_state = "ACTIVE";

        runner.variable("duckId", generateEvenOddNumber(false)); //задаём случайный ID
        int repetitionCount = 2; // количество повторов серий
        int soundCount = 3; // количество кваков в серии

        String quackSound = getQuackSound(repetitionCount, soundCount, sound);
        String responseMessage = "{\n" + "  \"sound\": \"" + quackSound + "\"\n" + "}";

        runner.$(doFinally().actions(context ->
                deleteDuckDB(runner, "${duckId}"))); // удаление утки из БД по завершениии

        createDuckDB(runner, "${duckId}", color, height, material, sound, wings_state);

        duckQuack(runner,
                repetitionCount,
                soundCount);
        validateResponseString(runner, responseMessage);
    }


}
