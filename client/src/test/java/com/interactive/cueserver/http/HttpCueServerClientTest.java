package com.interactive.cueserver.http;

import com.interactive.cueserver.data.cue.Cue;
import com.interactive.cueserver.data.playback.CombineMode;
import com.interactive.cueserver.data.playback.DetailedPlaybackStatus;
import com.interactive.cueserver.data.playback.Playback;
import com.interactive.cueserver.data.system.Model;
import com.interactive.cueserver.data.playback.PlaybackInfo;
import com.interactive.cueserver.data.playback.PlaybackStatus;
import com.interactive.cueserver.data.system.SystemInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@code HttpCueServerClient} class.
 * <p>
 * author: Chris Reising
 */
public class HttpCueServerClientTest
{
    /** URL for tests. */
    private final String testUrl = "http://localhost.invalid.com";

    /** Comand URL. */
    private final String cmdUrl = testUrl + ":80/exe.cgi/?cmd=";

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

    @Test
    public void testByteToString()
    {
        Integer[] array = new Integer[2];
        array[0] = 65;
        array[1] = 66;
        HttpCueServerClient.ParseStruct<String> struct =
                HttpCueServerClient.bytesToString(array, 0, 2);

        assertThat(struct.value, is("AB"));
    }

    /**
     * Test a valid request and response.
     */
    @Test
    public void validSystemInfoRequest()
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
    public void validSystemInfoRequestNoPassword()
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
     * A {@code null} response will result in {@code null} being returned.
     */
    @Test
    public void statusWrongSizeArray()
    {
        when(mockedHttpClient.submitHttpGetRequest(
                anyString())).thenReturn(new Integer[1]);

        assertThat(cueServerClient.getSystemInfo(), nullValue());
    }

    /**
     * A {@code null} response will result in {@code null} being returned.
     */
    @Test
    public void statusNullResponse()
    {
        when(mockedHttpClient.submitHttpGetRequest(
                anyString())).thenReturn(null);

        assertThat(cueServerClient.getSystemInfo(), nullValue());
    }

    /**
     * Test getting the playback status.
     */
    @Test
    public void getStatusSuccess()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        Integer[] values = new Integer[48];
        Arrays.fill(values, 0);
        values[0] = 10;
        values[2] = 11;
        values[12] = 12;
        values[14] = 13;
        values[26] = 15;
        values[36] = 16;

        when(mockedHttpClient.submitHttpGetRequest(
                anyString())).thenReturn(values);

        PlaybackStatus status = cueServerClient.getPlaybackStatus();

