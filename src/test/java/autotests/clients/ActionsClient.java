package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.atomic.AtomicInteger;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class ActionsClient extends TestNGCitrusSpringSupport {
    @Autowired
    protected HttpClient duckService;


    //Создание утки
    public void createDuck(TestCaseRunner runner,
                           String color,
                           double height,
                           String material,
                           String sound,
                           String wingsState) {
        runner.$(http().client(duckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" + "  \"color\": \"" + color + "\",\n"
                        + "  \"height\": " + height + ",\n"
                        + "  \"material\": \"" + material + "\",\n"
                        + "  \"sound\": \"" + sound + "\",\n"
                        + "  \"wingsState\": \"" + wingsState
                        + "\"\n" + "}"));
    }

    //Создание утки с чётным ID
    public long createDuckEvenId(TestCaseRunner runner,
                                 String color,
                                 double height,
                                 String material,
                                 String sound,
                                 String wingsState) {
        long id;
        do {
            createDuck(runner, color, height, material, sound, wingsState);
            id = getIntegerDuckId(runner);
        }
        while (id % 2 != 0);
        return id;
    }

    //Создание утки с нечётным ID
    public long createDuckOddId(TestCaseRunner runner,
                                String color,
                                double height,
                                String material,
                                String sound,
                                String wingsState) {
        long id;
        do {
            createDuck(runner, color, height, material, sound, wingsState);
            id = getIntegerDuckId(runner);
        }
        while (id % 2 == 0);
        return id;
    }

    //Получение тестовой переменной ID уточки
    public String getDuckId(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
        return "${duckId}";
    }

    //Получение id уточки типа long
    public long getIntegerDuckId(TestCaseRunner runner) {
        AtomicInteger id = new AtomicInteger();
        getDuckId(runner);
        runner.$(action -> {
            id.set(Integer.parseInt(action.getVariable("duckId")));
        });
        return id.longValue();
    }

    //Валидация ответа
    public void validateResponse(TestCaseRunner runner, String responseMessage) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(responseMessage));
    }


    //Формирование звука
    public String getQuackSound(int repetitionCount,
                                int soundCount,
                                String sound) {
        StringBuilder quackRepetition = new StringBuilder(sound);
        for (int i = 1; i < soundCount; i++) {
            quackRepetition.append("-" + sound);
        }
        StringBuilder quackSound = new StringBuilder(quackRepetition);
        for (int i = 1; i < repetitionCount; i++) {
            quackSound.append(", ").append(quackRepetition);
        }
        return quackSound.toString();
    }


    //Показать характеристики
    public void duckProperties(TestCaseRunner runner, long id) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", String.valueOf(id)));
    }

    //крякай, уточка!
    public void duckQuack(TestCaseRunner runner,
                          long id,
                          int repetitionCount,
                          int soundCount) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", String.valueOf(id))
                .queryParam("repetitionCount", String.valueOf(repetitionCount))
                .queryParam("soundCount", String.valueOf(soundCount)))
        ;
    }

    //плыви, уточка!
    public void duckSwim(TestCaseRunner runner, long id) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", String.valueOf(id)));
    }

    //лети, уточка!
    public void duckFly(TestCaseRunner runner, long id) {
        runner.$(http().client(duckService)
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", String.valueOf(id)));
    }

}
