package security_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.wavyway.ApplicationLauncher;
import net.wavyway._02_security.oauth2.JwtService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.TreeMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest(classes = ApplicationLauncher.class)
@ExtendWith(SpringExtension.class)
@Import(HttpEncodingAutoConfiguration.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    JwtService jwtService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
            .webAppContextSetup(this.wac)
            .addFilter((request, response, chain) -> {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                springSecurityFilterChain.doFilter(request, response, chain);
                }, "/*")
            .build();
    }

    @Test
    @Order(1)
    public void contextLoads() throws Exception {
        Assertions.assertNotNull(wac);
    }

    @Test
    @Order(2)
    public void authorizedJwtTest() throws Exception {

        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("username", "my_user");
        treeMap.put("password", "my_password");

        ObjectMapper mapper = new ObjectMapper();

        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(treeMap);

        MvcResult mvcResult = mockMvc.perform(post("http://localhost:8080/secpath")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .characterEncoding("UTF-8")
            .content(requestJson))
            .andReturn();

        String resultString = jwtService.getBearerTokenFromCookie(mvcResult.getResponse().getCookies());

        Assertions.assertFalse(resultString.contains("empty"));
        Assertions.assertTrue(resultString.contains("Bearer"));
    }

    @Test
    @Order(3)
    public void unauthorizedTest() throws Exception {

        TreeMap<String, String> treeMap = new TreeMap<>();
        treeMap.put("username", "fake_user");
        treeMap.put("password", "fake_pass");

        ObjectMapper mapper = new ObjectMapper();

        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(treeMap);

        ResultActions resultActions = mockMvc.perform(post("http://localhost:8080/secpath")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .characterEncoding("UTF-8")
            .content(requestJson));

        int status = resultActions.andReturn().getResponse().getStatus();
        Assertions.assertTrue(status == HttpStatus.UNAUTHORIZED.value());
    }
}
