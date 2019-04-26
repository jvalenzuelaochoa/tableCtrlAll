/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package project.benchmark;

import org.openjdk.jmh.annotations.*;

import project.dynamodb.AWSDynamoDB;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)


public class DynamoDBBenchmark {
    
	private int queryCounter,scanCounter;
	ArrayList<String> queryValues;
	ArrayList<String> scanAttribute, scanOperand, scanValues;
	AWSDynamoDB dynamoDBHelper;
	
    @Param({
            "Users_1000",
            "Users_5000",
            "Users_10000"
    })
    public String tableName;

    @Setup
    public void loadTime(){
    	queryCounter = 0;
    	scanCounter = 0;
    	
    	queryValues = new ArrayList<String>();
    	scanValues = new ArrayList<String>();
    	scanOperand = new ArrayList<String>();
    	scanAttribute = new ArrayList<String>();
    	    	
    	String hostname = "http://localhost:8000";
    	String signingRegion = "us-east-1";

    	dynamoDBHelper = new AWSDynamoDB(hostname, signingRegion);

    	List<String> tableAttributes = new ArrayList<String>();
    	tableAttributes.add("id");

//    	Check if the table already exist. Create otherwise.
    	try {
    		//Check if table exists already
    		if(!dynamoDBHelper.doesTableExist(tableName)) {
    			dynamoDBHelper.createTable(tableName, tableAttributes);
    		}
    		else {
    			System.out.println("Table exists. Skipping Table creation.");
    		}
    	}
        catch (Exception e) {
            System.err.println("DynamoDB Exception: ");
            System.err.println(e.getMessage());
        }

    	
//    	Populate table with input data, the following logic could be moved to the previous try statement. TODO
    	final String dir = System.getProperty("user.dir");
    	java.nio.file.Path tool = java.nio.file.Paths.get(dir, "scripts", "csv_to_dynamodb.py");
        String command = "python3 " + tool +" "+ tableName;

        System.out.println("current dir = " + tool);
        System.out.println(command);

        try {
        	Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
	        	String[] args = new String[] {"/usr/local/bin/python3", tool.toString(), tableName};
	        	Process proc = new ProcessBuilder(args).start();
			} catch (IOException f) {
				// TODO Auto-generated catch block

		        System.out.println("Unable to run python command");
			}
		}


//      Get elements to query for 
        String csvFile = java.nio.file.Paths.get(dir, "testCases", tableName + "_queries_dynamo.csv").toString();

        System.out.println(csvFile);
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] currentUsrAtt = line.split(cvsSplitBy);
                queryValues.add(currentUsrAtt[2]);

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
	@Benchmark
    public void testQuery() {
		dynamoDBHelper.getTableItem(tableName, "id", queryValues.get(queryCounter++), true);
		System.out.println(queryValues.get(queryCounter));
		if(queryCounter == queryValues.size())
		{
			queryCounter = 0;
		}
    }

	
	@Benchmark
    public void testScan() {
//        System.out.println(tableName);
    }
}
