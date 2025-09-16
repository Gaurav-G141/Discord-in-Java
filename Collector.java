//standard Java imports
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Collector {
    //instance variables
    private String user; //device
    private String token; //discord's authentication token
    private long id; //id of the channel being scraped
    //class variables
    final int DESIRED_MESSAGES = 100;
    //constructor
    public Collector(String user, String token, long id){
        this.user = user;
        this.token = token;
        this.id = id;
    }
    /**
     * Scrapes the channel specified starting at the last message given
     * <br>pre: last_message_id >= 0 (Note: 0 is used when there's no last message
     * <br> post: An ArrayList of message objects
     * @param last_message_id The id of the last message scraped
     * (or 0 if at the start of the scraping process)
     * */
    public ArrayList<Message> getMessages(long last_message_id) throws MalformedURLException, IOException {
        //checks pre-condition
        if(last_message_id < 0){
            throw new IllegalArgumentException("Message id appears invalid:" +last_message_id);
        }

        //creates URL link
        String server = "https://discord.com/api/v9/channels/" + id + "/messages?";
        if(last_message_id != 0){
            server += "before=" + last_message_id + "&";
        }
        server += ("limit=" + DESIRED_MESSAGES);
        URL grabMessages = new URL(server);
        System.out.println(server);
        //sets up URL for being scraped
        HttpURLConnection con = (HttpURLConnection) grabMessages.openConnection();
        con.setRequestProperty("authorization", token);
        con.setRequestProperty("user-agent", user);
        con.setRequestProperty("accept", "*/*");
        con.setRequestMethod("GET");
        int error = con.getResponseCode();
        //handles event of a successful
        if (error == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return splitMessage(response.toString());
        }
        //handles failure event
        else{
            long start = System.nanoTime();
            while(System.nanoTime() - start < 1_000_000_000){}
            return getMessages(last_message_id);
        }
    }

    /**
     * Splits a String containing several discord messages into an array of Message objects
     * <br> pre: messages != null
     * <br> post: An ArrayList of Message objects (of around DESIRED_MESSAGES)
     * @param messages A string containing several discord message jsons
     */
    private ArrayList<Message> splitMessage(String messages){
        ArrayList<Message> splitMessages = new ArrayList<Message>(DESIRED_MESSAGES);
        int startIndex = 1;
        int endIndex = messages.indexOf("},{\"type\":");
        int numMessagesFound = 0;
        while (numMessagesFound <= DESIRED_MESSAGES && endIndex != -1){
            String message = messages.substring(startIndex,endIndex+1);
            startIndex = endIndex + 2;
            endIndex = messages.indexOf("},{\"type\":",startIndex+10);
            numMessagesFound++;
            splitMessages.add(new Message(message));
            if(splitMessages.get(splitMessages.size() - 1).getAuthorId() == 0){
                splitMessages.remove(splitMessages.size() - 1);
            }
        }
    return splitMessages;
    }
}
