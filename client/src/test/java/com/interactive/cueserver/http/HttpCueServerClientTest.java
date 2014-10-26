package com.interactive.cueserver.http;

import com.interactive.cueserver.data.Model;
import com.interactive.cueserver.data.PlaybackInfo;
import com.interactive.cueserver.data.PlaybackStatus;
import com.interactive.cueserver.data.SystemInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@code HttpCueServerClient} class.
 *
 * author: Chris Reising
 */
public class HttpCueServerClientTest
{
    /** URL for tests. */
    private final String testUrl = "http://localhost.invalid.com";

    /** Mocked HTTP client. */
    private SimpleHttpClient mockedHttpClient;

    /** CueServer client being tested. */
    private HttpCueServerClient cueServerClient;

    /**
     * Setup for tests.
     */
    @Before
    public void setupTest()
    {
        mockedHttpClient = mock(SimpleHttpClient.class);

        cueServerClient = new HttpCueServerClient(
                testUrl, 80, mockedHttpClient);
    }

    /**
     * Test minimal constructor.
     */
    @Test
    public void minimalConstructorNoException()
    {
        HttpCueServerClient client =
                new HttpCueServerClient(testUrl);
        assertThat(client.getUrl(), is(testUrl + ":80"));
    }

    /**
     * Passing {@code null} into the minimal constructor will cause an
     * exception.
     */
    @Test(expected = NullPointerException.class)
    public void minimalConstructorNullException()
    {
        new HttpCueServerClient(null);
    }

    /**
     * Test the valid minimal port number.
     */
    @Test
    public void constructorMinPort()
    {
        HttpCueServerClient client =
                new HttpCueServerClient(testUrl, 0);
        assertThat(client.getUrl(), is(testUrl + ":0"));
    }

    /**
     * Test the valid max port number.
     */
    @Test
    public void constructorMaxPort()
    {
        HttpCueServerClient client =
                new HttpCueServerClient(testUrl, 65535);
        assertThat(client.getUrl(), is(testUrl + ":65535"));
    }

    /**
     * Test the invalid minimal port number.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorMinInvalidPort()
    {
        new HttpCueServerClient(testUrl, -1);
    }

    /**
     * Test the invalid max port number.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorMaxInvalidPort()
    {
        new HttpCueServerClient(testUrl, 65536);
    }

    /**
     * A {@code null} URL will cause an exception.
     */
    @Test(expected = NullPointerException.class)
    public void constructorNullUrl()
    {
        new HttpCueServerClient(null, 0);
    }

    /**
     * Test max valid port number.
     */
    @Test
    public void maxConstructorMaxPort()
    {
        HttpCueServerClient client =
                new HttpCueServerClient(testUrl, 65535,
                        mockedHttpClient);
        assertThat(client.getUrl(), is(testUrl + ":65535"));
    }

    /**
     * Test the invalid minimal port number.
     */
    @Test(expected = IllegalArgumentException.class)
    public void MaxConstructorMinInvalidPort()
    {
        new HttpCueServerClient(testUrl, -1, mockedHttpClient);
    }

    /**
     * Test the valid max port number.
     */
    @Test(expected = IllegalArgumentException.class)
    public void maxConstructorMaxInvalidPort()
    {
        new HttpCueServerClient(testUrl, 65536, mockedHttpClient);
    }

    /**
     * A {@code null} URL will cause an exception.
     */
    @Test(expected = NullPointerException.class)
    public void maxConstructorNullUrl()
    {
        new HttpCueServerClient(null, 0, mockedHttpClient);
    }

    /**
     * A {@code null} HTTP client will cause an exception.
     */
    @Test(expected = NullPointerException.class)
    public void maxConstructorNullClient()
    {
        new HttpCueServerClient(testUrl, 0, null);
    }

    /**
     * Test a valid request and response.
     */
    @Test
    public void validRequest()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        Integer[] array = new Integer[78];
        String name = "name";
        String serialNum = "AQW123";
        String firmware = "firmware";
        String time = "time from box";
        fillArray(array, 0, serialNum);
        // add a null value
        array[16] = 0;
        fillArray(array, 17, name);
        fillArray(array, 41,firmware);
        fillArray(array, 53, time);

        array[76] = 1; // set model to CS-800
        array[77] = 1; // set password to true

        when(mockedHttpClient.submitHttpGetRequest(
                anyString())).thenReturn(array);

        SystemInfo info = cueServerClient.getSystemInfo();

