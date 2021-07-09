package unit_test;

import net.wavyway.ApplicationLauncher;
import net.wavyway._04_service.DataService;
import net.wavyway._05_model.RowObject;
import net.wavyway._05_model.WebDao;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@SpringBootTest(
    classes = ApplicationLauncher.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UnitTest {

    @Container
    public PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
        .withInitScript("db/data.sql")
        .withDatabaseName("testcontainers");

    private WebDao webDao;

    public List<RowObject> testList () {
        return null;
    }

    @Test
    @Order(1)
    public void initContainer() {
        System.out.println("NAME: " + postgreSQLContainer.getDatabaseName());
        System.out.println("URL: " + postgreSQLContainer.getJdbcUrl());
        System.out.println("TEST QUERY STRING: " + postgreSQLContainer.getTestQueryString());
        postgreSQLContainer.start();
        Assertions.assertTrue(postgreSQLContainer.isCreated());
        Assertions.assertTrue(postgreSQLContainer.isRunning());
    }

    private DriverManagerDataSource getDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(postgreSQLContainer.getJdbcUrl());
        dataSource.setUsername(postgreSQLContainer.getUsername());
        dataSource.setPassword(postgreSQLContainer.getPassword());
        dataSource.setDriverClassName(postgreSQLContainer.getDriverClassName());
        return dataSource;
    }

    private DataService getDataService(DataSource dataSource) {
        webDao = new WebDao(dataSource);
        return new DataService(webDao);
    }

    @Test
    @Order(2)
    public void testInteractionWithDatabase() throws IOException, InterruptedException, SQLException {

        DataService dataService = getDataService(getDataSource());

        RowObjectBuilder.create().persist(dataService);
        RowObjectBuilder.create().persist(dataService);
        RowObjectBuilder.create().persist(dataService);

        List<RowObject> rowObjectList = dataService.selectAll();
        System.out.println("NUMBER: " + rowObjectList.size());

        Assertions.assertTrue(rowObjectList.size() == 3);
    }

    @Test
    @Order(3)
    public void testTruncateTable() throws IOException, InterruptedException, SQLException {

        DataService dataService = getDataService(getDataSource());

        RowObjectBuilder.create().persist(dataService);
        RowObjectBuilder.create().persist(dataService);
        RowObjectBuilder.create().persist(dataService);

        List<RowObject> rowObjectPreList = dataService.selectAll();
        System.out.println("TOTAL PRE TRUNCATE: " + rowObjectPreList.size());

        Assertions.assertTrue(rowObjectPreList.size() == 3);

        dataService.deleteData();
        List<RowObject> rowObjectPostList = dataService.selectAll();
        System.out.println("TOTAL POST TRUNCATE: " + rowObjectPostList.size());

        Assertions.assertTrue(rowObjectPostList.size() == 0);
    }
}
