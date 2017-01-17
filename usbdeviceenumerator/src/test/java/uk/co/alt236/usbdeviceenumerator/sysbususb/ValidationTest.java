package uk.co.alt236.usbdeviceenumerator.sysbususb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class ValidationTest {
    private Validation cut;

    @Before
    public void setUp() {
        cut = new Validation();
    }

    @Test
    public void isValidUsbDeviceCandidate_not_existent() throws Exception {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(false);

        assertFalse(cut.isValidUsbDeviceCandidate(mockFile));
    }

    @Test
    public void isValidUsbDeviceCandidate_not_directory() throws Exception {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(true);
        Mockito.when(mockFile.isDirectory()).thenReturn(false);

        assertFalse(cut.isValidUsbDeviceCandidate(mockFile));
    }

    @Test
    public void isValidUsbDeviceCandidate_is_current_dir() throws Exception {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(true);
        Mockito.when(mockFile.isDirectory()).thenReturn(true);
        Mockito.when(mockFile.getName()).thenReturn(".");

        assertFalse(cut.isValidUsbDeviceCandidate(mockFile));
    }

    @Test
    public void isValidUsbDeviceCandidate_is_parent_dir() throws Exception {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(true);
        Mockito.when(mockFile.isDirectory()).thenReturn(true);
        Mockito.when(mockFile.getName()).thenReturn("..");

        assertFalse(cut.isValidUsbDeviceCandidate(mockFile));
    }

    @Test
    public void isValidUsbDeviceCandidate_is_valid() throws Exception {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(true);
        Mockito.when(mockFile.isDirectory()).thenReturn(true);
        Mockito.when(mockFile.getName()).thenReturn("foo");

        assertTrue(cut.isValidUsbDeviceCandidate(mockFile));
    }

    @Test(expected = NullPointerException.class)
    public void isValidUsbDeviceCandidate_is_null() throws Exception {
        final File mockFile = null;
        cut.isValidUsbDeviceCandidate(mockFile);
    }

    @Test(expected = NullPointerException.class)
    public void getListOfChildren_is_null() throws Exception {
        final File mockFile = null;
        cut.getListOfChildren(mockFile);
    }

    @Test
    public void getListOfChildren_not_existent() throws Exception {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(false);

        final File[] result = cut.getListOfChildren(mockFile);
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void getListOfChildren_not_directory() throws Exception {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(true);
        Mockito.when(mockFile.isDirectory()).thenReturn(false);

        final File[] result = cut.getListOfChildren(mockFile);
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void getListOfChildren_null_children() throws Exception {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(true);
        Mockito.when(mockFile.isDirectory()).thenReturn(true);
        Mockito.when(mockFile.listFiles()).thenReturn(null);

        final File[] result = cut.getListOfChildren(mockFile);
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void getListOfChildren_no_children() throws Exception {
        final File[] children = new File[0];
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(true);
        Mockito.when(mockFile.isDirectory()).thenReturn(true);
        Mockito.when(mockFile.listFiles()).thenReturn(children);

        final File[] result = cut.getListOfChildren(mockFile);
        Assert.assertSame(children, result);
    }

    @Test
    public void getListOfChildren_valid_children() throws Exception {
        final File[] children = new File[5];
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(true);
        Mockito.when(mockFile.isDirectory()).thenReturn(true);
        Mockito.when(mockFile.listFiles()).thenReturn(children);

        final File[] result = cut.getListOfChildren(mockFile);
        Assert.assertSame(children, result);
    }
}