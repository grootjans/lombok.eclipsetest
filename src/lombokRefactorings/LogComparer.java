package lombokRefactorings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.LinkedListMultimap;

/*
 * Compare two lombok refactor test logs and report differences
 * 
 * Preparation:
 * - run 'ant test' and copy the lombokrefactor.log file to lombokrefactor.anttest.log
 * - run the 'EclipseWithTests' run target and copy the lombokrefactor.log file to lombokrefactor.eclispe.log
 * 
 * Differences are only reported when the two logs contain the same amount of test results for the same file
 * (this weeds out some false test reports like those for targetClass)
 */
public class LogComparer {
	private static final Pattern filePattern = Pattern.compile("Working on /refactored/src/test/(.*)");
	private static final Pattern testPattern = Pattern.compile(" - (\\d .*)");
	
	public static void main(String[] args) throws IOException {
		File antLog = new File("testenviron.workspace/testNewProj/lombokrefactor.anttest.log");
		File eclipseLog = new File("testenviron.workspace/testNewProj/lombokrefactor.eclipse.log");
		
		if (! antLog.exists() || !eclipseLog.exists()) {
			throw new FileNotFoundException("No logs to compare");
		}
		
		LinkedListMultimap<String, String> antResults = LinkedListMultimap.create();
		LinkedListMultimap<String, String> eclipseResults = LinkedListMultimap.create();
		
		fillResults(antLog, antResults);
		fillResults(eclipseLog, eclipseResults);
		
		for (String fileName : antResults.keySet()) {
			List<String> antList = antResults.get(fileName);
			List<String> eclipseList = eclipseResults.get(fileName);
			if (antList.size() == eclipseList.size()) {
				for (int i = 0; i < antList.size(); i++) {
					if (! antList.get(i).equals(eclipseList.get(i))) {
						System.out.println(fileName);
						System.out.println("  ANT " + antList.get(i));
						System.out.println("  ECL " + eclipseList.get(i));
					}
				}
			}
		}
	}

	private static void fillResults(File file, LinkedListMultimap<String, String> results) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String currentFile = "";
		
		String line = reader.readLine();
		while (line != null) {
			Matcher fileMatcher = filePattern.matcher(line);
			Matcher testMatcher = testPattern.matcher(line);
			if (fileMatcher.matches()) {
				currentFile = fileMatcher.group(1);
			}
			if (testMatcher.matches()) {
				results.put(currentFile, testMatcher.group(1));
			}
			line = reader.readLine();
		}
	}
}
