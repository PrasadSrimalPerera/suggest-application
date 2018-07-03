package com.task.suggest.daos;

import com.google.common.collect.Lists;
import com.task.suggest.index.SuggestIndex;
import com.task.suggest.models.SuggestDocument;
import junit.framework.TestCase;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by prasad on 7/2/18.
 *
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations="classpath:test.properties")
public class SuggestDocumentDAOTest {
    @Autowired
    private SuggestDocumentDAO suggestDocumentDAO;

    @BeforeClass
    public static void setUp() throws IOException {
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
        SuggestIndex.persistIndex(new SuggestIndex(), "/tmp/index/index.ser");
    }

    @AfterClass
    public static void shutdown() throws IOException {
        Files.delete(Paths.get("/tmp/data/cities_canada-usa.tsv"));
        Files.delete(Paths.get("/tmp/data"));
        Files.delete(Paths.get("/tmp/index/index.ser"));
        Files.delete(Paths.get("/tmp/index"));
    }

    @Test
    public void retrieveSuggestDocuments() throws Exception {
        List<SuggestDocument> suggestDocumentList = suggestDocumentDAO.retrieveSuggestDocuments();

        Assert.assertEquals(1, suggestDocumentList.size());
        Assert.assertEquals("5882142", suggestDocumentList.get(0).getId());
        Assert.assertEquals("Acton Vale", suggestDocumentList.get(0).getName());
        Assert.assertEquals("CA", suggestDocumentList.get(0).getCountry());
    }

}