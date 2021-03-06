package org.urbanbyte.cueserver.http;

import org.urbanbyte.cueserver.data.cue.Cue;
import org.urbanbyte.cueserver.data.playback.CombineMode;
import org.urbanbyte.cueserver.data.playback.DetailedPlaybackStatus;
import org.urbanbyte.cueserver.data.playback.Playback;
import org.urbanbyte.cueserver.data.system.Model;
import org.urbanbyte.cueserver.data.playback.PlaybackInfo;
import org.urbanbyte.cueserver.data.playback.PlaybackStatus;
import org.urbanbyte.cueserver.data.system.SystemInfo;
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

    /** Command URL. */
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

        cueServerClient.playCue(2);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(), is(cmdUrl + "P+"+ "1" + "+Q+" + 2.0 +
                "+GO"));
    }

    /**
     * Test playing a cue.
     */
    @Test
    public void playCuePlayback2()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        cueServerClient.playCue(2.1, Playback.PLAYBACK_2);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(), is(cmdUrl + "P+"+ "2" + "+Q+" +
                2.1 + "+GO"));
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
        assertThat(urlCaptor.getValue(), is(cmdUrl + "P+"+ "2" + "+CL"));
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
     * Test setting channel and level.
     */
    @Test
    public void setChannelLevel()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        cueServerClient.setChannel(1, 255);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(),
                is(cmdUrl + "T+0.0+P1+C+1+A+%23255"));
    }

    /**
     * Test setting channel, level, and time.
     */
    @Test
    public void setChannelLevelTime()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        cueServerClient.setChannel(1, 255, 2.11);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(),
                is(cmdUrl + "T+2.1+P1+C+1+A+%23255"));
    }

    /**
     * Test setting channel, level, time and playback.
     */
    @Test
    public void setChannelLevelTimePlayback()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        cueServerClient.setChannel(1, 255, 2.1, Playback.PLAYBACK_3);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(),
                is(cmdUrl + "T+2.1+P3+C+1+A+%23255"));
    }

    /**
     * Test setting channel, level, time and playback valid min values.
     */
    @Test
    public void setChannelLevelValidMinValues()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        cueServerClient.setChannel(1, 0, 0, Playback.PLAYBACK_3);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(),
                is(cmdUrl + "T+0.0+P3+C+1+A+%230"));
    }

    /**
     * Test setting channel, level, time and playback valid max values.
     */
    @Test
    public void setChannelLevelValidMaxValues()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        cueServerClient.setChannel(512, 255, 1, Playback.PLAYBACK_3);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(),
                is(cmdUrl + "T+1.0+P3+C+512+A+%23255"));
    }

    /**
     * Invalid min channel value will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setChannelMinValueInvalid()
    {
        cueServerClient.setChannel(0, 255);
    }

    /**
     * Invalid max channel value will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setChannelMaxValueInvalid()
    {
        cueServerClient.setChannel(513, 255);
    }

    /**
     * Invalid min level value will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setChannelLevelMinValueInvalid()
    {
        cueServerClient.setChannel(1, -1);
    }

    /**
     * Invalid max level value will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setChannelLevelMaxValueInvalid()
    {
        cueServerClient.setChannel(1, 256);
    }

    /**
     * Invalid min time value will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setChannelTimeMinValueInvalid()
    {
        cueServerClient.setChannel(1, 255, -1);
    }

    /**
     * Invalid max time value will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setChannelTimeMazValueInvalid()
    {
        cueServerClient.setChannel(1, 255, 650001);
    }

    /**
     * {@code null} playback will cause an exception.
     */
    @Test(expected = NullPointerException.class)
    public void setChannelNullPlayback()
    {
        cueServerClient.setChannel(1, 255, 0, null);
    }

    /**
     * Test controlling a range of channels.
     */
    @Test
    public void setChannelRange()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        cueServerClient.setChannelRange(1, 10, 255);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(), is(
                cmdUrl + "T+0.0+P1+C+1%3E10+A%23255"));
    }

    /**
     * Test controlling a range of channels with a time.
     */
    @Test
    public void setChannelRangeTime()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        cueServerClient.setChannelRange(1, 10, 255, 20.4);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(), is(
                cmdUrl + "T+20.4+P1+C+1%3E10+A%23255"));
    }

    /**
     * Test controlling a range of channels with a time and playback.
     */
    @Test
    public void setChannelRangeTimePlayback()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        cueServerClient.setChannelRange(1, 10, 0, 20, Playback.PLAYBACK_2);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(), is(
                cmdUrl + "T+20.0+P2+C+1%3E10+A%230"));
    }

    /**
     * Test with a start channel that is too low.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setChannelRangeInvalidMinStartChan()
    {
        cueServerClient.setChannelRange(0, 10, 255);
    }

    /**
     * Test with a start channel that is too high.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setChannelRangeInvalidMaxStartChan()
    {
        cueServerClient.setChannelRange(513, 10, 255);
    }

    /**
     * Test a end channel that is greater than the start channel.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setChannelRangeInvalidRange()
    {
        cueServerClient.setChannelRange(10, 9, 255);
    }

    /**
     * Test invalid min channel level.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setChannelRangeInvalidMinLevel()
    {
        cueServerClient.setChannelRange(9, 10, -1);
    }

    /**
     * Test a end channel that is greater than the start channel.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setChannelRangeInvalidMaxLevel()
    {
        cueServerClient.setChannelRange(10, 9, 256);
    }

    /**
     * Test with an end channel that is too high.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setChannelRangeInvalidMaxEndChan()
    {
        cueServerClient.setChannelRange(1, 513, 255);
    }

    /**
     * Test with an end channel that is too low.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setChannelRangeInvalidMinEndChan()
    {
        cueServerClient.setChannelRange(1, 0, 255);
    }

    /**
     * Test with an invalid time.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setChannelRangeInvalidMinTime()
    {
        cueServerClient.setChannelRange(1, 9, 255, -1);
    }

    /**
     * Test with an invalid time.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setChannelRangeInvalidMaxTime()
    {
        cueServerClient.setChannelRange(1, 9, 255, 65001);
    }

    /**
     * Test with a {@code null} playback.
     */
    @Test(expected = NullPointerException.class)
    public void setChannelRangeNullPlayback()
    {
        cueServerClient.setChannelRange(1, 9, 255, 0, null);
    }

    /**
     * Test recording a cue.
     */
    @Test
    public void recordCue()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        cueServerClient.recordCue(1, 2, 3);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(), is(
                cmdUrl + "FA+2.0%2F3.0%3BRQ+1.0"));
    }

    /**
     * An invalid cue number will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void recordCueInvalidCueNumber()
    {
        cueServerClient.recordCue(0, 1, 2);
    }

    /**
     * An invalid uptime will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void recordCueUptimeTooLow()
    {
        cueServerClient.recordCue(1, -1, 2);
    }

    /**
     * An invalid uptime will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void recordCueUptimeTooHigh()
    {
        cueServerClient.recordCue(1, 65001, 2);
    }

    /**
     * An invalid downtime will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void recordCueDowntimeTooLow()
    {
        cueServerClient.recordCue(1, 1, -1);
    }

    /**
     * An invalid downtime will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void recordCueDowntimeTooHigh()
    {
        cueServerClient.recordCue(1, 1, 65001);
    }

    /**
     * Test deleting a cue.
     */
    @Test
    public void deleteCue()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        cueServerClient.deleteCue(1);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(), is(cmdUrl +"DELQ+1.0"));
    }

    /**
     * An invalid cue number will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void deleteCueInvalidCueNumber()
    {
        cueServerClient.deleteCue(0);
    }

    /**
     * Test update a cue.
     */
    @Test
    public void updateCue()
    {
        ArgumentCaptor<String> urlCaptor =
                ArgumentCaptor.forClass(String.class);

        cueServerClient.updateCue(1);

        verify(mockedHttpClient).submitHttpGetRequest(urlCaptor.capture());
        assertThat(urlCaptor.getValue(), is(cmdUrl + "UQ+1.0"));
    }

    /**
     * An invalid cue number will cause an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void updateCueInvalidCueNumber()
    {
        cueServerClient.updateCue(0);
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
