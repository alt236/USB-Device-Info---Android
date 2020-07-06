package aws.apps.usbDeviceEnumerator.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringUtilsTest {


    @Test
    public void properly_pads_empty_string() {
        final String input = "";
        final String expected = "0000";

        assertEquals(expected, StringUtils.padLeft(input, '0', 4));
    }

    @Test
    public void properly_pads_one_character() {
        final String input = "000";
        final String expected = "0000";

        assertEquals(expected, StringUtils.padLeft(input, '0', 4));
    }

}