
        
import java.io.IOException;
import java.util.*;
        
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
        
public class equijoin {
        

    public static String Table1_Name = "";
    public static String Table2_Name = "";
    public static class Map extends Mapper<LongWritable, Text, Text, Text> {


        public static boolean done = false;

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            Text joinCol = new Text();
            Text row = new Text();        
	        String line = value.toString();
	        line = line.replaceAll(", ",",");
	        String[] cols = line.split(",");
            if (!done) 
            {
                if (Table1_Name.isEmpty())
                    Table1_Name = cols[0];
                else {
                    if (!Table1_Name.equals(cols[0])) {
                        Table2_Name = cols[0];
                        done = true;
                    }
                }
            }
            joinCol.set(cols[1]);
            context.write(joinCol, value);
        }
    }    

        
 public static class Reduce extends Reducer<Text, Text, Text, NullWritable> {

    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        boolean R_is_there = false, S_is_there = false;
	    ArrayList<String> left=new ArrayList<String>();
        ArrayList<String> right=new ArrayList<String>();
        for (Text val : values) {
            String line = val.toString();
            String[] cols = line.split(",");

            if (cols[0].equals(Table1_Name))
            { 
                left.add(line);
                R_is_there = true;
	        }
	        else if(cols[0].equals(Table2_Name))
	        { 
                right.add(line);
                S_is_there = true;
            }
        }
        String joinedTuple = "";
        if (R_is_there && S_is_there)
        {

            for (int i = 0; i < left.size(); i++)
                for (int j = 0; j < right.size(); j++) 
                    joinedTuple = left.get(i) + ", " + right.get(j) +"\n";
                

            context.write(new Text(joinedTuple.substring(0,joinedTuple.length()-1)), null);  

        }
        
    }
 }
        
 public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
        
    Job job = new Job(conf, "equijoin");
    job.setJarByClass(equijoin.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
        
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);
        
    job.setInputFormatClass(TextInputFormat.class);
    job.setOutputFormatClass(TextOutputFormat.class);
        
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
    job.waitForCompletion(true);
 }
        
}
