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
    public void createDuckEvenId(TestCaseRunner runner,
                                 String color,
                                 double height,
                                 String material,
                                 String sound,
                                 String wingsState) {
        long id;
        do {
            createDuck(runner, color, height, material, sound, wingsState);
            getDuckId(runner);
            id = getIntegerDuckId(runner, "${duckId}");
        }
        while (id % 2 != 0);
    }

    //Создание утки с нечётным ID
    public void createDuckOddId(TestCaseRunner runner,
                                String color,
                                double height,
                                String material,
                                String sound,
                                String wingsState) {
        long id;
        do {
            createDuck(runner, color, height, material, sound, wingsState);
            getDuckId(runner);
            id = getIntegerDuckId(runner, "${duckId}");
        }
        while (id % 2 == 0);
    }

    //Получение тестовой переменной ID уточки
    public void getDuckId(TestCaseRunner runner) {
        runner.$(http().client(duckService)
                .receive()
                .response(HttpStatus.OK)
                .message()
                .extract(fromBody().expression("$.id", "duckId")));
    }

    //Преобразование контекстной переменной "idContext" в тип long
    public long getIntegerDuckId(TestCaseRunner runner, String idContext) {
        AtomicInteger id = new AtomicInteger();
        runner.$(action -> {
            id.set(Integer.parseInt(action.getVariable(idContext)));
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
