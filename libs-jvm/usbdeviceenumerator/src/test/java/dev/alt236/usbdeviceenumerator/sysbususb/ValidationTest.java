package dev.alt236.usbdeviceenumerator.sysbususb;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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
    public void isValidUsbDeviceCandidate_not_existent() {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(false);

        assertFalse(cut.isValidUsbDeviceCandidate(mockFile));
    }

    @Test
    public void isValidUsbDeviceCandidate_not_directory() {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(true);
        Mockito.when(mockFile.isDirectory()).thenReturn(false);

        assertFalse(cut.isValidUsbDeviceCandidate(mockFile));
    }

    @Test
    public void isValidUsbDeviceCandidate_is_current_dir() {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(true);
        Mockito.when(mockFile.isDirectory()).thenReturn(true);
        Mockito.when(mockFile.getName()).thenReturn(".");

        assertFalse(cut.isValidUsbDeviceCandidate(mockFile));
    }

    @Test
    public void isValidUsbDeviceCandidate_is_parent_dir() {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(true);
        Mockito.when(mockFile.isDirectory()).thenReturn(true);
        Mockito.when(mockFile.getName()).thenReturn("..");

        assertFalse(cut.isValidUsbDeviceCandidate(mockFile));
    }

    @Test
    public void isValidUsbDeviceCandidate_is_valid() {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(true);
        Mockito.when(mockFile.isDirectory()).thenReturn(true);
        Mockito.when(mockFile.getName()).thenReturn("foo");

        assertTrue(cut.isValidUsbDeviceCandidate(mockFile));
    }

    @Test(expected = NullPointerException.class)
    public void isValidUsbDeviceCandidate_is_null() {
        final File mockFile = null;
        cut.isValidUsbDeviceCandidate(mockFile);
    }

    @Test(expected = NullPointerException.class)
    public void getListOfChildren_is_null() {
        final File mockFile = null;
        cut.getListOfChildren(mockFile);
    }

    @Test
    public void getListOfChildren_not_existent() {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(false);

        final File[] result = cut.getListOfChildren(mockFile);
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void getListOfChildren_not_directory() {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(true);
        Mockito.when(mockFile.isDirectory()).thenReturn(false);

        final File[] result = cut.getListOfChildren(mockFile);
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void getListOfChildren_null_children() {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(true);
        Mockito.when(mockFile.isDirectory()).thenReturn(true);
        Mockito.when(mockFile.listFiles()).thenReturn(null);

        final File[] result = cut.getListOfChildren(mockFile);
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void getListOfChildren_no_children() {
        final File[] children = new File[0];
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(true);
        Mockito.when(mockFile.isDirectory()).thenReturn(true);
        Mockito.when(mockFile.listFiles()).thenReturn(children);

        final File[] result = cut.getListOfChildren(mockFile);
        Assert.assertSame(children, result);
    }

    @Test
    public void getListOfChildren_valid_children() {
        final File[] children = new File[5];
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.exists()).thenReturn(true);
        Mockito.when(mockFile.isDirectory()).thenReturn(true);
        Mockito.when(mockFile.listFiles()).thenReturn(children);

        final File[] result = cut.getListOfChildren(mockFile);
        Assert.assertSame(children, result);
    }
}