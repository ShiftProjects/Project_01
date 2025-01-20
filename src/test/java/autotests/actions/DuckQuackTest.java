package autotests.actions;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DuckQuackTest extends TestNGCitrusSpringSupport {

    private static class TestDuck {
        String color = "yellow";
        String height = "0.35";
        String material = "rubber";
        String sound = "quack";
        String wingsState = "FIXED";
    }

    @Test(description = "Проверка того, что уточка с чётным ID крякает")
    @CitrusTest
    public void successfulQuackEvenId(@Optional @CitrusResource TestCaseRunner runner) {
        int repetitionCount = 3; // повтор кваков в серии
        int soundCount = 5; // количество серий
        StringBuilder quackRepetition = new StringBuilder("quack");


        //Формирование звука
        for(int i = 1; i < repetitionCount; i++){
            quackRepetition.append("-quack");
        }
        StringBuilder quackSound = new StringBuilder(quackRepetition);
        for(int i = 1; i < soundCount; i++){
            quackSound.append(", ").append(quackRepetition);
        }

        String responseMessage = "{\n" + "  \"sound\": \"" + quackSound + "\"\n" + "}";
        TestDuck testDuck = new TestDuck();
        String idDuck;
        AtomicInteger id = new AtomicInteger();

        //Гарантированное создание утки с чётным ID
        do {
            idDuck = createDuck(runner, testDuck);
            runner.$(action -> {
                id.set(Integer.parseInt(action.getVariable("duckId")));
            });
        }
        while (id.get() % 2 != 0);

        duckQuack(runner,
                idDuck,
                Integer.toString(repetitionCount),
                Integer.toString(soundCount));
        validateResponse(runner, responseMessage);
    }

    @Test(description = "Проверка того, что уточка с нечётным ID крякает")
    @CitrusTest
    public void successfulQuackOddId(@Optional @CitrusResource TestCaseRunner runner) {
        int repetitionCount = 2; // повтор кваков в серии
        int soundCount = 3; // количество серий
        StringBuilder quackRepetition = new StringBuilder("quack");


        //Формирование звука
        for(int i = 1; i < repetitionCount; i++){
            quackRepetition.append("-quack");
        }
        StringBuilder quackSound = new StringBuilder(quackRepetition);
        for(int i = 1; i < soundCount; i++){
            quackSound.append(", ").append(quackRepetition);
        }

        String responseMessage = "{\n" + "  \"sound\": \"" + quackSound + "\"\n" + "}";
        TestDuck testDuck = new TestDuck();
        String idDuck;
        AtomicInteger id = new AtomicInteger();

        //Гарантированное создание утки с нечётным ID
        do {
            idDuck = createDuck(runner, testDuck);
            runner.$(action -> {
                id.set(Integer.parseInt(action.getVariable("duckId")));
            });
        }
        while (id.get() % 2 == 0);

        duckQuack(runner,
                idDuck,
                Integer.toString(repetitionCount),
                Integer.toString(soundCount));
        validateResponse(runner, responseMessage);
    }



    //крякай, уточка!
    public void duckQuack(TestCaseRunner runner,
                          String id,
                          String repetitionCount,
                          String soundCount) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount))
        ;
    }

    //валидация ответа
    public void validateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
    }

    //Создание утки
    private String createDuck(TestCaseRunner runner, TestDuck testDuck) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" + "  \"color\": \"" + testDuck.color + "\",\n"
                        + "  \"height\": " + testDuck.height + ",\n"
                        + "  \"material\": \"" + testDuck.material + "\",\n"
                        + "  \"sound\": \"" + testDuck.sound + "\",\n"
                        + "  \"wingsState\": \"" + testDuck.wingsState
                        + "\"\n" + "}"));
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
        return "${duckId}";
    }
}
