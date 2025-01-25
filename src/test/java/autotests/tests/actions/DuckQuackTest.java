package autotests.tests.actions;

import autotests.clients.ActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.Message;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckQuackTest extends ActionsClient {

    @Test(description = "Проверка того, что уточка с чётным ID крякает")
    @CitrusTest
    public void successfulQuackEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("even")
                .height(2.21)
                .sound("quack")
                .material("rubber")
                .wingsState(WingState.ACTIVE);
        int repetitionCount = 3; // количество повторов серий
        int soundCount = 5; // количество кряков в серии
        String sound = "quack";

        String quackSound = getQuackSound(repetitionCount, soundCount, sound);
        Message responseMessage = new Message()
                .sound(quackSound);
//        String responseMessage = "{\n" + "  \"sound\": \"" + quackSound + "\"\n" + "}";

        createDuckEvenId(runner, true, testDuck);
        duckQuack(runner,
                repetitionCount,
                soundCount);
        validateResponsePayload(runner, responseMessage);
    }

    @Test(description = "Проверка того, что уточка с нечётным ID крякает")
    @CitrusTest
    public void successfulQuackOddId(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("odd")
                .height(2.21)
                .sound("quack")
                .material("rubber")
                .wingsState(WingState.ACTIVE);
        int repetitionCount = 2; // количество повторов серий
        int soundCount = 3; // количество кваков в серии
        String sound = "quack";

        String quackSound = getQuackSound(repetitionCount, soundCount, sound);
        String responseMessage = "{\n" + "  \"sound\": \"" + quackSound + "\"\n" + "}";

        createDuckEvenId(runner, false, testDuck);
        duckQuack(runner,
                repetitionCount,
                soundCount);
        validateResponseString(runner, responseMessage);
    }


}
