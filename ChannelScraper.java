//standard Java imports
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;

public class ChannelScraper {
    //instance variables
    String device;
    String token;
    long channelId;
    String channelName;
    String serverName;
    //constructor
    public ChannelScraper(String device, String token, long ChannelId, String channelName, String serverName){
        this.device = device;
        this.token = token;
        this.channelId = ChannelId;
        this.channelName = channelName;
        this.serverName = serverName;
    }
    /**
     * Runs this line of code, creating a folder of this channel's messages
     * <br> pre: none
     * <br> post: A folder filled with .ser objects
     */
    public void run() throws MalformedURLException,IOException{
        //message data
        Collector collector = new Collector(device,token,channelId);
        ArrayList<Message> someMessages = new ArrayList<Message>();
        ArrayList<Message> messagesToFile = new ArrayList<Message>(10000);
        //makes directory
        File singleDir = new File(serverName + "/" + channelName + "/");
        if (singleDir.mkdir()) {
            System.out.println("Single directory created:" + serverName + "/" + channelName + "/");
        } else {
            System.out.println("Failed to create single directory or it already exists: " + serverName + "/" + channelName + "/");
        }
        //data about messages scraped
        long lastMessageId = 0;
        int iterations = 0;
        int totalMessages = 0;
        int currentMessages = 0;
        long startTime = System.nanoTime();
        long currentTime = System.nanoTime();
        while(someMessages.size() > 1 || lastMessageId == 0) {
            //collects some messages
            someMessages = collector.getMessages(lastMessageId);
            if(someMessages.isEmpty()){
                break;
            }
            messagesToFile.addAll(someMessages);
            lastMessageId = (someMessages.get(someMessages.size() - 1).getID());
            iterations++;
            totalMessages += someMessages.size();
            currentMessages += someMessages.size();
            //prints info here and there
            if (iterations % 10 == 0){
                System.out.println("---------------------------------------------------------------------------------------------------------------------");
                System.out.println("Another " + currentMessages +" messages have been collected");
                System.out.println("Total Messages Scraped: " + totalMessages);
                System.out.println("Current time per 1000 messages: " + (System.nanoTime() - currentTime)/1_000_000_000.0 * 1000 / currentMessages + " seconds");
                System.out.println("Average time per 1000 messages: " + (System.nanoTime() - startTime)/1_000_000_000.0 * 1000 / totalMessages + " seconds");
                System.out.println(messagesToFile.get(messagesToFile.size() - 1));
                currentTime = System.nanoTime();
                currentMessages = 0;
            }
            if (iterations % 100 == 0){
                //saves messages
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serverName + "/" + channelName + "/messages" + (iterations/100) + ".ser"))) {
                    oos.writeObject(messagesToFile);
                    System.out.println(messagesToFile.size() + " Messages saved!");
                    System.out.println(messagesToFile.get(0));
                    System.out.println("\n" + messagesToFile.get(messagesToFile.size() - 1));
                    messagesToFile.clear();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //saves messages one last time
        iterations += 100;
        if(!messagesToFile.isEmpty()){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serverName + "/" + channelName + "/messages" + (iterations/100) + ".ser"))) {
            oos.writeObject(messagesToFile);
            System.out.println(messagesToFile.size() + " Messages saved!");
            System.out.println(messagesToFile.get(0));
            System.out.println(messagesToFile.get(messagesToFile.size() - 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        }

        System.out.println("Final amount of messages saved: " + totalMessages);
    }
}