        assertThat(info.getSerialNumber(), is(serialNum));
        assertThat(info.getDeviceName(), is(name));
        assertThat(info.getFirmwareVersion(), is(firmware));
        assertThat(info.getTime(), is(time));
        assertThat(info.getModel(), is(Model.CS_800));
        assertThat(info.hasPassword(), is(true));

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(), is(testUrl + ":80/get.cgi/?req=SI"));
    }

    /**
     * Test a valid request and response.
     */
    @Test
    public void validRequestNoPassword()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        Integer[] array = new Integer[78];
        String name = "name";
        String serialNum = "AQW123";
        String firmware = "firmware";
        String time = "time from box";
        fillArray(array, 0, serialNum);
        // add a null value
        array[16] = 0;
        fillArray(array, 17, name);
        fillArray(array, 41,firmware);
        fillArray(array, 53, time);

        array[76] = 1; // set model to CS-800
        array[77] = 0; // set password to true

        when(mockedHttpClient.submitHttpGetRequest(
                anyString())).thenReturn(array);

        SystemInfo info = cueServerClient.getSystemInfo();

        assertThat(info.getSerialNumber(), is(serialNum));
        assertThat(info.getDeviceName(), is(name));
        assertThat(info.getFirmwareVersion(), is(firmware));
        assertThat(info.getTime(), is(time));
        assertThat(info.getModel(), is(Model.CS_800));
        assertThat(info.hasPassword(), is(false));

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(), is(testUrl + ":80/get.cgi/?req=SI"));
    }

    /**
     * Test getting the playback status.
     */
    @Test
    public void getStatusSuccess()
    {
        Integer[] values = new Integer[48];
        Arrays.fill(values, 0);
        values[0] = 10;
        values[2] = 11;
        values[12] = 12;
        values[14] = 13;
        values[26] = 15;
        values[36] = 16;

        when(mockedHttpClient.submitHttpGetRequest(testUrl +
                ":80/get.cgi/?req=PS")).thenReturn(values);

        PlaybackStatus status = cueServerClient.getPlaybackStatus();

        assertPlatbackInfo(status.getPlayback1(), 1, 1.0, 1.1);
        assertPlatbackInfo(status.getPlayback2(), 2, 1.2, 1.3);
        assertPlatbackInfo(status.getPlayback3(), 3, null, 1.5);
        assertPlatbackInfo(status.getPlayback4(), 4, 1.6, null);
    }

    /**
     * An invalid array length will result in {@code null} being returned.
     */
    @Test
    public void getStatusInvalidArrayLength()
    {
        Integer[] values = new Integer[2];

        when(mockedHttpClient.submitHttpGetRequest(
                anyString())).thenReturn(values);

        assertThat(cueServerClient.getPlaybackStatus(), nullValue());
    }

    /**
     * The max cue number valid will result in {@code null}.
     */
    @Test
    public void parseCueNumberMax()
    {
        assertThat(cueServerClient.parseCueNumber(65535), nullValue());
    }

    /**
     * The min cue number valid will result in {@code null}.
     */
    @Test
    public void parseCueNumberMin()
    {
        assertThat(cueServerClient.parseCueNumber(0), nullValue());
    }

    /**
     * If the array returned by the service is not the expected size
     * {@code null} will be returned.
     */
    @Test
    public void systemArrayTooSmall()
    {

        Integer[] array = new Integer[77];

        when(mockedHttpClient.submitHttpGetRequest(
                anyString())).thenReturn(array);

        SystemInfo info = cueServerClient.getSystemInfo();

        assertThat(info, nullValue());
    }

    /**
     * Test converting a CS-800.
     */
    @Test
    public void convertCs800()
    {
        assertThat(cueServerClient.convertModel(1), is(Model.CS_800));
    }

    /**
     * Test converting a CS-810.
     */
    @Test
    public void convertCs810()
    {
        assertThat(cueServerClient.convertModel(2), is(Model.CS_810));
    }

    /**
     * Test converting a CS-816.
     */
    @Test
    public void convertCs816()
    {
        assertThat(cueServerClient.convertModel(3), is(Model.CS_816));
    }

    /**
     * Test converting a CS-840.
     */
    @Test
    public void convertCs840()
    {
        assertThat(cueServerClient.convertModel(4), is(Model.CS_840));
    }

    /**
     * Test converting an unknown model.
     */
    @Test
    public void convertUnknown()
    {
        assertThat(cueServerClient.convertModel(0), is(Model.UNKNOWN));
    }

    /**
     * A start index that is greater than the end index will cause an exception.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void invertedIndex()
    {
        HttpCueServerClient.checkIndex(new Integer[10], 2, 1);
    }

    /**
     * A start index that is less than 0 will cause an exception.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void startTooLow()
    {
        HttpCueServerClient.checkIndex(new Integer[10], -1, 1);
    }

    /**
     * A start index that is greater than the array length will cause an
     * exception.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void startTooHigh()
    {
        HttpCueServerClient.checkIndex(new Integer[10], 10, 12);
    }

    /**
     * An end index that is greater than the array length will cause an
     * exception.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void endTooHigh()
    {
        HttpCueServerClient.checkIndex(new Integer[10], 9, 10);
    }

    /**
     * Helper method to assert {@link PlaybackInfo}.
     *
     * @param info the object to asseert.
     * @param number the expected playback number.
     * @param cc the expected current cue.
     * @param nc the expected next cue.
     */
    private void assertPlatbackInfo(PlaybackInfo info,
                                    int number,
                                    Double cc,
                                    Double nc)
    {
        assertThat(info.getPlaybackNumber(), is(number));
        assertThat(info.getCurrentCue(), is(cc));
        assertThat(info.getNextCue(), is(nc));
    }

    /**
     * Help method to fill an array for tests.
     *
     * @param array the array to fill.
     * @param start the index it should start filling from.
     * @param value the value to be put in the array.
     */
    private void fillArray(Integer[] array, int start, String value)
    {
        int index = start;
        for(char c : value.toCharArray())
        {
            array[index] = (int)c;
            index++;
        }
    }
}
