package autotests.tests.actions;

import autotests.clients.ActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckQuackTest extends ActionsClient {

    @Test(description = "Проверка того, что уточка с чётным ID крякает")
    @CitrusTest
    public void successfulQuackEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        int repetitionCount = 3; // количество повторов серий
        int soundCount = 5; // количество кряков в серии
        String sound = "quack";

        String quackSound = getQuackSound(repetitionCount, soundCount, sound);
        String responseMessage = "{\n" + "  \"sound\": \"" + quackSound + "\"\n" + "}";

        createDuckEvenId(runner, "even", 2.21, "rubber", "quack", "ACTIVE");
        duckQuack(runner,
                "${duckId}",
                repetitionCount,
                soundCount);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка того, что уточка с нечётным ID крякает")
    @CitrusTest
    public void successfulQuackOddId(@Optional @CitrusResource TestCaseRunner runner) {
        int repetitionCount = 2; // количество повторов серий
        int soundCount = 3; // количество кваков в серии
        String sound = "quack";

        String quackSound = getQuackSound(repetitionCount, soundCount, sound);
        String responseMessage = "{\n" + "  \"sound\": \"" + quackSound + "\"\n" + "}";

        createDuckOddId(runner, "odd", 2.21, "rubber", "quack", "ACTIVE");
        duckQuack(runner,
                "${duckId}",
                repetitionCount,
                soundCount);
        validateResponse(runner, responseMessage);
    }


}
