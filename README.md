# csv-util

Utility for reading CSV files and doing stuff.

## Usage
```
usage: java HRT [FILE] [OPTIONS]...
 -co <col_1> ... <col_n>          Columns to output to stdout. Defaults to
                                  all columns being outputted.
 -h                               Help
 -j <col1> <col2> <type>          Perform joins on columns.
                                  Valid types: "inner", "outer"
 -js <side>                       Defaults to left.
                                  Valid outer-join sides: "left", "right"
 -o <file>                        Output file.
 -op <operator> <col_1> <col_2>   Perform operations on columns, Column
                                  headers are ouputted as "col1opcol2"
                                  e.g. “col1 + col2” or “col1 / col2”
                                  which will be output as a new column.
                                  Valid operators: '+', '-', '*', '/'.
 -s <statstic> <col>              Output statistics on a column.
                                  Valid statistics: "max", "min", "mean",
                                  "median."
```
## Examples
Perform addition, subtraction, division, and multiplication on two columns, e.g. “col1 + col2” or “col1 / col2” which will be output as a new column.
```
data.csv -op + col1 col2
```
Output statistics on a column: min, max, median and average.
```
data.csv -s mean col1
```
Perform an column-based outer and inner join on two data sets
```
// inner join
data.csv -j col1 col2 inner -o data_out.csv
// outer join (left)
test_data.csv -j col1 col2 outer -js left -o data_out.csv 
```
Select to only include particular columns for output.

```
// Output col1, col2, col1+col2 only
data.csv -op + col1 col2 -co col1 col2 col1+col2 -o data_out.csv
```
