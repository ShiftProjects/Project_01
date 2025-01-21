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
        int repetitionCount = 3; // количество повторов серий
        int soundCount = 5; // количество кваков в серии
        String sound = "quack";

        String quackSound = getQuackSound(repetitionCount, soundCount, sound);

        String responseMessage = "{\n" + "  \"sound\": \"" + quackSound + "\"\n" + "}";
        TestDuck testDuck = new TestDuck();
        long idDuck;

        //Гарантированное создание утки с чётным ID
        do {
            createDuck(runner,
                    testDuck.color,
                    testDuck.height,
                    testDuck.material,
                    testDuck.sound,
                    testDuck.wingsState);
            idDuck = getIntegerDuckId(runner);
        }
        while (idDuck % 2 != 0);

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
        long idDuck;
        int repetitionCount = 2; // количество повторов серий
        int soundCount = 3; // количество кваков в серии
        String sound = "quack";

        String quackSound = getQuackSound(repetitionCount, soundCount, sound);
        String responseMessage = "{\n" + "  \"sound\": \"" + quackSound + "\"\n" + "}";


        //Гарантированное создание утки с нечётным ID
        do {
            createDuck(runner,
                    testDuck.color,
                    testDuck.height,
                    testDuck.material,
                    testDuck.sound,
                    testDuck.wingsState);
            idDuck = getIntegerDuckId(runner);
        }
        while (idDuck % 2 == 0);

        duckQuack(runner,
                idDuck,
                repetitionCount,
                soundCount);
        validateResponse(runner, responseMessage);
    }


}
