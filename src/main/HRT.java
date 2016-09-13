package main;

import org.apache.commons.cli.*;

public class HRT {

    public static void main(String[] args) throws ParseException{
        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if(cmd.hasOption("h")){
            help(options);
            return;
        }

        Csv csv = new Csv(args[0]);

        // column statistics
        if(cmd.hasOption("s")){
            String[] stat_args = cmd.getOptionValues("s");
            String statistic = stat_args[0];
            String col = stat_args[1];
            Double result = null;
            switch(statistic){
                case "max": result = csv.columnMax(col);
                    break;
                case "min": result = csv.columnMin(col);
                    break;
                case "mean": result = csv.columnMean(col);
                    break;
                case "median": result = csv.columnMedian(col);
                    break;
            }
            System.out.println(col + " " + statistic + ": " + result);
        }

        // column operations
        if(cmd.hasOption("op")){
            String[] op_args = cmd.getOptionValues("op");
            String operator =  op_args[0];
            String col1 = op_args[1];
            String col2 = op_args[2];
            csv.columnOp(col1, col2, operator);
        }

        // column joins
        if(cmd.hasOption("j")){
            String[] join_args = cmd.getOptionValues("j");
            String col1 = join_args[0];
            String col2 = join_args[1];
            String type = join_args[2];
            String side = "left";
            if(cmd.hasOption("js")){
                side = cmd.getOptionValues("js")[0];
            }
            if(type.equals("inner")){
                csv.innerJoin(col1, col2);
            }else{
                csv.applyOuterJoin(col1, col2, side);
            }
        }

        // output file
        if(cmd.hasOption("o")){
            String file_out = cmd.getOptionValues("o")[0];
            // output specific columns
            if(cmd.hasOption("co")){
                String[] cols_out = cmd.getOptionValues("co");
                for(String col : cols_out){
                    csv.output_headers.put(col, csv.headers.get(col));
                }
            }else{
                for(String col : csv.headers.keySet()){
                    csv.output_headers.put(col, csv.headers.get(col));
                }
            }
            csv.writeCsv(file_out);
        }
//        help(options);
    }

    public static Options createOptions(){
        Options options = new Options();
        Option help_option = new Option("h", "Help");
        Option stat_option = OptionBuilder
                .withArgName("statstic> <col")
                .hasArgs(2)
                .withDescription("Output statistics on a column. \n " +
                        "Valid statistics: \"max\", \"min\", \"mean\", \"median.\"")
                .create("s");
        Option operator_option = OptionBuilder
                .withArgName("operator> <col_1> <col_2")
                .hasArgs(3)
                .withDescription("Perform operations on columns,\n " +
                        "e.g. “col1 + col2” or “col1 / col2” \n " +
                        "which will be output as a new column. \n " +
                        "Valid operators: '+', '-', '*', '/'.")
                .create("op");
        Option file_out_option = OptionBuilder
                .withArgName("file")
                .hasArg()
                .withDescription("Output file.")
                .create("o");
        Option cols_out_option = OptionBuilder
                .withArgName("col_1> ... <col_n")
                .hasArgs(Option.UNLIMITED_VALUES)
                .withDescription("Columns to output to stdout. Defaults to all columns being outputted.")
                .create("co");
        Option join_option = OptionBuilder
                .withArgName("col1> <col2> <type")
                .hasArgs(3)
                .withDescription("Perform joins on columns. \n " +
                        "Valid types: \"inner\", \"outer\"")
                .create("j");
        Option outer_join_side_option = OptionBuilder
                .withArgName("side").hasArg()
                .withDescription("Defaults to left. \n Valid outer-join sides: \"left\", \"right\"")
                .create("js");
        options.addOption(help_option);
        options.addOption(stat_option);
        options.addOption(operator_option);
        options.addOption(cols_out_option);
        options.addOption(file_out_option);
        options.addOption(join_option);
        options.addOption(outer_join_side_option);
        return options;
    }

    private static void help(Options options){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "java HRT [FILE] [OPTIONS]... ", options );
    }
}

