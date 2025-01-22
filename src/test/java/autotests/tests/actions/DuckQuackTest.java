package autotests.tests.actions;

import autotests.clients.ActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class DuckQuackTest extends ActionsClient {

    private static class TestDuck {
        String color = "yellow";
        double height = 0.35;
        String material = "rubber";
        String sound = "quack";
        String wingsState = "FIXED";
    }

    @Test(description = "Проверка того, что уточка с чётным ID крякает")
    @CitrusTest
    public void successfulQuackEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        TestDuck testDuck = new TestDuck();
        int repetitionCount = 3; // количество повторов серий
        int soundCount = 5; // количество кваков в серии
        String sound = "quack";
        testDuck.color = "even";

        String quackSound = getQuackSound(repetitionCount, soundCount, sound);
        String responseMessage = "{\n" + "  \"sound\": \"" + quackSound + "\"\n" + "}";

        long idDuck = createDuckEvenId(runner,
                testDuck.color,
                testDuck.height,
                testDuck.material,
                testDuck.sound,
                testDuck.wingsState);

        duckQuack(runner,
                idDuck,
                repetitionCount,
                soundCount);
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка того, что уточка с нечётным ID крякает")
    @CitrusTest
    public void successfulQuackOddId(@Optional @CitrusResource TestCaseRunner runner) {
        TestDuck testDuck = new TestDuck();
        int repetitionCount = 2; // количество повторов серий
        int soundCount = 3; // количество кваков в серии
        String sound = "quack";
        testDuck.color = "odd";

        String quackSound = getQuackSound(repetitionCount, soundCount, sound);
        String responseMessage = "{\n" + "  \"sound\": \"" + quackSound + "\"\n" + "}";

        long idDuck = createDuckOddId(runner,
                testDuck.color,
                testDuck.height,
                testDuck.material,
                testDuck.sound,
                testDuck.wingsState);

        duckQuack(runner,
                idDuck,
                repetitionCount,
                soundCount);
        validateResponse(runner, responseMessage);
    }


}
