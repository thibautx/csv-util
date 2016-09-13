package test;

import main.Csv;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class TestCsvReader {

    String file = "data.csv";
    private Csv csv = new Csv(file);
    String header1 = "col1";
    String header2 = "col2";
    String header3 = "col3";

    long start_time;
    long run_time;
    int MAX_RUN_TIME = 5;

    @Before
    public void setUp(){
    }

    @After
    public void after(){
        csv = new Csv(file);
    }

    @Test
    public void testReadCsv(){
//        csv.readCsv("data.csv");
    }

    @Test
    public void testWriteCsv(){
//        csv.writeCsv();
    }

    @Test
    public void testAddColumn(){
        ArrayList<Double> new_column = new ArrayList<>(csv.getColumnNumerical(header1));
        csv.addNewColumn("test_new", new_column);
    }

    @Test
    public void testColumnMax(){
        start_time = System.currentTimeMillis();
        double max = csv.columnMax(header1);
        run_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start_time);
        assertEquals(100000.0, max, 0.01);
        assertTrue(run_time < MAX_RUN_TIME);

        start_time = System.currentTimeMillis();
        max = csv.columnMax(header2);
        run_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start_time);
        assertEquals(1000000.0, max, 0.01);
        assertTrue(run_time < MAX_RUN_TIME);

        start_time = System.currentTimeMillis();
        max = csv.columnMax(header3);
        run_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start_time);
        assertEquals(10000000.0, max, 0.01);
        assertTrue(run_time < MAX_RUN_TIME);
    }

    @Test
    public void testColumnMin(){
        double min;

        start_time = System.currentTimeMillis();
        min = csv.columnMin(header1);
        run_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start_time);
        assertEquals(1.0, min, 0.01);
        assertTrue(run_time < MAX_RUN_TIME);

        start_time = System.currentTimeMillis();
        min = csv.columnMin(header2);
        run_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start_time);
        assertEquals(10.0, min, 0.01);
        assertTrue(run_time < MAX_RUN_TIME);

        start_time = System.currentTimeMillis();
        min = csv.columnMin(header3);
        run_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start_time);
        assertEquals(100.0, min, 0.01);
        assertTrue(run_time < MAX_RUN_TIME);
    }

    @Test
    public void testColumnMean(){
        double mean;

        start_time = System.currentTimeMillis();
        mean = csv.columnMean(header1);
        run_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start_time);
        assertEquals(50000.5, mean, 0.01);
        assertTrue(run_time < MAX_RUN_TIME);

        start_time = System.currentTimeMillis();
        mean = csv.columnMean(header2);
        run_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start_time);
        assertEquals(500005.0, mean, 0.01);
        assertTrue(run_time < MAX_RUN_TIME);

        start_time = System.currentTimeMillis();
        mean = csv.columnMean(header3);
        run_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start_time);
        assertEquals(5000050.0, mean, 0.01);
        assertTrue(run_time < MAX_RUN_TIME);
    }

    @Test
    public void testColumnMedian(){
        double median;

        start_time = System.currentTimeMillis();
        median = csv.columnMedian(header1);
        run_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start_time);
        assertEquals(50000.5, median, 0.01);
        assertTrue(run_time < MAX_RUN_TIME);

        start_time = System.currentTimeMillis();
        median = csv.columnMedian(header2);
        run_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start_time);
        assertEquals(500005.0, median, 0.01);
        assertTrue(run_time < MAX_RUN_TIME);

        start_time = System.currentTimeMillis();
        median = csv.columnMedian(header3);
        run_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start_time);
        assertEquals(5000050.0, median, 0.01);
        assertTrue(run_time < MAX_RUN_TIME);
    }

    @Test
    public void testColumnOp(){
        testColumnAdd();
        testColumnSubtract();
        testColumnMultiply();
        testColumnDivide();
    }

    private void testColumnAdd(){
        ArrayList<Double> col1 = csv.getColumnNumerical(header1);
        ArrayList<Double> col2 = csv.getColumnNumerical(header2);

        start_time = System.currentTimeMillis();
        ArrayList<Double> col1op2 = csv.applyColumnOp(header1, header2, "+");
        run_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start_time);

        for(int i = 0; i < col1.size(); i++){
            assertEquals(col1.get(i)+col2.get(i), col1op2.get(i), 0.01);
        }
        assertTrue(run_time < MAX_RUN_TIME);
    }

    private void testColumnSubtract(){
        ArrayList<Double> col1 = csv.getColumnNumerical(header1);
        ArrayList<Double> col2 = csv.getColumnNumerical(header2);

        start_time = System.currentTimeMillis();
        ArrayList<Double> col1op2 = csv.applyColumnOp(header1, header2, "-");
        run_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start_time);

        for(int i = 0; i < col1.size(); i++){
            assertEquals(col1.get(i)-col2.get(i), col1op2.get(i), 0.01);
        }
        assertTrue(run_time < MAX_RUN_TIME);
    }

    private void testColumnMultiply(){
        ArrayList<Double> col1 = csv.getColumnNumerical(header1);
        ArrayList<Double> col2 = csv.getColumnNumerical(header2);

        start_time = System.currentTimeMillis();
        ArrayList<Double> col1op2 = csv.applyColumnOp(header1, header2, "*");
        run_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start_time);

        for(int i = 0; i < col1.size(); i++){
            assertEquals(col1.get(i)*col2.get(i), col1op2.get(i), 0.01);
        }
        assertTrue(run_time < MAX_RUN_TIME);
    }

    private void testColumnDivide(){
        ArrayList<Double> col1 = csv.getColumnNumerical(header1);
        ArrayList<Double> col2 = csv.getColumnNumerical(header2);

        start_time = System.currentTimeMillis();
        ArrayList<Double> col1op2 = csv.applyColumnOp(header1, header2, "/");
        run_time = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - start_time);

        for(int i = 0; i < col1.size(); i++){
            assertEquals(col1.get(i)/col2.get(i), col1op2.get(i), 0.01);
        }
        assertTrue(run_time < MAX_RUN_TIME);
    }
}
