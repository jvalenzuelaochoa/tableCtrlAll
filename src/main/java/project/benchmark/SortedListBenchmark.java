package project.benchmark;

import org.openjdk.jmh.annotations.*;

import project.tableCtrlManual.ListTable.SortedListTable;
import project.tableCtrlManual.UsrCtrl.User;

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

public class SortedListBenchmark {

	private int queryCounter, scanCounter;
	ArrayList<String> queryValues, queryAtt;
	ArrayList<String> scanAttribute, scanOperand, scanValues;
	SortedListTable testTable;
	
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
    	queryAtt = new ArrayList<String>();
    	scanValues = new ArrayList<String>();
    	scanOperand = new ArrayList<String>();
    	scanAttribute = new ArrayList<String>();
    	testTable = new SortedListTable();
    	    	
    	final String dir = System.getProperty("user.dir");
    	
//      populate structure
        String csvFile = java.nio.file.Paths.get(dir, "testCases", tableName + ".csv").toString();

        BufferedReader br = null;
        String line = "";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] currentUsrAtt = line.split(",");
                testTable.addUser(new User(Integer.parseInt(currentUsrAtt[0]),currentUsrAtt[1], Integer.parseInt(currentUsrAtt[2])));

            }

        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException e) {
            System.out.println("IO Exception");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//      Get elements to query for 
        csvFile = java.nio.file.Paths.get(dir, "testCases", tableName + "_queries_manual.csv").toString();

        br = null;
        line = "";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] currentUsrAtt = line.split(",");
                if(currentUsrAtt[0].equals("EMPLOYEE"))
                {
                	currentUsrAtt[0] = "NAME";
                }
                queryValues.add(currentUsrAtt[2]);
                queryAtt.add(currentUsrAtt[0]);

            }

        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException e) {
            System.out.println("IO Exception");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        // Read Scan file
        csvFile = java.nio.file.Paths.get(dir, "testCases", tableName + "_scan.csv").toString();

        br = null;
        line = "";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] currentUsrAtt = line.split(",");
                if(currentUsrAtt[0].equals("EMPLOYEE"))
                {
                	currentUsrAtt[0] = "NAME";
                }
                scanAttribute.add(currentUsrAtt[0]);
                scanOperand.add(currentUsrAtt[1]);
                scanValues.add(currentUsrAtt[2]);

            }

        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException e) {
            System.out.println("IO Exception");
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
		String queryStr = queryAtt.get(queryCounter) + " = " + queryValues.get(queryCounter);
		System.out.println("QUERY: " + queryStr);
        ArrayList<User> queryResults = testTable.query(queryStr); // instead of value do :val and then theres a separate string for the value

        for(User usr : queryResults)
        {
            System.out.println(usr);
        }
		queryCounter++;
		if(queryCounter == queryValues.size())
		{
			queryCounter = 0;
		}
    }

	
	@Benchmark
    public void testScan() {
		String queryStr = scanAttribute.get(scanCounter) + " " + scanOperand.get(scanCounter) + " " + scanValues.get(scanCounter);

        ArrayList<User> queryResults = testTable.query(queryStr); // instead of value do :val and then theres a separate string for the value

        for(User usr : queryResults)
        {
            System.out.println(usr);
        }

		scanCounter++;
		
		if(scanCounter == scanValues.size())
		{
			scanCounter = 0;
		}

    }
	
}
