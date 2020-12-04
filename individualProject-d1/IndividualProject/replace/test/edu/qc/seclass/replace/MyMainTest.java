package edu.qc.seclass.replace;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class MyMainTest {

    private ByteArrayOutputStream outStream;
    private ByteArrayOutputStream errStream;
    private PrintStream outOrig;
    private PrintStream errOrig;
    private Charset charset = StandardCharsets.UTF_8;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        outStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outStream);
        errStream = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(errStream);
        outOrig = System.out;
        errOrig = System.err;
        System.setOut(out);
        System.setErr(err);
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(outOrig);
        System.setErr(errOrig);
    }

    // Some utilities

    private File createTmpFile() throws IOException {
        File tmpfile = temporaryFolder.newFile();
        tmpfile.deleteOnExit();
        return tmpfile;
    }

    private File createInputFile1() throws Exception {
        File file1 = createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!");

        fileWriter.close();
        return file1;
    }

    private File createInputFile2() throws Exception {
        File file1 = createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("Howdy Bill,\n" +
                "This is another test file for the replace utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bill\" twice");

        fileWriter.close();
        return file1;
    }

    private File createInputFile3() throws Exception {
        File file1 = createTmpFile();
        FileWriter fileWriter = new FileWriter(file1);

        fileWriter.write("Howdy Bill, have you learned your abc and 123?\n" +
                "It is important to know your abc and 123," +
                "so you should study it\n" +
                "and then repeat with me: abc and 123");

        fileWriter.close();
        return file1;
    }

    private String getFileContent(String filename) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(filename)), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    //----------------- Actual test cases-------------------------------------------------

    /*
    Test Case 1  		<single>
    Size :  Empty
    */
    @Test(expected = FileNotFoundException.class)
    public void myMainTest1() throws Exception {
        String args[] = {"Howdy", "Willson", "--"};
        Main.main(args);
    }

    /*
     *Test Case 6  		<error>
     *Options :  Opt not found
     */
    @Test
    public void myMainTest2() throws Exception {

        File inputFile1 = createInputFile1();
        String args[] = {"a ", "the ", "--", inputFile1.getPath()};
        Main.main(args);
        String expected1 = "Howdy Bill,\n" +
                "This is the test file for the replace utility\n" +
                "Let's make sure it has at least the few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("These strings differ", expected1, actual1);
    }

    /*
   *Test Case 11 		(Key = 2.3.0.1.2.1.2.1.)
   Size                                            :  Not empty
   Number of files provided                        :  One
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -b
   Parameter from                                  :  One
   Parameter to                                    :  Zero
   Number of matches of the pattern in second file :  One
   Replace Value                                   :  Replace backUp
   */
    @Test
    public void myMainTest3() throws Exception {

        File inputFile1 = createInputFile1();

        String args[] = {"-b", "make", "Make", "--", inputFile1.getPath()};
        Main.main(args);
        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's Make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("These strings differ", expected1, actual1);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }

    /*
   Test Case 31 		(Key = 2.3.0.2.2.1.2.2.)
   Size                                            :  Not empty
   Number of files provided                        :  Many
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -f
   Parameter from                                  :  One
   Parameter to                                    :  Zero
   Number of matches of the pattern in second file :  One
   Replace Value                                   :  Replace firstOccurence
   */

    @Test
    public void myMainTest4() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();

        String args[] = {"-f", "Howdy", "How are you", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);

        String expected1 = "How are you Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "How are you Bill,\n" +
                "This is another test file for the replace utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bill\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    /*
   Test Case 30 		(Key = 2.3.0.2.2.1.1.2.)
   Size                                            :  Not empty
   Number of files provided                        :  Many
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -f
   Parameter from                                  :  One
   Parameter to                                    :  Zero
   Number of matches of the pattern in second file :  Zero
   Replace Value                                   :  Replace firstOccurence
   */
    @Test
    public void myMainTest5() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();

        String args[] = {"-f", "zoo", "museum", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy Bill,\n" +
                "This is another test file for the replace utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bill\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    /*
    Test Case 41 		(Key = 2.3.0.2.3.1.3.2.)
   Size                                            :  Not empty
   Number of files provided                        :  Many
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -f
   Parameter from                                  :  Many
   Parameter to                                    :  Zero
   Number of matches of the pattern in second file :  Many
   Replace Value                                   :  Replace firstOccurence*/
    @Test
    public void myMainTest6() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();

        String args[] = {"-f", "at", "AAA", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has AAA least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy Bill,\n" +
                "This is another test file for the replace utility\n" +
                "thAAA contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bill\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

/*Test Case 50 		(Key = 2.3.0.3.2.1.1.3.)
   Size                                            :  Not empty
   Number of files provided                        :  Many
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -l
   Parameter from                                  :  One
   Parameter to                                    :  Zero
   Number of matches of the pattern in second file :  Zero
   Replace Value                                   :  Replace lastOccurence
 */
/*Test Case 71 		(Key = 2.3.0.4.2.1.1.4.)
   Size                                            :  Not empty
   Number of files provided                        :  Many
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -i
   Parameter from                                  :  One
   Parameter to                                    :  Zero
   Number of matches of the pattern in second file :  One
   Replace Value                                   :  Replace sensitiveCase*/
@Test
public void myMainTest7() throws Exception {
    File inputFile1 = createInputFile1();
    File inputFile2 = createInputFile2();

    String args[] = {"-i", "file", "", "--", inputFile1.getPath(), inputFile2.getPath()};
    Main.main(args);

    String expected1 = "Howdy Bill,\n" +
            "This is a test  for the replace utility\n" +
            "Let's make sure it has at least a few lines\n" +
            "so that we can create some interesting test cases...\n" +
            "And let's say \"howdy bill\" again!";

    String expected2 = "Howdy Bill,\n" +
            "This is another test  for the replace utility\n" +
            "that contains a list:\n" +
            "-a) Item 1\n" +
            "-b) Item 2\n" +
            "...\n" +
            "and says \"howdy Bill\" twice";

    String actual1 = getFileContent(inputFile1.getPath());
    String actual2 = getFileContent(inputFile2.getPath());

    assertEquals(expected1, actual1);
    assertEquals(expected2, actual2);
}
/*Test Case 50 		(Key = 2.3.0.3.2.1.1.3.)
   Size                                            :  Not empty
   Number of files provided                        :  Many
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -l
   Parameter from                                  :  One
   Parameter to                                    :  Zero
   Number of matches of the pattern in second file :  Zero
   Replace Value                                   :  Replace lastOccurence
   */
    @Test
    public void myMainTest8() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();

        String args[] = {"-l", "z", "zoo", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy Bill,\n" +
                "This is another test file for the replace utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bill\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    /*
    * Test Case 51 		(Key = 2.3.0.3.2.1.2.3.)
   Size                                            :  Not empty
   Number of files provided                        :  Many
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -l
   Parameter from                                  :  One
   Parameter to                                    :  Zero
   Number of matches of the pattern in second file :  One
   Replace Value                                   :  Replace lastOccurence
   */

    @Test
    public void myMainTest9() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();

        String args[] = {"-l", "ill", "ILL", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bILL\" again!";

        String expected2 = "Howdy Bill,\n" +
                "This is another test file for the replace utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bill\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    /*
    * Test Case 55 		(Key = 2.3.0.3.2.3.2.3.)
   Size                                            :  Not empty
   Number of files provided                        :  Many
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -l
   Parameter from                                  :  One
   Parameter to                                    :  Many
   Number of matches of the pattern in second file :  One
   Replace Value                                   :  Replace lastOccurence
   * */

    @Test
    public void myMainTest10() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();

        String args[] = {"-l", "howdy", "Robert", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"Robert bill\" again!";

        String expected2 = "Howdy Bill,\n" +
                "This is another test file for the replace utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bill\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    /*
   Test Case 56 		(Key = 2.3.0.3.2.4.1.3.)
   Size                                            :  Not empty
   Number of files provided                        :  Many
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -l
   Parameter from                                  :  One
   Parameter to                                    :  Upper case
   Number of matches of the pattern in second file :  Zero
   Replace Value                                   :  Replace lastOccurence
*/

    @Test
    public void myMainTest11() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();

        String args[] = {"-l", "z", "Z", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy Bill,\n" +
                "This is another test file for the replace utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bill\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    /*Test Case 57 		(Key = 2.3.0.3.2.4.2.3.)
   Size                                            :  Not empty
   Number of files provided                        :  One
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -l
   Parameter from                                  :  One
   Parameter to                                    :  Upper case
   Number of matches of the pattern in second file :  One
   Replace Value                                   :  Replace lastOccurence*/
    @Test
    public void myMainTest12() throws Exception {
        File inputFile1 = createInputFile1();
        String args[] = {"-l", "howdy", "HOWDY", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"HOWDY bill\" again!";
        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals(expected1, actual1);
    }
    /*
   Test Case 59 		(Key = 2.3.0.3.2.5.2.3.)
   Size                                            :  Not empty
   Number of files provided                        :  One
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -l
   Parameter from                                  :  One
   Parameter to                                    :  Lower case
   Number of matches of the pattern in second file :  One
   Replace Value                                   :  Replace lastOccurence*/

    @Test
    public void myMainTest13() throws Exception {
        File inputFile1 = createInputFile1();
        String args[] = {"-l", "Howdy", "howdy", "--", inputFile1.getPath()};
        Main.main(args);
        String expected1 = "howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";
        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals(expected1, actual1);
    }

    /*
    * Test Case 70 		(Key = 2.3.0.4.2.1.1.4.)
    Size                                            :  Not empty
    Number of files provided                        :  Many
    Whether the files specifies exist               :  <n/a>
    Options                                         :  -i
    Parameter from                                  :  One
    Parameter to                                    :  Zero
    Number of matches of the pattern in second file :  Zero
    Replace Value                                   :  Replace sensitiveCase*/
    @Test
    public void myMainTest14() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();

        String args[] = {"-i", "z", "Z", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String expected2 = "Howdy Bill,\n" +
                "This is another test file for the replace utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bill\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

   /*Test Case 71 		(Key = 2.3.0.4.2.1.2.4.)
   Size                                            :  Not empty
   Number of files provided                        :  Many
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -i
   Parameter from                                  :  One
   Parameter to                                    :  Zero
   Number of matches of the pattern in second file :  One
   Replace Value                                   :  Replace sensitiveCase
*/

    @Test
    public void myMainTest15() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();
        String args[] = {"-i", "Howdy", "Hi", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);
        String expected1 = "Hi Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"Hi bill\" again!";

        String expected2 = "Hi Bill,\n" +
                "This is another test file for the replace utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"Hi Bill\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        String actual2 = getFileContent(inputFile2.getPath());
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    /*Test Case 85 		(Key = 2.3.0.4.3.3.3.4.)
   Size                                            :  Not empty
   Number of files provided                        :  Many
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -i
   Parameter from                                  :  Many
   Parameter to                                    :  Many
   Number of matches of the pattern in second file :  Many
   Replace Value                                   :  Replace sensitiveCase*/

    @Test
    public void myMainTest16() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();

        String args[] = {"-i", "a", "AaA", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);
        String expected1 = "Howdy Bill,\n" +
                "This is AaA test file for the replAaAce utility\n" +
                "Let's mAaAke sure it hAaAs AaAt leAaAst AaA few lines\n" +
                "so thAaAt we cAaAn creAaAte some interesting test cAaAses...\n" +
                "AaAnd let's sAaAy \"howdy bill\" AaAgAaAin!";

        String expected2 = "Howdy Bill,\n" +
                "This is AaAnother test file for the replAaAce utility\n" +
                "thAaAt contAaAins AaA list:\n" +
                "-AaA) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "AaAnd sAaAys \"howdy Bill\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals(expected1, actual1);
        String actual2 = getFileContent(inputFile2.getPath());
        assertEquals(expected2, actual2);
    }

    /*
   Test Case 27 		(Key = 2.3.0.1.3.4.3.1.)
   Size                                            :  Not empty
   Number of files provided                        :  Many
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -b
   Parameter from                                  :  Many
   Parameter to                                    :  Upper case
   Number of matches of the pattern in second file :  Many
   Replace Value                                   :  Replace backUp
    */

    @Test
    public void myMainTest17() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = {"-b", "a", "A", "--", inputFile1.getPath()};
        Main.main(args);
        String expected1 = "Howdy Bill,\n" +
                "This is A test file for the replAce utility\n" +
                "Let's mAke sure it hAs At leAst A few lines\n" +
                "so thAt we cAn creAte some interesting test cAses...\n" +
                "And let's sAy \"howdy bill\" AgAin!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("These strings differ", expected1, actual1);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }

    /*
   Test Case 10 		(Key = 2.3.0.1.2.1.1.1.)
   Size                                            :  Not empty
   Number of files provided                        :  Many
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -b
   Parameter from                                  :  One
   Parameter to                                    :  Zero
   Number of matches of the pattern in second file :  Zero
   Replace Value                                   :  Replace backUp
    */

    @Test
    public void myMainTest18() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = {"-b", "z", "X", "--", inputFile1.getPath()};
        Main.main(args);
        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("These strings differ", expected1, actual1);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }

    /*
   Test Case 87 		(Key = 2.3.0.4.3.4.3.4.)
   Size                                            :  Not empty
   Number of files provided                        :  Many
   Whether the files specifies exist               :  <n/a>
   Options                                         :  -i
   Parameter from                                  :  Many
   Parameter to                                    :  Upper case
   Number of matches of the pattern in second file :  Many
   Replace Value                                   :  Replace sensitiveCase
    */

    @Test
    public void myMainTest19() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();
        String args[] = {"-i", "ay", "AYYYY", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);
        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's sAYYYY \"howdy bill\" again!";
        String expected2 = "Howdy Bill,\n" +
                "This is another test file for the replace utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and sAYYYYs \"howdy Bill\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals(expected1, actual1);

        String actual2 = getFileContent(inputFile2.getPath());
        assertEquals(expected2, actual2);
    }
    //New test case. Test replace sensitive case and create backup file before replacing
    @Test
    public void myMainTest20() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();
        String args[] = {"-i", "-b","ill", "ucky", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);
        String expected1 = "Howdy Bucky,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bucky\" again!";
        String expected2 = "Howdy Bucky,\n" +
                "This is another test file for the replace utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bucky\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals(expected1, actual1);

        String actual2 = getFileContent(inputFile2.getPath());
        assertEquals(expected2, actual2);

        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }

    //New test case. Test replace first occurance and create backup file before replacing
    @Test
    public void myMainTest21() throws Exception {
        File inputFile1 = createInputFile1();
        File inputFile2 = createInputFile2();
        String args[] = {"-f", "-b","ill", "ucky", "--", inputFile1.getPath(), inputFile2.getPath()};
        Main.main(args);
        String expected1 = "Howdy Bucky,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";
        String expected2 = "Howdy Bucky,\n" +
                "This is another test file for the replace utility\n" +
                "that contains a list:\n" +
                "-a) Item 1\n" +
                "-b) Item 2\n" +
                "...\n" +
                "and says \"howdy Bill\" twice";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals(expected1, actual1);

        String actual2 = getFileContent(inputFile2.getPath());
        assertEquals(expected2, actual2);

        assertTrue(Files.exists(Paths.get(inputFile2.getPath() + ".bck")));
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }

    //New test case. Test replace last occurance and create backup file before replacing
    @Test
    public void myMainTest22() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = {"-l", "-b", "ill", "ucky", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bucky\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals(expected1, actual1);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }
    //New test case. Test replace first and last occurance
    @Test
    public void myMainTest23() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = {"-f", "-l", "a", "the", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is the test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" agthein!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals(expected1, actual1);

    }

    //New test case. Test syntax match properly otherwise fail
    @Test
    public void myMainTest24() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = {"-f", "l", "a", "the", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals(expected1, actual1);
    }
    //New test case. Test if wrong letter is used as opt then replace won't happen
    @Test
    public void myMainTest25() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = {"-p","a", "the", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals(expected1, actual1);
    }

    //New test case. Test case sensitive
    @Test
    public void myMainTest26() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = {"-i","oWdY", "UCKY", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "HUCKY Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"hUCKY bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals(expected1, actual1);
    }
    //New test case. Test replace special character with new special character
    @Test
    public void myMainTest27() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = {"-i","\n", "\t", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\tThis is a test file for the replace utility\tLet's make sure it has at least a few lines\tso that we can create some interesting test cases...\tAnd let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals(expected1, actual1);
    }
    //New test case. Test replace first special character(\n new line) with new special character
    @Test
    public void myMainTest28() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = {"-f","\n", "\t", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\tThis is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals(expected1, actual1);
    }
    //New test case. Test replace first special character(\n new line) with new special character
    @Test
    public void myMainTest29() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = {"-l","\n", "\t", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\nThis is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\t" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals(expected1, actual1);
    }
    //New Test case. Test replace last occurance special character and create a backup copy of each file
    @Test
    public void myMainTest30() throws Exception {
        File inputFile1 = createInputFile1();

        String args[] = {"-b","-l","\n", "\t", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\nThis is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\t" +
                "And let's say \"howdy bill\" again!";

        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals(expected1, actual1);
    }

    //Newly test case. Test combination of three opt operation
    @Test
    public void myMainTest31() throws Exception{
        File inputFile3 = createInputFile3();

        String args[] = {"-i", "-l", "-b", "123", "789", "--", inputFile3.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill, have you learned your abc and 123?\n" +
                "It is important to know your abc and 123,so you should study it\n" +
                "and then repeat with me: abc and 789";

        String actual1 = getFileContent(inputFile3.getPath());

        assertEquals("Those strings differ", expected1, actual1);

        assertTrue(Files.exists(Paths.get(inputFile3.getPath() + ".bck")));
    }

    //Newly test case. Test combination of three opt operation
    @Test
    public void myMainTest32() throws Exception{
        File inputFile3 = createInputFile3();

        String args[] = {"-i", "-f", "-b", "123", "789", "--", inputFile3.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill, have you learned your abc and 789?\n" +
                "It is important to know your abc and 123,so you should study it\n" +
                "and then repeat with me: abc and 123";

        String actual1 = getFileContent(inputFile3.getPath());
        assertEquals("Those strings differ", expected1, actual1);
        assertTrue(Files.exists(Paths.get(inputFile3.getPath() + ".bck")));
    }
    //Newly test case. Test combination of three opt operation
    @Test
    public void myMainTest33() throws Exception{
        File inputFile1 = createInputFile1();

        String args[] = {"-i", "-f", "-l", "bill", "Bucky", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bucky,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy Bucky\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("Those strings differ", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }
    //Newly test case. Test combination of three opt operation
    @Test
    public void myMainTest34() throws Exception{
        File inputFile1 = createInputFile1();

        String args[] = {"-b", "-f", "-l", "ill", "ucky", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bucky,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bucky\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("Those strings differ", expected1, actual1);
        assertTrue(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }

    //Newly test case. Test combination of three opt operation. If one opt operation has missing -, replace won't takr place and won't backup the replaced file
    @Test
    public void myMainTest35() throws Exception{
        File inputFile1 = createInputFile1();

        String args[] = {"-b", "-f", "l", "ill", "ucky", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("Those strings differ", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }
    //Newly test case. Test combination of three opt operation. If one opt operation has missing -, replace won't takr place and won't backup the replaced file
    @Test
    public void myMainTest36() throws Exception{
        File inputFile1 = createInputFile1();

        String args[] = {"-b", "-i", "l", "ill", "ucky", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("Those strings differ", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }

    //Newly test case. Test combination of three opt operation. If one opt operation has missing -, replace won't takr place and won't backup the replaced file
    @Test
    public void myMainTest37() throws Exception{
        File inputFile1 = createInputFile1();

        String args[] = {"-b", "-f", "i", "ill", "ucky", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("Those strings differ", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }
    //Newly test case. Test combination of three opt operation. If one opt operation has missing -, replace won't takr place and won't backup the replaced file
    @Test
    public void myMainTest38() throws Exception{
        File inputFile1 = createInputFile1();

        String args[] = {"b", "-i", "-l", "ill", "ucky", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("Those strings differ", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }

    //Newly test case. Test combination of three opt operation. If one opt operation has missing -, replace won't takr place and won't backup the replaced file
    @Test
    public void myMainTest39() throws Exception{
        File inputFile1 = createInputFile1();

        String args[] = {"b", "i", "-l", "ill", "ucky", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("Those strings differ", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }

    //Newly test case. Test combination of three opt operation. If one opt operation has missing -, replace won't takr place and won't backup the replaced file
    @Test
    public void myMainTest40() throws Exception{
        File inputFile1 = createInputFile1();

        String args[] = {"b", "i", "l", "ill", "ucky", "--", inputFile1.getPath()};
        Main.main(args);

        String expected1 = "Howdy Bill,\n" +
                "This is a test file for the replace utility\n" +
                "Let's make sure it has at least a few lines\n" +
                "so that we can create some interesting test cases...\n" +
                "And let's say \"howdy bill\" again!";

        String actual1 = getFileContent(inputFile1.getPath());
        assertEquals("Those strings differ", expected1, actual1);
        assertFalse(Files.exists(Paths.get(inputFile1.getPath() + ".bck")));
    }
}