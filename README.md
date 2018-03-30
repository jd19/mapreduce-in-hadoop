# mapreduce-in-hadoop

The code is in Java (use Java 1.8.x) using Hadoop Framework (use Hadoop 2.7.x).
The code would take two inputs, 
one would be the HDFS location of the file on which the equijoin should be performed and other would be the HDFS location of the file,
where the output should be stored.

## Format of the Input File: -
Table1Name, Table1JoinColumn, Table1Other Column1, Table1OtherColumn2, ……..
Table2Name, Table2JoinColumn, Table2Other Column1, Table2OtherColumn2, ……...

## Format of the Output File: -
If Table1JoinColumn value is equal to Table2JoinColumn value, simply append both line side by side for Output. 
If Table1JoinColumn value does not match any value of Table2JoinColumn, simply remove them for the output file.

## Examples

Example Input : -
R, 2, Don, Larson, Newark, 555-3221
S, 1, 33000, 10000, part1
S, 2, 18000, 2000, part1
R, 3, Sal, Maglite, Nutley, 555-6905
S, 3, 24000, 5000, part1
S, 4, 22000, 7000, part1
R, 4, Bob, Turley, Passaic, 555-8908
Example Output: -
R, 2, Don, Larson, Newark, 555-3221, S, 2, 18000, 2000, part1
R, 3, Sal, Maglite, Nutley, 555-6905, S, 3, 24000, 5000, part1
R, 4, Bob, Turley, Passaic, 555-8908, S, 4, 22000, 7000, part1

## how to run

sudo -u <username> <path_of_hadoop> jar <name_of_jar> <class_with_main_function>
<HDFSinputFile> <HDFSoutputFile>
