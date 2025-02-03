package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.message.MessagePayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import javax.sql.DataSource;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class BaseTest extends TestNGCitrusSpringSupport {

    @Autowired
    protected HttpClient duckService;

    @Autowired
    protected SingleConnectionDataSource testDB;

    //Запрос методом sql() к БД
    protected void statementSQL(TestCaseRunner runner, DataSource dataSource, String sql) {
        runner.$(sql(dataSource)
                .statement(sql));
    }

    //Получение ответа на API запросс передачей ожидаемого ответа String’ой
    protected void responseReceiveString(TestCaseRunner runner, HttpClient httpClient,
                                         HttpStatus httpStatus, String contentType,
                                         String payload) {
        runner.$(http().client(httpClient)
                .receive()
                .response(httpStatus)
                .message()
                .contentType(contentType)
                .body(payload));
    }

    //Получение ответа на API запросс передачей ожидаемого ответа String’ой
    // с сохранением тестовой переменной
    protected void responseReceiveStringAndSaveId(TestCaseRunner runner, HttpClient httpClient,
                                                  HttpStatus httpStatus, String contentType,
                                                  String payload, String path, Object variableName) {
        runner.$(http().client(httpClient)
                .receive()
                .response(httpStatus)
                .message()
                .contentType(contentType)
                .body(payload)
                .extract(fromBody()
                        .expression(path, variableName)));
    }

    //Получение ответа на API запрос с передачей ожидаемого ответа из папки Payload
    protected void responseReceivePayload(TestCaseRunner runner, HttpClient httpClient,
                                          HttpStatus httpStatus, String contentType,
                                          MessagePayloadBuilder payload) {
        runner.$(http().client(httpClient)
                .receive()
                .response(httpStatus)
                .message()
                .contentType(contentType)
                .body(payload));
    }

    //Получение ответа на API запрос с передачей ожидаемого ответа из папки Resources
    protected void responseReceiveResources(TestCaseRunner runner, HttpClient httpClient,
                                            HttpStatus httpStatus, String contentType,
                                            Resource payload) {
        runner.$(http().client(httpClient)
                .receive()
                .response(httpStatus)
                .message()
                .contentType(contentType)
                .body(payload));
    }

    //API запрос GET
    protected void sendGetRequest(TestCaseRunner runner, HttpClient httpClient, String path) {
        runner.$(http().client(httpClient)
                .send()
                .get(path));
    }

    //API запрос POST с передачей тела из папки Payload
    protected void postSendAPIPayload(TestCaseRunner runner, HttpClient httpClient, String path,
                                      String contentType, MessagePayloadBuilder payload) {
        runner.$(http().client(httpClient)
                .send()
                .post(path)
                .message()
                .contentType(contentType)
                .body(payload));
    }

    //API запрос PUT
    protected void putSendAPI(TestCaseRunner runner, HttpClient httpClient, String path) {
        runner.$(http().client(httpClient)
                .send()
                .put(path));
    }

    //API запрос DELETE
    protected void deleteSendAPI(TestCaseRunner runner, HttpClient httpClient, String path) {
        runner.$(http().client(httpClient)
                .send()
                .delete(path));
    }


}
