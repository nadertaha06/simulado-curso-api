package nader.avaliacao_api;

import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

class AvaliacoesApiApplicationMainTest {

    @Test
    void main_chamaSpringApplicationRun() {
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            String[] args = {};

            AvaliacoesApiApplication.main(args);

            mocked.verify(() -> SpringApplication.run(AvaliacoesApiApplication.class, args));
        }
    }
}
