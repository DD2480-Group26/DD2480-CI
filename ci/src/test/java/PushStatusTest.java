import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ci.PushStatus;

public class PushStatusTest {

    @Test
    public void TestFormatCommitDate() {
        PushStatus pushStatus = new PushStatus("gkj42lkc43l21", "2022-02-12T21:30:52+01:00", true, true, "", "");
        assertEquals("2022-02-12 21:30:52 +01:00", pushStatus.getCommitDate());
    }
}