        assertPlaybackInfo(status.getPlayback1(),
                Playback.PLAYBACK_1, 1.0, 1.1);
        assertPlaybackInfo(status.getPlayback2(),
                Playback.PLAYBACK_2, 1.2, 1.3);
        assertPlaybackInfo(status.getPlayback3(),
                Playback.PLAYBACK_3, null, 1.5);
        assertPlaybackInfo(status.getPlayback4(),
                Playback.PLAYBACK_4, 1.6, null);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(), is(testUrl + ":80/get.cgi/?req=PS"));
    }

    /**
     * An invalid array length will result in {@code null} being returned.
     */
    @Test
    public void getStatusInvalidArrayLength()
    {
        when(mockedHttpClient.submitHttpGetRequest(
                anyString())).thenReturn(new Integer[2]);

        assertThat(cueServerClient.getPlaybackStatus(), nullValue());
    }

    /**
     * A {@code null} response will result in {@code null} being returned.
     */
    @Test
    public void getStatusNullArray()
    {
        when(mockedHttpClient.submitHttpGetRequest(
                anyString())).thenReturn(null);

        assertThat(cueServerClient.getPlaybackStatus(), nullValue());
    }

    /**
     * Test a valid DMX value response.
     */
    @Test
    public void getOutputValues()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        Integer[] values = new Integer[512];

        when(mockedHttpClient.submitHttpGetRequest(
                anyString())).thenReturn(values);

        Integer[] outputLevels = cueServerClient.getOutputLevels();

        assertSame(outputLevels, values);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(), is(testUrl + ":80/get.cgi/?req=OUT"));
    }

    /**
     * Too few values will return {@code null}.
     */
    @Test
    public void getOutputValuesTooFewChannels()
    {
        when(mockedHttpClient.submitHttpGetRequest(
                anyString())).thenReturn(new Integer[511]);

        Integer[] outputLevels = cueServerClient.getOutputLevels();

        assertThat(outputLevels, nullValue());
    }

    /**
     * A {@code null} response will result in {@code null} being returned.
     */
    @Test
    public void getOutputValuesNullResponse()
    {
        when(mockedHttpClient.submitHttpGetRequest(
                anyString())).thenReturn(null);

        assertThat(cueServerClient.getOutputLevels(), nullValue());
    }

    /**
     * The max cue number valid will result in {@code null}.
     */
    @Test
    public void parseCueNumberMax()
    {
        assertThat(HttpCueServerClient.parseCue(65535), nullValue());
    }

    /**
     * The min cue number valid will result in {@code null}.
     */
    @Test
    public void parseCueNumberMin()
    {
        assertThat(HttpCueServerClient.parseCue(0), nullValue());
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
     * Test converting detailed playback information.
     */
    @Test
    public void getDetailedPlayback()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        String ccName =  "current cue";
        String ncName = "next cue";
        Integer[] array = new Integer[96];
        Arrays.fill(array, 0);
        array[1] = 1;   // time is disabled
        array[2] = 255; // level is full
        array[3] = 1;   // is in override mode
        array[12] = 11; // current cue is 1.1
        array[14] = 12; // next cue is 1.2
        array[22] = 30; // linked cue is 3.0

        // current cue name
        fillArray(array, 32, ccName);
        // next cue name
        fillArray(array, 64, ncName);

        when(mockedHttpClient.submitHttpGetRequest(
                anyString())).thenReturn(array);

        DetailedPlaybackStatus playbackStatus =
                cueServerClient.getDetailedPlaybackInfo(Playback.PLAYBACK_1);

        Cue cc = playbackStatus.getCurrentCue();
        Cue nc = playbackStatus.getNextCue();
        Cue lc = playbackStatus.getLinkedCue();
        assertThat(cc.getNumber(), is(1.1));
        assertThat(cc.getName(), is(ccName));
        assertThat(nc.getNumber(), is(1.2));
        assertThat(nc.getName(), is(ncName));
        assertThat(lc.getNumber(), is(3.0));
        assertThat(lc.getName(), nullValue());

        assertThat(playbackStatus.isTimingDisabled(), is(true));
        assertThat(playbackStatus.getMasterLevel(), is(255));
        assertThat(playbackStatus.getCombineMode(), is(CombineMode.OVERRIDE));

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(),
                is(testUrl + ":80/get.cgi/?req=PI&id=1"));
    }

    /**
     * An array of the wrong size will result in {@code null}
     */
    @Test
    public void getDetailedPlaybackArrayTooSmall()
    {
        Integer[] array = new Integer[95];

        when(mockedHttpClient.submitHttpGetRequest(
                anyString())).thenReturn(array);

        assertThat(cueServerClient.getDetailedPlaybackInfo(Playback.PLAYBACK_1),
                nullValue());
    }

    /**
     * A {@code null} array will result in {@code null}
     */
    @Test
    public void getDetailedPlaybackNullFromClient()
    {
        when(mockedHttpClient.submitHttpGetRequest(
                anyString())).thenReturn(null);

        assertThat(cueServerClient.getDetailedPlaybackInfo(Playback.PLAYBACK_1),
                nullValue());
    }

    /**
     * Passing {@code null} for a playback will result in an exception.
     */
    @Test(expected = NullPointerException.class)
    public void getDetailedPlaybackNullPlayback()
    {
        cueServerClient.getDetailedPlaybackInfo(null);
    }

    /**
     * Test playing a cue.
     */
    @Test
    public void playCue()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        cueServerClient.playCue(1);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(), is(cmdUrl + "p+"+ "1" + "+cue+" + 1.0 +
                "+go"));
    }

    /**
     * Test playing a cue.
     */
    @Test
    public void playCuePlayback2()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        cueServerClient.playCue(1, Playback.PLAYBACK_2);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(), is(cmdUrl + "p+"+ "2" + "+cue+" + 1.0 +
                "+go"));
    }

    /**
     * An invalid cue number will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void invalidCueNumber()
    {
        cueServerClient.playCue(0);
    }

    /**
     * An invalid cue number will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void playCueInvalidCueNumber2()
    {
        cueServerClient.playCue(0, Playback.PLAYBACK_1);
    }

    /**
     * A {@code null} playback will cause an exception.
     */
    @Test(expected = NullPointerException.class)
    public void playCueNullPayback()
    {
        cueServerClient.playCue(1, null);
    }

    /**
     * Test clearing a playback.
     */
    @Test
    public void clearPlayback()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        cueServerClient.clearPlayback(Playback.PLAYBACK_2);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(), is(cmdUrl + "p+"+ "2" + "+clear"));
    }

    /**
     * A {@code null} playback will cause an exception.
     */
    @Test(expected = NullPointerException.class)
    public void clearPlaybackNullInput()
    {
        cueServerClient.clearPlayback(null);
    }

    /**
     * Convert merge mode.
     */
    @Test
    public void convertMergeMode()
    {
        assertThat(HttpCueServerClient.convertCombineMode(0),
                is(CombineMode.MERGE));
    }

    /**
     * Convert override mode.
     */
    @Test
    public void convertOverrideMode()
    {
        assertThat(HttpCueServerClient.convertCombineMode(1),
                is(CombineMode.OVERRIDE));
    }

    /**
     * Convert scale mode.
     */
    @Test
    public void convertScaleMode()
    {
        assertThat(HttpCueServerClient.convertCombineMode(2),
                is(CombineMode.SCALE));
    }

    /**
     * Convert an unknown mode.
     */
    @Test
    public void convertUnknownMode()
    {
        assertThat(HttpCueServerClient.convertCombineMode(3),
                is(CombineMode.UNKNOWN));
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
        HttpCueServerClient.checkIndex(new Integer[10], 9, 11);
    }

    /**
     * A start index out of the bounds of the array will cause an exception.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void parseInBadStartIndex()
    {
        HttpCueServerClient.unsignedIntToInt(new Integer[0], 1);
    }

    /**
     * Helper method to assert {@link PlaybackInfo}.
     *
     * @param info the object to assert.
     * @param number the expected playback number.
     * @param cc the expected current cue.
     * @param nc the expected next cue.
     */
    private void assertPlaybackInfo(PlaybackInfo info,
                                    Playback number,
                                    Double cc,
                                    Double nc)
    {
        Cue playbackCc = info.getCurrentCue();
        Cue playbackNc = info.getNextCue();
        assertThat(info.getPlayback(), is(number));

        if(cc != null)
        {
            assertThat(playbackCc.getNumber(), is(cc));
            assertThat(playbackCc.getName(), nullValue());
        }
        else
        {
            assertThat(playbackCc, nullValue());

        }

        if(nc != null)
        {
            assertThat(playbackNc.getNumber(), is(nc));
            assertThat(playbackNc.getName(), nullValue());
        }
        else
        {
            assertThat(playbackNc, nullValue());

        }
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
