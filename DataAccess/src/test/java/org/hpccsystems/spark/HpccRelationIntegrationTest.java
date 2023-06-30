package org.hpccsystems.spark;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.sources.EqualTo;
import org.apache.spark.sql.sources.Filter;
import org.apache.spark.sql.sources.GreaterThan;
import org.apache.spark.sql.sources.In;
import org.apache.spark.sql.sources.IsNull;
import org.apache.spark.sql.sources.LessThan;
import org.apache.spark.sql.sources.Not;
import org.apache.spark.sql.sources.Or;
import org.apache.spark.sql.sources.StringContains;
import org.apache.spark.sql.sources.StringEndsWith;
import org.apache.spark.sql.sources.StringStartsWith;
import org.hpccsystems.spark.datasource.HpccOptions;
import org.hpccsystems.spark.datasource.HpccRelation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import scala.collection.JavaConverters;
import scala.collection.Seq;

public class HpccRelationIntegrationTest extends BaseIntegrationTest
{
    // @Test
    public void testbuildScanAllValid() throws Exception
    {
        SparkSession spark = getOrCreateSparkSession();
        SQLContext sqlcontext = new SQLContext(spark);

        String testDataset = "spark::test::integer_kv";

        TreeMap<String, String> paramTreeMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        paramTreeMap.put("host", getHPCCClusterURL());
        paramTreeMap.put("path", testDataset);
        paramTreeMap.put("cluster", getThorCluster());
        paramTreeMap.put("username", getHPCCClusterUser());
        paramTreeMap.put("password", getHPCCClusterPass());

        HpccOptions hpccopts = new HpccOptions(paramTreeMap);
        HpccRelation hpccRelation = new HpccRelation(sqlcontext, hpccopts);

        Filter[] supportedSparkFilters = {
            new Or(new LessThan("key", 12), new GreaterThan("key", 8)),
            new In("key", new Object [] { 1, 2, 3, 4, 5}),
            new EqualTo("key", 5),
            new Not(new LessThan("key", 3)),
        };

        RDD<Row> rdd = hpccRelation.buildScan(new String[]{"key"}, supportedSparkFilters);
        Assert.assertTrue("Unexpected filter result count", rdd.count() == 1);
    }

    @Test
    public void testUnhandledFiltersAllValid() throws Exception
    {
        HpccRelation hpccRelation = new HpccRelation(null, null);

        Filter[] supportedSparkFilters = {
            new StringStartsWith("fixstr8", "Rod"),
            new Or(new LessThan("int8", 12), new GreaterThan("int8", 8)),
            new In("int8", new Object [] { "str", "values", "etc"}),
            new In("int8", new Object [] { 1, 2, 3, 4, 5.6}),
            new LessThan("fixstr8", "XYZ"),
            new Not(new EqualTo("fixstr8", "true")),
            new EqualTo("int8", 5),
            new Not(new LessThan("int8", 3))
        };

        Filter [] unhandledsparkfilters = hpccRelation.unhandledFilters(supportedSparkFilters);

        Assert.assertTrue("Unexpected unhandled filters detected" , unhandledsparkfilters.length == 0);
    }

    @Test
    public void testUnhandledFiltersNoneValid() throws Exception
    {
        HpccRelation hpccRelation = new HpccRelation(null, null);

        Filter[] unsupportedSparkFilters = {
            new IsNull("something"),
            new Or(new LessThan("int8", 12), new GreaterThan("int4", 8)),
            new Not(new Or(new LessThan("int8", 12), new GreaterThan("int8", 8))),
            new Not(new In("int8", new Object [] { 1, 2, 3, 4, 5.6})),
            new StringContains("somestring", "some"),
            new StringEndsWith("somestring", "ing")
        };

        Filter[] unhandledsparkfilters = hpccRelation.unhandledFilters(unsupportedSparkFilters);

        Assert.assertTrue("Unexpected unhandled filters detected" , unhandledsparkfilters.length == unsupportedSparkFilters.length);
    }
}
