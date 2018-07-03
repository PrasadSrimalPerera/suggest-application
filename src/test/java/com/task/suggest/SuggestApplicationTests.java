package com.task.suggest;


import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:test.properties")
public class SuggestApplicationTests {
	@Autowired
	private MockMvc mvc;

	@BeforeClass
	public static void setUp() throws IOException {
		// Create necessary data, index dir and create tsv file with one entry
		if (!Files.exists(Paths.get("/tmp/data/")))
			Files.createDirectory(Paths.get("/tmp/data/"));
		Path file = Paths.get("/tmp/data/cities_canada-usa.tsv");
		List<String> lines = Lists.newArrayList();
		lines.add("id\tname\tascii\talt_name\tlat\tlong\tfeat_class\tfeat_code\tcountry\tcc2" +
				"\tadmin1\tadmin2\tadmin3\tadmin4\tpopulation\televation\tdem\ttz\tmodified_at");
		lines.add("5882142\tActon Vale\tActon Vale\t\t45.65007\t-72.56582\tP\tPPL\tCA\t\t10\t16\t" +
				"\t\t5135\t\t90\tAmerica/Montreal\t2008-04-11\n");
		Files.write(file, lines, Charset.forName("UTF-8"));
		if (!Files.exists(Paths.get("/tmp/index/")))
			Files.createDirectory(Paths.get("/tmp/index/"));
	}

	@AfterClass
	public static void shutdown() throws IOException {
		Files.delete(Paths.get("/tmp/data/cities_canada-usa.tsv"));
		Files.delete(Paths.get("/tmp/data"));
		Files.delete(Paths.get("/tmp/index/index.ser"));
		Files.delete(Paths.get("/tmp/index"));
	}

	@Test
	public void testAPIWithQuery() throws Exception {
		// The document indexed is 'Acton Vale'
		mvc.perform(get("/suggest?q=Act")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
		.andExpect(jsonPath("$.suggestions.[0].name", is("Acton Vale, CA")));

		// Query for non existent
		mvc.perform(get("/suggest?q=Mon")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.suggestions", hasSize(0)));
	}

	@Test
	public void testAPIWithQueryAndIncompleteGeoLocation() throws Exception {
		// The document indexed is 'Acton Vale'
		mvc.perform(get("/suggest?q=Act&latitude=12.33")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());

		// Query for non existent
		mvc.perform(get("/suggest?q=Act&longitude=12.33")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testAPIWithQueryAndGeoLocation() throws Exception {
		// The document indexed is 'Acton Vale'
		mvc.perform(get("/suggest?q=Act&latitude=12.33&longitude=-66.60")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.suggestions.[0].name", is("Acton Vale, CA")));

		mvc.perform(get("/suggest?q=Mon&latitude=12.33&longitude=-66.60")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.suggestions", hasSize(0)));
	}
}
