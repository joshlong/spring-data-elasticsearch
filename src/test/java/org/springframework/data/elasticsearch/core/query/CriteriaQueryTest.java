package org.springframework.data.elasticsearch.core.query;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.SampleEntity;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.RandomStringUtils.randomNumeric;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:elasticsearch-template-test.xml")
public class CriteriaQueryTest {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void shouldTestAndOperation(){
        //given
        //given
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setId(documentId);
        sampleEntity.setMessage("some test message");
        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setId(documentId);
        indexQuery.setObject(sampleEntity);
        elasticsearchTemplate.index(indexQuery);
        elasticsearchTemplate.refresh(SampleEntity.class, true);
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria("message").contains("test").and("message").contains("some"));
        //when
        SampleEntity sampleEntity1 = elasticsearchTemplate.queryForObject(criteriaQuery,SampleEntity.class);
        //then
        assertThat(sampleEntity1, is(notNullValue()));
    }

    @Test
    public void shouldTestOrOperation(){
        //given
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setMessage("some message");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        //second document
        String documentId2 = randomNumeric(5);
        SampleEntity sampleEntity2 = new SampleEntity();
        sampleEntity2.setId(documentId2);
        sampleEntity2.setMessage("test message");
        IndexQuery indexQuery2 = new IndexQuery();
        indexQuery2.setId(documentId2);
        indexQuery2.setObject(sampleEntity2);

        indexQueries.add(indexQuery2);
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria("message").contains("some").or("message").contains("test"));
        //when
        Page<SampleEntity> page = elasticsearchTemplate.queryForPage(criteriaQuery, SampleEntity.class);
        //then
        assertThat(page, is(notNullValue()));
        assertThat(page.getTotalElements(),is(greaterThanOrEqualTo(1L)));
    }

    @Test
    public void shouldTestInnerAndCriteria(){
        //given
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setMessage("some message");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria().and(new Criteria("message").contains("some")));
        //when
        Page<SampleEntity> page = elasticsearchTemplate.queryForPage(criteriaQuery, SampleEntity.class);
        //then
        assertThat(page, is(notNullValue()));
        assertThat(page.getTotalElements(),is(greaterThanOrEqualTo(1L)));
    }


    @Test
    public void shouldTestInnerOrCriteria(){
        //given
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setMessage("some message");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria().or(new Criteria("message").contains("some")));
        //when
        Page<SampleEntity> page = elasticsearchTemplate.queryForPage(criteriaQuery, SampleEntity.class);
        //then
        assertThat(page, is(notNullValue()));
        assertThat(page.getTotalElements(),is(greaterThanOrEqualTo(1L)));
    }

    @Test
    public void testIs() {
        //given
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setMessage("some message");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria("message").is("some message"));
        //when
        Page<SampleEntity> page= elasticsearchTemplate.queryForPage(criteriaQuery, SampleEntity.class);
        //then
        assertThat("message", is(criteriaQuery.getCriteria().getField().getName()));
        assertThat(page.getTotalElements(),is(greaterThanOrEqualTo(1L)));
    }

    @Test
    public void testMultipleIs() {
        //given
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setMessage("some message");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        //second document
        String documentId2 = randomNumeric(5);
        SampleEntity sampleEntity2 = new SampleEntity();
        sampleEntity2.setId(documentId2);
        sampleEntity2.setMessage("test message");
        IndexQuery indexQuery2 = new IndexQuery();
        indexQuery2.setId(documentId2);
        indexQuery2.setObject(sampleEntity2);

        indexQueries.add(indexQuery2);
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria("message").is("some message").is("test message"));
        //when
        Page<SampleEntity> page = elasticsearchTemplate.queryForPage(criteriaQuery,SampleEntity.class);
        //then
        assertThat("message", is(criteriaQuery.getCriteria().getField().getName()));
        assertThat(page.getTotalElements(),is(greaterThanOrEqualTo(2L)));
    }

    @Test
    public void testEndsWith() {
        //given
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setMessage("some message");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        //second document
        String documentId2 = randomNumeric(5);
        SampleEntity sampleEntity2 = new SampleEntity();
        sampleEntity2.setId(documentId2);
        sampleEntity2.setMessage("test message end");
        IndexQuery indexQuery2 = new IndexQuery();
        indexQuery2.setId(documentId2);
        indexQuery2.setObject(sampleEntity2);

        indexQueries.add(indexQuery2);
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        Criteria criteria = new Criteria("message").endsWith("end");
        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        //when
        SampleEntity sampleEntity = elasticsearchTemplate.queryForObject(criteriaQuery, SampleEntity.class);
        //then
        assertThat("message", is(criteriaQuery.getCriteria().getField().getName()));
        assertThat(sampleEntity, is(notNullValue()));
    }

    @Test
    public void testStartsWith() {
        //given
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setMessage("start some message");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        //second document
        String documentId2 = randomNumeric(5);
        SampleEntity sampleEntity2 = new SampleEntity();
        sampleEntity2.setId(documentId2);
        sampleEntity2.setMessage("test message");
        IndexQuery indexQuery2 = new IndexQuery();
        indexQuery2.setId(documentId2);
        indexQuery2.setObject(sampleEntity2);

        indexQueries.add(indexQuery2);
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        Criteria criteria = new Criteria("message").startsWith("start");
        CriteriaQuery criteriaQuery = new CriteriaQuery(criteria);
        //when
        SampleEntity sampleEntity = elasticsearchTemplate.queryForObject(criteriaQuery,SampleEntity.class);
        //then
        assertThat("message", is(criteriaQuery.getCriteria().getField().getName()));
        assertThat(sampleEntity,is(notNullValue()));
    }

    @Test
    public void testContains() {
        //given
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setMessage("contains some message");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        //second document
        String documentId2 = randomNumeric(5);
        SampleEntity sampleEntity2 = new SampleEntity();
        sampleEntity2.setId(documentId2);
        sampleEntity2.setMessage("test message");
        IndexQuery indexQuery2 = new IndexQuery();
        indexQuery2.setId(documentId2);
        indexQuery2.setObject(sampleEntity2);

        indexQueries.add(indexQuery2);
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        CriteriaQuery criteriaQuery= new CriteriaQuery(new Criteria("message").contains("contains"));
        //when
        SampleEntity sampleEntity = elasticsearchTemplate.queryForObject(criteriaQuery,SampleEntity.class);
        //then
        assertThat("message", is(criteriaQuery.getCriteria().getField().getName()));
        assertThat(sampleEntity,is(notNullValue()));
    }

    @Test
    public void testExpression() {
        //given
        //todo
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setMessage("elasticsearch search");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        //second document
        String documentId2 = randomNumeric(5);
        SampleEntity sampleEntity2 = new SampleEntity();
        sampleEntity2.setId(documentId2);
        sampleEntity2.setMessage("test message");
        IndexQuery indexQuery2 = new IndexQuery();
        indexQuery2.setId(documentId2);
        indexQuery2.setObject(sampleEntity2);

        indexQueries.add(indexQuery2);
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        CriteriaQuery criteriaQuery= new CriteriaQuery(new Criteria("message").expression("+elasticsearch || test"));
        //when
        SampleEntity sampleEntity = elasticsearchTemplate.queryForObject(criteriaQuery,SampleEntity.class);
        //then
        assertThat("message", is(criteriaQuery.getCriteria().getField().getName()));
        assertThat(sampleEntity,is(notNullValue()));
    }

    @Test
    public void testCriteriaChain() {
        //given
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setMessage("some message search");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        //second document
        String documentId2 = randomNumeric(5);
        SampleEntity sampleEntity2 = new SampleEntity();
        sampleEntity2.setId(documentId2);
        sampleEntity2.setMessage("test test message");
        IndexQuery indexQuery2 = new IndexQuery();
        indexQuery2.setId(documentId2);
        indexQuery2.setObject(sampleEntity2);

        indexQueries.add(indexQuery2);
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria("message").startsWith("some").endsWith("search").contains("message").is("some message search"));
        //when
        SampleEntity sampleEntity = elasticsearchTemplate.queryForObject(criteriaQuery,SampleEntity.class);
        //then
        assertThat("message", is(criteriaQuery.getCriteria().getField().getName()));
        assertThat(sampleEntity, is(notNullValue()));
    }

    @Test
    public void testIsNot() {
        //given
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setMessage("bar");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        //second document
        String documentId2 = randomNumeric(5);
        SampleEntity sampleEntity2 = new SampleEntity();
        sampleEntity2.setId(documentId2);
        sampleEntity2.setMessage("foo");
        IndexQuery indexQuery2 = new IndexQuery();
        indexQuery2.setId(documentId2);
        indexQuery2.setObject(sampleEntity2);

        indexQueries.add(indexQuery2);
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria("message").is("foo").not());
        //when
        Page<SampleEntity> page = elasticsearchTemplate.queryForPage(criteriaQuery, SampleEntity.class);
        //then
        assertTrue(criteriaQuery.getCriteria().isNegating());
        assertThat(page,is(notNullValue()));
        assertFalse(page.iterator().next().getMessage().contains("foo"));
    }

    @Test
    public void testBetween() {
        //given
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setRate(100);
        sampleEntity1.setMessage("bar");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        //second document
        String documentId2 = randomNumeric(5);
        SampleEntity sampleEntity2 = new SampleEntity();
        sampleEntity2.setId(documentId2);
        sampleEntity2.setRate(200);
        sampleEntity2.setMessage("foo");
        IndexQuery indexQuery2 = new IndexQuery();
        indexQuery2.setId(documentId2);
        indexQuery2.setObject(sampleEntity2);

        indexQueries.add(indexQuery2);
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria("rate").between(100,150));
        //when
        SampleEntity sampleEntity = elasticsearchTemplate.queryForObject(criteriaQuery, SampleEntity.class);
        //then
        assertThat(sampleEntity,is(notNullValue()));
    }

    @Test
    public void testBetweenWithoutUpperBound() {
        //given
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setRate(300);
        sampleEntity1.setMessage("bar");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        //second document
        String documentId2 = randomNumeric(5);
        SampleEntity sampleEntity2 = new SampleEntity();
        sampleEntity2.setId(documentId2);
        sampleEntity2.setRate(400);
        sampleEntity2.setMessage("foo");
        IndexQuery indexQuery2 = new IndexQuery();
        indexQuery2.setId(documentId2);
        indexQuery2.setObject(sampleEntity2);

        indexQueries.add(indexQuery2);
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria("rate").between(350,null));
        //when
        Page<SampleEntity> page = elasticsearchTemplate.queryForPage(criteriaQuery, SampleEntity.class);
        //then
        assertThat(page,is(notNullValue()));
        assertThat(page.getTotalElements(),is(greaterThanOrEqualTo(1L)));
    }

    @Test
    public void testBetweenWithoutLowerBound() {
        //given
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setRate(500);
        sampleEntity1.setMessage("bar");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        //second document
        String documentId2 = randomNumeric(5);
        SampleEntity sampleEntity2 = new SampleEntity();
        sampleEntity2.setId(documentId2);
        sampleEntity2.setRate(600);
        sampleEntity2.setMessage("foo");
        IndexQuery indexQuery2 = new IndexQuery();
        indexQuery2.setId(documentId2);
        indexQuery2.setObject(sampleEntity2);

        indexQueries.add(indexQuery2);
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria("rate").between(null,550));
        //when
        Page<SampleEntity> page = elasticsearchTemplate.queryForPage(criteriaQuery, SampleEntity.class);
        //then
        assertThat(page,is(notNullValue()));
        assertThat(page.getTotalElements(),is(greaterThanOrEqualTo(1L)));
    }

    @Test
    public void testLessThanEqauls() {
        //given
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setRate(700);
        sampleEntity1.setMessage("bar");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        //second document
        String documentId2 = randomNumeric(5);
        SampleEntity sampleEntity2 = new SampleEntity();
        sampleEntity2.setId(documentId2);
        sampleEntity2.setRate(800);
        sampleEntity2.setMessage("foo");
        IndexQuery indexQuery2 = new IndexQuery();
        indexQuery2.setId(documentId2);
        indexQuery2.setObject(sampleEntity2);

        indexQueries.add(indexQuery2);
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria("rate").lessThanEqual(750));
        //when
        Page<SampleEntity> page = elasticsearchTemplate.queryForPage(criteriaQuery, SampleEntity.class);
        //then
        assertThat(page,is(notNullValue()));
        assertThat(page.getTotalElements(),is(greaterThanOrEqualTo(1L)));
    }

    @Test
    public void testGreaterThanEqauls() {
        //given
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setRate(900);
        sampleEntity1.setMessage("bar");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        //second document
        String documentId2 = randomNumeric(5);
        SampleEntity sampleEntity2 = new SampleEntity();
        sampleEntity2.setId(documentId2);
        sampleEntity2.setRate(1000);
        sampleEntity2.setMessage("foo");
        IndexQuery indexQuery2 = new IndexQuery();
        indexQuery2.setId(documentId2);
        indexQuery2.setObject(sampleEntity2);

        indexQueries.add(indexQuery2);
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria("rate").greaterThanEqual(950));
        //when
        Page<SampleEntity> page = elasticsearchTemplate.queryForPage(criteriaQuery, SampleEntity.class);
        //then
        assertThat(page,is(notNullValue()));
        assertThat(page.getTotalElements(),is(greaterThanOrEqualTo(1L)));
    }

    @Test
    public void testBoost() {
        //given
        List<IndexQuery> indexQueries = new ArrayList<IndexQuery>();
        //first document
        String documentId = randomNumeric(5);
        SampleEntity sampleEntity1 = new SampleEntity();
        sampleEntity1.setId(documentId);
        sampleEntity1.setRate(700);
        sampleEntity1.setMessage("bar foo");

        IndexQuery indexQuery1 = new IndexQuery();
        indexQuery1.setId(documentId);
        indexQuery1.setObject(sampleEntity1);
        indexQueries.add(indexQuery1);

        //second document
        String documentId2 = randomNumeric(5);
        SampleEntity sampleEntity2 = new SampleEntity();
        sampleEntity2.setId(documentId2);
        sampleEntity2.setRate(800);
        sampleEntity2.setMessage("foo");
        IndexQuery indexQuery2 = new IndexQuery();
        indexQuery2.setId(documentId2);
        indexQuery2.setObject(sampleEntity2);

        indexQueries.add(indexQuery2);
        elasticsearchTemplate.bulkIndex(indexQueries);
        elasticsearchTemplate.refresh(SampleEntity.class,true);
        CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria("message").contains("foo").boost(1));
        //when
        Page<SampleEntity> page = elasticsearchTemplate.queryForPage(criteriaQuery, SampleEntity.class);
        //then
        assertThat(page.getTotalElements(),is(greaterThanOrEqualTo(1L)));
    }
}
