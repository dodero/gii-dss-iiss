package es.uca.dss.ejercicios.knightsoftheroundtable;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = AppConfig.class)
class KnightsoftheroundtableApplicationTests {
	@Autowired
	private Knight knight;

	@Test
	void KnightConstructor() {
		assert(knight instanceof Knight);
		assertNotNull(knight.embarkOnQuest());
		assert(knight.embarkOnQuest() instanceof QuestItem);
	}

}
