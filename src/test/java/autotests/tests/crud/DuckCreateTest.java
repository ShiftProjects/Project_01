package autotests.tests.crud;

import autotests.clients.CrudClient;
import autotests.payloads.Duck;
import autotests.payloads.WingState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.CitrusParameters;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static io.qameta.allure.SeverityLevel.BLOCKER;


@Feature("Эндпоинт /api/duck/create")
public class DuckCreateTest extends CrudClient {

    Duck testDuck1 = new Duck().color("red").height(0.12)
            .sound("quack").material("wood").wingsState(WingState.ACTIVE);
    Duck testDuck2 = new Duck().color("orange").height(3.45)
            .sound("woof").material("rubber").wingsState(WingState.FIXED);
    Duck testDuck3 = new Duck().color("yellow").height(67.8)
            .sound("moo").material("iron").wingsState(WingState.UNDEFINED);
    Duck testDuck4 = new Duck().color("green").height(901.)
            .sound("meow").material("plastic").wingsState(WingState.ACTIVE);
    Duck testDuck5 = new Duck().color("blue").height(2345.6)
            .sound("wee").material("ceramics").wingsState(WingState.FIXED);


    @DataProvider(name = "duckList")
    public Object[][] DuckProvider() {
        return new Object[][]{
                {testDuck1, null},
                {testDuck2, null},
                {testDuck3, null},
                {testDuck4, null},
                {testDuck5, null}
        };
    }

    //Параметризированный тест на создание утки
    @Test(description = "Параметризированный тест на создание утки",
            dataProvider = "duckList")
    @CitrusTest
    @CitrusParameters({"testDuck", "runner"})
    public void successfulDuckCreate(Duck testDuck,
                                     @Optional @CitrusResource TestCaseRunner runner) {
        String responseMessage = "{\n"
                + "  \"id\": \"@ignore@\"" + ",\n"
                + "  \"color\": \"" + testDuck.color() + "\",\n"
                + "  \"height\": " + testDuck.height() + ",\n"
                + "  \"material\": \"" + testDuck.material() + "\",\n"
                + "  \"sound\": \"" + testDuck.sound() + "\",\n"
                + "  \"wingsState\": \"" + testDuck.wingsState()
                + "\"\n" + "}";
        createDuckAPI(runner, testDuck);
        validateResponseString(runner, responseMessage);
    }

    @Test(description = "Проверка создания утки с material = rubber")
    @CitrusTest
    @Severity(BLOCKER)
    public void successfulCreationRubberDuck(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("yellow")
                .height(2.21)
                .sound("quack")
                .material("rubber")
                .wingsState(WingState.ACTIVE);
        String responseMessage = "{\n"
                + "  \"id\": \"@ignore@\"" + ",\n"
                + "  \"color\": \"" + testDuck.color() + "\",\n"
                + "  \"height\": " + testDuck.height() + ",\n"
                + "  \"material\": \"" + testDuck.material() + "\",\n"
                + "  \"sound\": \"" + testDuck.sound() + "\",\n"
                + "  \"wingsState\": \"" + testDuck.wingsState()
                + "\"\n" + "}";

        runner.$(doFinally().actions(context ->
                deleteDuckDB(runner, "${duckId}"))); // удаление утки из БД по завершениии
        createDuckAPI(runner, testDuck);

        // проверка ответа сервера
        validateResponseStringAndSaveId(runner, responseMessage);

        // проверка утки в БД
        validateDuckInDB(runner, "${duckId}", testDuck.color(),
                testDuck.height(), testDuck.material(),
                testDuck.sound(), String.valueOf(testDuck.wingsState()));
    }

    @Test(description = "Проверка создания утки с material = wood")
    @CitrusTest
    @Severity(BLOCKER)
    public void successfulCreationWoodDuck(@Optional @CitrusResource TestCaseRunner runner) {
        Duck testDuck = new Duck()
                .color("yellow")
                .height(2.14)
                .sound("quack")
                .material("wood")
                .wingsState(WingState.ACTIVE);
        String responseMessage = "{\n"
                + "  \"id\": \"@ignore@\"" + ",\n"
                + "  \"color\": \"" + testDuck.color() + "\",\n"
                + "  \"height\": " + testDuck.height() + ",\n"
                + "  \"material\": \"" + testDuck.material() + "\",\n"
                + "  \"sound\": \"" + testDuck.sound() + "\",\n"
                + "  \"wingsState\": \"" + testDuck.wingsState()
                + "\"\n" + "}";

        runner.$(doFinally().actions(context ->
                deleteDuckDB(runner, "${duckId}"))); // удаление утки из БД по завершениии
        createDuckAPI(runner, testDuck);

        // проверка ответа сервера
        validateResponseStringAndSaveId(runner, responseMessage);

        // проверка утки в БД
        validateDuckInDB(runner, "${duckId}", testDuck.color(),
                testDuck.height(), testDuck.material(),
                testDuck.sound(), String.valueOf(testDuck.wingsState()));
    }


}
