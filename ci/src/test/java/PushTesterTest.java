import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ci.PushTester;
import ci.PushStatus;

/**
 * Test the the PushTester class
 */
public class PushTesterTest {


    @Test
    public void TestCompilableCodePassingAllTests(){
        // Testing compilable code with passing test returns a PushStatus ps with
        // testSuccess and compileSuccess as True
        File localDirectory = new File("./PushTesterTestDirectories/CompilableCodePassingAllTests/");
        PushTester pt = new PushTester();
        PushStatus ps = pt.createPushStatus(localDirectory, "1", "2022-02-12 21:30:52 +01:00");
        assertTrue(ps.getTestSuccess());
        assertTrue(ps.getCompileSuccess());


    }

    @Test
    public void TestCompilableCodeFailingOneTest(){
        // Testing compilable code with failing test returns a PushStatus ps with
        // testSuccess as False and compileSuccess as True
        File localDirectory = new File("./PushTesterTestDirectories/CompilableCodeFailingTests/");
        PushTester pt = new PushTester();
        PushStatus ps = pt.createPushStatus(localDirectory, "1", "2022-02-12 21:30:52 +01:00");
        assertFalse(ps.getTestSuccess());
        assertTrue(ps.getCompileSuccess());

    }

    @Test
    public void TestNotCompilableCode(){
        // Testing not compilable code with passing test returns a PushStatus ps with
        // testSuccess and compileSuccess as False
        File localDirectory = new File("./PushTesterTestDirectories/NotCompilableCode/");
        PushTester pt = new PushTester();
        PushStatus ps = pt.createPushStatus(localDirectory, "1", "2022-02-12 21:30:52 +01:00");
        assertFalse(ps.getTestSuccess());
        assertFalse(ps.getCompileSuccess());
    }



}