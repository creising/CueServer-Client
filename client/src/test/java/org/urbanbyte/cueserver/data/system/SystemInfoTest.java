package org.urbanbyte.cueserver.data.system;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests the {@code SystemInfo} class.
 * <p>
 * @author Chris Reising
 */
public class SystemInfoTest
{
    /** The test name of the CueServer. */
    private final String deviceName = "deviceName";

    /** The test model of the CueServer. */
    private final Model model = Model.CS_800;

    /** The test serial number. */
    private final String serialNumber = "serialNumber";

    /** The test version of firmware running on the CueServer. */
    private final String firmwareVersion = "firmware";

    /** The test time of the CueServer. */
    private final String time = "time";

    /** Test password state. */
    private final boolean hasPassword = true;

    @Test
    public void testConstruction()
    {
        SystemInfo info = new SystemInfo.Builder().
                setDeviceName(deviceName).
                setModel(model).
                setSerialNumber(serialNumber).
                setFirmwareVersion(firmwareVersion).
                setTime(time).
                setHasPassword(hasPassword).build();

        assertThat(info.getDeviceName(), is(deviceName));
        assertThat(info.getModel(), is(model));
        assertThat(info.getSerialNumber(), is(serialNumber));
        assertThat(info.getFirmwareVersion(), is(firmwareVersion));
        assertThat(info.getTime(), is(time));
        assertThat(info.hasPassword(), is(hasPassword));
        assertThat(info.toString(), containsString(deviceName));
    }

    /**
     * {@code null} name will cause an exception.
     */
    @Test(expected = NullPointerException.class)
    public void testNullDevice()
    {
        new SystemInfo.Builder().
                setModel(model).
                setSerialNumber(serialNumber).
                setFirmwareVersion(firmwareVersion).
                setTime(time).
                setHasPassword(hasPassword).build();

    }

    /**
     * {@code null} model will cause an exception.
     */
    @Test(expected = NullPointerException.class)
    public void testNullModel()
    {
        new SystemInfo.Builder().
                setDeviceName(deviceName).
                setSerialNumber(serialNumber).
                setFirmwareVersion(firmwareVersion).
                setTime(time).
                setHasPassword(hasPassword).build();

    }

    /**
     * {@code null} serial number will cause an exception.
     */
    @Test(expected = NullPointerException.class)
    public void testSerialNumber()
    {
        new SystemInfo.Builder().
                setDeviceName(deviceName).
                setModel(model).
                setFirmwareVersion(firmwareVersion).
                setTime(time).
                setHasPassword(hasPassword).build();

    }

    /**
     * {@code null} firmware will cause an exception.
     */
    @Test(expected = NullPointerException.class)
    public void testNullFirmware()
    {
        new SystemInfo.Builder().
                setDeviceName(deviceName).
                setModel(model).
                setSerialNumber(serialNumber).
                setTime(time).
                setHasPassword(hasPassword).build();

    }

    /**
     * {@code null} time will cause an exception.
     */
    @Test(expected = NullPointerException.class)
    public void testNullTime()
    {
        new SystemInfo.Builder().
                setDeviceName(deviceName).
                setModel(model).
                setSerialNumber(serialNumber).
                setFirmwareVersion(firmwareVersion).
                setHasPassword(hasPassword).build();

    }

    /**
     * {@code null} password state will cause an exception.
     */
    @Test(expected = NullPointerException.class)
    public void testNullPassword()
    {
        new SystemInfo.Builder().
                setDeviceName(deviceName).
                setModel(model).
                setSerialNumber(serialNumber).
                setFirmwareVersion(firmwareVersion).
                setTime(time).build();

    }
}
