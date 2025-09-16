import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Message implements Serializable{
    //instance variables
    private long id;
    private long channelId;
    private String authorName;
    private long authorId;
    private String content;
    private long timeStamp;
    //class constants
    private static final long serialVersionUID = 1L;

    //constructor
    public Message(String data){
        try {
            this.content = data.substring(data.indexOf("\"content\"") + 11, data.indexOf("\"mentions\"") - 2);
            int startOfIdInfo = data.indexOf("\"edited_timestamp\"");
            this.id = Long.parseLong(data.substring(data.indexOf("],\"id\":", startOfIdInfo) + 8, data.indexOf("\"channel_id\"", startOfIdInfo) - 2));
            this.channelId = Long.parseLong(data.substring(data.indexOf("\"channel_id\"", startOfIdInfo) + 15, data.indexOf("\"author\"", startOfIdInfo) - 2));
            int startOfAuthorInfo = data.indexOf("\"author\"", data.indexOf("\"channel_id\""));
            this.authorName = data.substring(data.indexOf("\"username\"", startOfAuthorInfo) + 12, data.indexOf("\"avatar\"", startOfAuthorInfo) - 2);
            this.authorId = Long.parseLong(data.substring(data.indexOf("\"id\":", startOfAuthorInfo) + 6, data.indexOf("\"username\"", startOfAuthorInfo) - 2));
            String time = data.substring(data.indexOf("\"timestamp\"") + 13, startOfIdInfo - 15);
            // Parse the string into a unix
            LocalDateTime ldt = LocalDateTime.parse(time);
            ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
            this.timeStamp = zdt.toEpochSecond();
        }
        catch(Exception e){}
    }

    //getter methods
    public long getID(){
        return id;
    }
    public long getChannelId(){
        return channelId;
    }
    public String getAuthorName(){
        return authorName;
    }
    public String getContent(){
        return content;
    }
    public long getAuthorId(){
        return authorId;
    }
    public long getTimeStamp(){
        return timeStamp;
    }

    @Override
    public String toString() {
        return "Content: \"" + content + "\"\n"
            + "ID: " + id + "\n"
            + "Channel ID: " + channelId + "\n"
            + "Author Name: \"" + authorName + "\"\n"
            + "Author ID: " + authorId + "\n"
            + "Time Sent: " + timeStamp + "\n";
    }
}
