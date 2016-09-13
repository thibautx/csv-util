package main;
import java.io.*;
import java.util.*;

public class Csv {

    private static final String COMMA_DELIMITER = ",";
    private static final String NEWLINE = "\n";

    String file_in;
    HashMap<String, ArrayList<String>> headers = new LinkedHashMap<>();           // map column headers to index
    HashMap<String, ArrayList<String>> output_headers = new LinkedHashMap<>();    // specific output columns
    ArrayList<ArrayList<String>> columns = new ArrayList<>();                     // list of columns

    /**
     *
     * @param file_in input file
     */
    public Csv(String file_in){
        this.file_in = file_in;
        this.readCsv();
    }

    /**
     * Read the csv file
     */
    private void readCsv(){
        try(BufferedReader br = new BufferedReader(new FileReader(this.file_in))){
            String line;

            // process the headers
            line = br.readLine();
            String[] headers = line.split(",");
            for(String header : headers){
                ArrayList<String> column = new ArrayList<>();
                this.headers.put(header, column);
                this.columns.add(column);
            }

            // process rest of csv
            while((line = br.readLine()) != null) {
                String[] row = line.split(",");
                for(int i = 0; i < row.length; i++){
                    this.columns.get(i).add(row[i]);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Output
     */
    public void writeCsv(String file_out){
        BufferedWriter bw = null;
        try{
            bw = new BufferedWriter(new FileWriter(file_out));

            // write headers
            bw.write(this.getHeadersRow());

            // write the rows
            int n_rows = this.columns.get(0).size();
            for(int i = 0; i < n_rows; i++){
                bw.write(this.getRow(i));
            }

        } catch(IOException e){
            System.out.println("bad out file");
            e.printStackTrace();
        } finally{
            try{
                bw.close();
            } catch(IOException|NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Get headers for csv.
     * @return string headers as csv
     */
    private String getHeadersRow(){
        String header_row = "";
        // if no output headers specify, default to output all columns
        if(this.output_headers.size() == 0){
            this.output_headers = this.headers;
        }
        for(String header: output_headers.keySet()){
            header_row += header + COMMA_DELIMITER;
        }
        return header_row.substring(0, header_row.length()-1) + NEWLINE;  // replace extra comma w/ newline
    }

    /**
     * Get row at an index.
     * @param i row index
     * @return string (csv) of row
     */
    private String getRow(int i){
        String row = "";
        for(ArrayList<String> column : this.output_headers.values()){
//            try{
            row += column.get(i) + COMMA_DELIMITER;
//            } catch (IndexOutOfBoundsException e){
//                row += " " + COMMA_DELIMITER; // after joins, columns may be mis-sized
//            }
        }
        return row.substring(0, row.length()-1) + NEWLINE;  // replace extra comma w/ newline
    }

    /**
     * Converts the string column to column of doubles
     * @param header string
     * @return column as doubles
     */
    public ArrayList<Double> getColumnNumerical(String header){
        ArrayList<String> column = this.headers.get(header);
        ArrayList<Double> column_numerical = new ArrayList<>();
        for(String stringValue : column) {
            try{
                column_numerical.add(Double.parseDouble(stringValue));
            } catch(NumberFormatException e){
                System.out.println("Could not parse " + e);
            }
        }
        return column_numerical;
    }

    /**
     * Converts the column of doubles back to string, used for writing back out
     * @param column column to convert
     * @return column as strings
     */
    private ArrayList<String> columnDoubleToString(ArrayList<Double> column){
        ArrayList<String> column_string = new ArrayList<>();
        for(Double double_value : column){
            column_string.add(Double.toString(double_value));
        }
        return column_string;
    }

    /**
     * Add new column (i.e. after column operation)
     * @param header
     * @param column
     */
    public void addNewColumn(String header, ArrayList<Double> column){
        ArrayList<String> columnString = this.columnDoubleToString(column);
        this.columns.add(columnString);
        this.headers.put(header, columnString);
        this.output_headers.put(header, columnString); // all columns from column ops are outputted
    }

    /**
     * Reference: http://stackoverflow.com/a/38578/3146064
     * @param header1
     * @param header2
     */
    public void innerJoin(String header1, String header2){
        ArrayList<String> col1 = this.headers.get(header1);
        ArrayList<String> col2 = this.headers.get(header1);
        ArrayList<String> intersection = new ArrayList<>();
        HashMap<String, Integer> col2_vals = this.hashArrayList(col2);

        for(String col1_val : col1){
            if(col2_vals.get(col1_val) != null){
                intersection.add(col1_val);
            }
        }
        // replace both columns with the inner-join
        this.headers.put(header1, intersection);
        this.headers.put(header2, intersection);
    }

    /**
     *
     * @param header1
     * @param header2
     * @param side
     */
    public void applyOuterJoin(String header1, String header2, String side){
        if(side.equals("full")){
            ArrayList<String> joined_col1 = outerJoin(header1, header2);
            ArrayList<String> joined_col2 = outerJoin(header2, header1);
        }else{
            ArrayList<String> joined_col = null;
            String joined_header = null;
            switch(side){
                // A left outer join will give all rows in A, plus any common rows in B
                case "left": joined_col = outerJoin(header1, header2);
                    joined_header = header2;
                    break;
                // A right outer join will give all rows in B, plus any common rows in A.
                case "right": joined_col = outerJoin(header2, header1);
                    joined_header = header1;
                    break;
            }
            this.headers.put(joined_header, joined_col);
        }
    }

    private ArrayList<String> outerJoin(String header1, String header2){
        ArrayList<String> col1 = this.headers.get(header1);
        ArrayList<String> col2 = this.headers.get(header2);
        HashMap<String, Integer> col_vals = this.hashArrayList(col2);
        ArrayList<String> col_joined = new ArrayList<>();

        for(String val : col1){
            if(col_vals.get(val) != null){
                col_joined.add(val);
            }else{
                col_joined.add("null");
            }
        }
        return col_joined;
    }

    private HashMap<String, Integer> hashArrayList(ArrayList<String> list){
        HashMap<String, Integer> vals = new HashMap<>();
        for(String val : list){
            vals.put(val, 1);
        }
        return vals;
    }

    /**
     * Apply col1 operator col2
     * @param header1 col1
     * @param header2 col2
     * @param operator "+" / "-" / "*" / "/"
     */
    public void columnOp(String header1, String header2, String operator){
        ArrayList<Double> col1opcol2 = this.applyColumnOp(header1, header2, operator);
        String col1opcol2_header = header1 + operator + header2;
        this.addNewColumn(col1opcol2_header, col1opcol2);
    }

    interface MathOp{
        double apply(double a, double b);
    }
    MathOp op_addition = (double a, double b) -> a+b;
    MathOp op_subtraction = (double a, double b) -> a-b;
    MathOp op_multiplication = (double a, double b) -> a*b;
    MathOp op_division = (double a, double b) -> a/b;

    public ArrayList<Double> applyColumnOp(String header1, String header2, String operator){
        MathOp op = null;
        switch(operator){
            case "+": op = op_addition;
                break;
            case "-": op = op_subtraction;
                break;
            case "*": op = op_multiplication;
                break;
            case "/": op = op_division;
                break;
        }
        ArrayList<Double> column1 = this.getColumnNumerical(header1);
        ArrayList<Double> column2 = this.getColumnNumerical(header2);
        Double[] result = new Double[column1.size()];
        for(int i = 0; i < column1.size(); i++){
            result[i] = op.apply(column1.get(i), column2.get(i));
        }
        return new ArrayList<>(Arrays.asList(result));
    }

    /**
     * Get max element in a column
     * @param header the column's header
     * @return max element
     */
    public double columnMax(String header){
        ArrayList<Double> column = this.getColumnNumerical(header);
        double max = Integer.MIN_VALUE;
        for(Double element : column){
            if(element > max){
                max = element;
            }
        }
        return max;
    }

    /**
     * Get min element of a column
     * @param header the column's header
     * @return min element
     */
    public double columnMin(String header){
        ArrayList<Double> column = this.getColumnNumerical(header);
        double min = Integer.MAX_VALUE;
        for(Double element : column){
            if(element < min){
                min = element;
            }
        }
        return min;
    }

    /**
     * Get mean of a column
     * @param header the column's header
     * @return the mean
     */
    public double columnMean(String header){
        ArrayList<Double> column = this.getColumnNumerical(header);
        double sum = 0.0;
        for(Double element : column){
            sum += element;
        }
        return sum/column.size();
    }

    /**
     * Get median of a column
     * @param header the column's header
     * @return the median element
     */
    public double columnMedian(String header){

        ArrayList<Double> column = this.getColumnNumerical(header);
        int initial_size = column.size()/2;

        PriorityQueue<Double> min_heap = new PriorityQueue(initial_size, new MinHeapComparator());
        PriorityQueue<Double> max_heap = new PriorityQueue(initial_size, new MaxHeapComparator());

        for(double element : column){
            if(max_heap.isEmpty()){
                max_heap.add(element);
            }else if(element < max_heap.peek()){
                max_heap.add(element);
            }else{
                min_heap.add(element);
            }

            // balance the heaps
            if(Math.abs(max_heap.size() - min_heap.size()) > 1){
                PriorityQueue<Double> smaller_heap = (max_heap.size() < min_heap.size()) ? max_heap : min_heap;
                PriorityQueue<Double> larger_heap = (max_heap.size() > min_heap.size()) ? max_heap : min_heap;
                smaller_heap.add(larger_heap.remove());
            }
            assert Math.abs(max_heap.size() - min_heap.size()) <= 1;
        }
        // if even number of elements, return the average of the two medians
        if(max_heap.size() == min_heap.size()){
            return (max_heap.remove() + min_heap.remove())/2.0;
        }else{
            PriorityQueue<Double> larger_heap = (max_heap.size() > min_heap.size()) ? max_heap : min_heap;
            return larger_heap.remove();
        }
    }

    class MaxHeapComparator implements Comparator<Double> {
        public int compare(Double x, Double y){
            return x > y ? -1 : x.equals(y) ? 0 : 1;
        }
    }

    class MinHeapComparator implements Comparator<Double> {
        public int compare(Double x, Double y){
            return x < y ? -1 : x.equals(y) ? 0 : 1;
        }
    }
}

