//standard imports
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.MalformedURLException;
import java.io.IOException;

public class ServerScraper {
    // System constants
    final static String DEVICE; //INSERT DEVICE HERE
    final static String TOKEN = //INSERT TOKEN HERE
    final static String SERVER_NAME; //INSERT SERVER NAME HERE
    final static long SERVER_ID; //INSERT SERVER ID HERE
    public static void main(String[] args) throws MalformedURLException, IOException{
        //sets up file to run
        File singleDir = new File(SERVER_NAME + "/");
        if (singleDir.mkdir()) {
            System.out.println("Single directory created:" + SERVER_NAME + "/");
        } else {
            System.out.println("Failed to create single directory or it already exists: " + SERVER_NAME + "/");
        }
        //sets up URL for being scraped
        String server = "https://discord.com/api/v9/guilds/" + SERVER_ID + "/channels";
        URL grabMessages = new URL(server);
        HttpURLConnection con = (HttpURLConnection) grabMessages.openConnection();
        con.setRequestProperty("authorization", TOKEN);
        con.setRequestProperty("user-agent", DEVICE);
        con.setRequestProperty("accept", "*/*");
        con.setRequestMethod("GET");
        //collects channelNames
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        String c = response.substring(1,response.length() - 1);
        System.out.println(c);
        //stores channel Names
        ArrayList<String> channels = splitChannel(c);
        HashMap<Long, String> channelData = new HashMap<Long, String>(500);
        for(String channel: channels){
            try {
                channelData.put(Long.parseLong(channel.substring(channel.indexOf("\"id\"") + 6, channel.indexOf("\"type\"") - 2)),
                        (channel.substring(channel.indexOf("\"name\"") + 8, channel.indexOf("\"parent_id\"") - 2)));
            }
            catch(Exception e){
                System.out.println("Huh, it seems like " + channel + " failed");
            }
        }
        //validates every channel as accessible
        HashMap<Long,String> accessibleChannels = new HashMap<Long,String>(500);
        for(Long l: channelData.keySet()){
            System.out.println(channelData.get(l));
            server = "https://discord.com/api/v9/channels/" + l + "/messages?";
            grabMessages = new URL(server);
            con = (HttpURLConnection) grabMessages.openConnection();
            con.setRequestProperty("authorization", TOKEN);
            con.setRequestProperty("user-agent", DEVICE);
            con.setRequestProperty("accept", "*/*");
            con.setRequestMethod("GET");
            if(con.getResponseCode() != 200){
                System.out.println("Seems like " + channelData.get(l) + " is not an accesible channel");
            } else {
                accessibleChannels.put(l,channelData.get(l));
            }
        }
        //prints out final batch of channels and runs
        for(Long l: accessibleChannels.keySet()){
            System.out.println("ID: " + l + "\nChannel name: " + channelData.get(l));
            ChannelScraper channelScraper = new ChannelScraper(DEVICE,TOKEN,l,channelData.get(l),SERVER_NAME);
            channelScraper.run();
        }

    }

    /**
     * Splits a String containing several discord channel into an array of Channel objects
     * <br> pre: messages != null
     * <br> post: An ArrayList of Channel jsons
     * <br> param: channel: A string containing several discord channel jsons
     */
    private static ArrayList<String> splitChannel(String channels){
        ArrayList<String> splitChannels = new ArrayList<String>();
        int startIndex = 0;
        int endIndex = channels.indexOf("},{\"id\":");
        int numMessagesFound = 0;
        while (endIndex != -1){
            String channel = channels.substring(startIndex,endIndex+1);
            startIndex = endIndex + 2;
            endIndex = channels.indexOf("},{\"id\":",startIndex+10);
            numMessagesFound++;
            splitChannels.add(channel);
        }
        return splitChannels;
    }
}
