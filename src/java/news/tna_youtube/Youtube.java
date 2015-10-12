/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package news.tna_youtube;

/**
 *
 * @author XTien
 */
import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

public class Youtube {

    private boolean rc = false;
    private BufferedReader textReader = null;
    String resolutions = "found video URL for resolution: ";

    public static void main(String[] args) throws Exception {
        Youtube yt = new Youtube();
        yt.getLinkYT("https://www.youtube.com/watch?v=fJrbRMxnvd4");
    }

    public boolean getLinkYT(String yturl) {
        try {
            URL oracle = new URL(yturl);
            textReader = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));
            String line;
            String line0 = "";
            String line1 = "";
            while ((line = textReader.readLine()) != null) {
                if (line.matches(".*(\"adaptive_fmts\":|\"url_encoded_fmt_stream_map\":).*")) { // behind that two strings are the comma separated video URLs we use 
                    rc = true;
                    HashMap<String, String> sourceCodeVideoUrls = new HashMap<String, String>();

                    line = line.replaceAll(" ", "");
                    line = line.replace("%25", "%");
                    line = line.replace("\\u0026", "&");

                    if (line.contains("\"url_encoded_fmt_stream_map\":\"")) {
                        line1 = StringUtils.substringBetween(line, "\"url_encoded_fmt_stream_map\":\"", "\"");
                    }

                    line0 = "";

                    if (line.contains("\"adaptive_fmts\":\"")) {
                        line0 = StringUtils.substringBetween(line, "\"adaptive_fmts\":\"", "\"");
                    }

                    if (line0 == null) {
                        line0 = "";
                    }
                    if (line1 == null) {
                        line1 = "";
                    }

                    line = line0 + "," + line1;
                    System.err.println(String.format("length sline0 sline1: %d %d", line0.length(), line1.length()));
                    System.err.println(String.format("sline0 (adaptive fmt) %s \n sline1 (url_encoded fmt): %s", line0, line1));
                    String[] sourceCodeYoutubeUrls = line.split(",");
                    System.err.println("ssourcecodeuturls.length: ".concat(Integer.toString(sourceCodeYoutubeUrls.length)));
                    for (String url : sourceCodeYoutubeUrls) {
                        // assuming rtmpe is used for all resolutions, if found once - end download
                        if (url.matches(".*conn=rtmpe.*")) {
                            System.err.println("RTMPE found. cannot download this one!");
                            break;
                        }
                        String[] fmtUrlPair = url.split("url=http(s)?", 2);
                        fmtUrlPair[1] = "url=http" + fmtUrlPair[1] + "&" + fmtUrlPair[0];
                        // grep itag=xz out and use xy as hash key
                        // 2013-02 itag now has up to 3 digits
                        fmtUrlPair[0] = fmtUrlPair[1].substring(fmtUrlPair[1].indexOf("itag=") + 5, fmtUrlPair[1].indexOf("itag=") + 5 + 1 + (fmtUrlPair[1].matches(".*itag=[0-9]{2}.*") ? 1 : 0) + (fmtUrlPair[1].matches(".*itag=[0-9]{3}.*") ? 1 : 0));

                        if (yturl.startsWith("https")) {
                            fmtUrlPair[1] = fmtUrlPair[1].replaceFirst("url=http%3A%2F%2F", "https://"); // webpage source code only provides http urls if accessed via wget or ytd2 - the browser does something unknown so google sends back httpS urls within source code!
                        } else {
                            fmtUrlPair[1] = fmtUrlPair[1].replaceFirst("url=http%3A%2F%2F", "http://");
                        }

                        fmtUrlPair[1] = fmtUrlPair[1].replaceAll("%3F", "?").replaceAll("%2F", "/").replaceAll("%3B", ";")/*.replaceAll("%2C",",")*/.replaceAll("%3D", "=").replaceAll("%26", "&").replaceAll("%252C", "%2C").replaceAll("sig=", "signature=").replaceAll("&s=", "&signature=").replaceAll("\\?s=", "?signature=");

                        // remove duplicate parts between &
                        String sortedUrl = this.sortStringAt(fmtUrlPair[1], "&");
                        System.err.println(String.format("video tag: %s url: %s", fmtUrlPair[0], sortedUrl));  // fmtUrlPair[1] -> ssortedURL
                        fmtUrlPair[1] = sortedUrl;

                        try {
                            sourceCodeVideoUrls.put(fmtUrlPair[0], fmtUrlPair[1]); // save that URL
                            //debugoutput(String.format( "video url saved with key %s: %s",fmtUrlPair[0],ssourcecodevideourls.get(fmtUrlPair[0]) ));
                            resolutions = resolutions.concat(
                fmtUrlPair[0].equals( "138" )?"2304p mpeg, ":            // 4k HD   type=video/mp4;+codecs="avc1.640033" & size=4096x2304
                fmtUrlPair[0].equals( "264" )?"1440p mpeg, ":            // <4k HD  type=video/mp4;+codecs="avc1.640032" & size=2560x1440
                fmtUrlPair[0].equals( "266" )?"2160p mpeg, ":            // <4k HD  type=video/mp4;+codecs=%22avc1.640033" & size=3840x2160&
                fmtUrlPair[0].equals( "271" )?"1440p webm, ":            // <4k HD  type=video/webm;+codecs=%22vp9" & size=2560x1440
                		
                fmtUrlPair[0].equals( "272" )?"2160p webm, ":            // <4k HD  type=video/webm;+codecs="vp9" &	size=3840x2160                
                fmtUrlPair[0].equals( "248" )?"1080p mpeg, ":            // HD      type=video/webm;+codecs="vp9" & size=1920x1080
                fmtUrlPair[0].equals(  "37" )?"1080p mpeg, ":            // HD      type=video/mp4;+codecs="avc1.64001F,+mp4a.40.2"
                fmtUrlPair[0].equals(  "22" )?"720p mpeg, ":             // HD      type=video/mp4;+codecs="avc1.64001F,+mp4a.40.2"
                fmtUrlPair[0].equals( "247" )?"1080p mpeg, ":            // HD      type=video/webm;+codecs="vp9" & size=1280x720
                fmtUrlPair[0].equals(  "84" )?"1080p 3d mpeg, ":         // HD 3D   type=video/mp4;+codecs="avc1.64001F,+mp4a.40.2"
                fmtUrlPair[0].equals(  "18" )?"360p mpeg, ":             // SD      type=video/mp4;+codecs="avc1.42001E,+mp4a.40.2"
                fmtUrlPair[0].equals(  "35" )?"480p flv, ":              // SD      type=video/x-flv
                fmtUrlPair[0].equals(  "34" )?"360p flv, ":              // SD      type=video/x-flv
                fmtUrlPair[0].equals(  "82" )?"360p 3d mpeg, ":          // SD 3D   type=video/mp4;+codecs="avc1.42001E,+mp4a.40.2"
                fmtUrlPair[0].equals(  "36" )?"240p mpeg 3gpp, ":        // LD      type=video/3gpp;+codecs="mp4v.20.3,+mp4a.40.2"
                fmtUrlPair[0].equals(  "17" )?"114p mpeg 3gpp, ":        // LD      type=video/3gpp;+codecs="mp4v.20.3,+mp4a.40.2"
                	
                fmtUrlPair[0].equals(  "46" )?"1080p webm, ":            // HD      type=video/webm;+codecs="vp8.0,+vorbis"
                fmtUrlPair[0].equals(  "45" )?"720p webm, ":             // HD      type=video/webm;+codecs="vp8.0,+vorbis"
                fmtUrlPair[0].equals( "100" )?"1080p 3d webm, ":         // HD 3D   type=video/webm;+codecs="vp8.0,+vorbis"
                fmtUrlPair[0].equals(  "44" )?"480p webm, ":             // SD      type=video/webm;+codecs="vp8.0,+vorbis"
                fmtUrlPair[0].equals(  "43" )?"360p webm, ":             // SD      type=video/webm;+codecs="vp8.0,+vorbis"
                fmtUrlPair[0].equals( "102" )?"360p 3d webm, ":          // SD 3D   type=video/webm;+codecs="vp8.0,+vorbis"
                fmtUrlPair[0].equals( "244" )?"480p webm, ":             // SD      type=video/webm;+codecs="vp9" & size=854x480
                
                fmtUrlPair[0].equals(  "5"  )?"240p flv, " :             // LD      type=video/x-flv
                	
                fmtUrlPair[0].equals( "137" )?"1080p mpeg, ":            // HD      type=video/mp4;+codecs="avc1.640028" & size=1920x1080
                fmtUrlPair[0].equals( "136" )?"720p mpeg, ":             // HD      type=video/mp4;+codecs="avc1.4d401f" & size=1280x720
                fmtUrlPair[0].equals( "135" )?"480p mpeg, ":             // SD      type=video/mp4;+codecs="avc1.4d401f" & size=854x480
                fmtUrlPair[0].equals( "134" )?"360p mpeg, ":             // SD      type=video/mp4;+codecs="avc1.4d401e" & size=640x360
                fmtUrlPair[0].equals( "133" )?"240p mpeg, ":             // LD      type=video/mp4;+codecs="avc1.4d4015" & size=426x240 
                fmtUrlPair[0].equals( "160" )?"144p mpeg, ":             // LD      type=video/mp4;+codecs="avc1.42c00c" & size=256x144
                fmtUrlPair[0].equals( "243" )?"360p webm, ":             // LD      type=video/webm;+codecs="vp9"
                fmtUrlPair[0].equals( "242" )?"240p webm, ":             // LD      type=video/webm;+codecs="vp9"
                			
                fmtUrlPair[0].equals( "140" )?"mpeg audio only, ":       // ??      type=audio/mp4;+codecs="mp4a.40.2 & bitrate=127949
                fmtUrlPair[0].equals( "171" )?"ogg vorbis audio only, ": // ??      audio/webm;+codecs="vorbis" & bitrate=127949
                		
                "unknown resolution! (".concat(fmtUrlPair[0]).concat(") "));
                        } catch (java.lang.ArrayIndexOutOfBoundsException aioobe) {
                            //TODO catch must not be empty
                        }
                    }
                }
            }
            textReader.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(Youtube.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Youtube.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private String sortStringAt(String source, String delimiter) {

        String sortedUrl = source.replaceFirst("\\?.*", ""); // http://...

        // FIXME range=0-999999999 lead to a maximum fraction of 1GB (not GiB !), but if the file is greater than 1GB we would have to download the whole video in more smaller parts (like the ytplayer does)
        // http://www.youtube.com/watch?v=x9WxkcUTGSY  4:14:33
        String[] unsortedUrl = source.replaceFirst("http.*\\?", "").concat("&range=0-999999999").split(delimiter);

        Arrays.sort(unsortedUrl);

        for (int i = 0; i < unsortedUrl.length - 1; i++) {
            if (unsortedUrl[i].equals(unsortedUrl[i + 1])) {
                unsortedUrl[i] = "";
            }
        }

        sortedUrl += Arrays.toString(unsortedUrl);
        sortedUrl = sortedUrl.replaceAll("\\[, ", delimiter).replaceAll(", ", delimiter).replaceAll(",,", delimiter).replaceAll("]", "").replaceAll(delimiter + delimiter, delimiter);
        sortedUrl = sortedUrl.replaceFirst("/videoplayback\\[", "/videoplayback?").replaceFirst("/videoplayback&", "/videoplayback?");
        return sortedUrl;

    }
}


