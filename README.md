# csv-util

Utility for reading CSV files and doing stuff.

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
 -op <operator> <col_1> <col_2>   Perform operations on columns,
                                  e.g. “col1 + col2” or “col1 / col2”
                                  which will be output as a new column.
                                  Valid operators: '+', '-', '*', '/'.
 -s <statstic> <col>              Output statistics on a column.
                                  Valid statistics: "max", "min", "mean",
                                  "median."
```
