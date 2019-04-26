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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)


public class DynamoDBBenchmark {
    
//	private int testTime;

    @Param({
            "Users_10",
            "Users_100",
            "Users_1000",
            "Users_5000",
            "Users_10000"
    })
    public String tableName;

    @Setup
    public void loadTime(){
    	String hostname = "http://localhost:8000";
    	String signingRegion = "us-east-1";

    	AWSDynamoDB dynamoDBHelper = new AWSDynamoDB(hostname, signingRegion);

    	List<String> tableAttributes = new ArrayList<String>();
    	tableAttributes.add("id");

    	try {
    		//Check if table exists already
    		if(!dynamoDBHelper.doesTableExist(tableName)) {
    			dynamoDBHelper.createTable(tableName, tableAttributes);
    		}
    		else {
    			System.out.println("Table exists. Skipping Table creation.");
    		}
    		//Save data in python using batch write
//    		dynamoDBHelper.saveData(tableName, primaryKey, imageInfoHashMap);
    	}
        catch (Exception e) {
            System.err.println("DynamoDB Exception: ");
            System.err.println(e.getMessage());
        }

    	final String dir = System.getProperty("user.dir");
    	java.nio.file.Path tool = java.nio.file.Paths.get(dir, "scripts", "csv_to_dynamodb.py");
        String command = tool +" "+ tableName;


        System.out.println("current dir = " + tool);
        System.out.println(command);
        //Runtime.getRuntime().exec(run);

        try {
        	String[] args = new String[] {"python3", tool.toString(), tableName};
        	Process proc = new ProcessBuilder(args).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block

	        System.out.println("Unable to run python command");
		}
        try {
        	String[] args = new String[] {"/usr/local/bin/python3", tool.toString(), tableName};
        	Process proc = new ProcessBuilder(args).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block

	        System.out.println("Unable to run python command");
		}
//    	testTime = Integer.parseInt(sleepTime);
    }
    
	@Benchmark
    public void testMethod() {
//        System.out.println(tableName);
    }

//	@Benchmark
//    public void alternateTest() {
//        doMagic(testTime);
//    }
//    
    public static void doMagic(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }
}
