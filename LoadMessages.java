import java.io.File;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class LoadMessages {


    public static ArrayList<Message> loadChannel(String serverName , String channelName) throws FileNotFoundException, IOException {
        ArrayList<Message> allMessages = new ArrayList<>();

        int start = 1;
        while(true) {
            File f = new File(serverName + "/" + channelName + "/messages" + (start) + ".ser");
            if(f.exists()) {
                System.out.println(start);
                start++;
            }
            else{
                start--;
                break;
            }
        }
        for(int i = 1; i <= start; i++){
            File f = new File(serverName + "/" + channelName + "/messages" + (i) + ".ser");
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                ArrayList<Message> chunk = (ArrayList<Message>) ois.readObject();
                allMessages.addAll(chunk);
                System.out.println(chunk.get(0));
                System.out.println(chunk.get(chunk.size() - 1));
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error reading file: " + f.getName());
                e.printStackTrace();
            }
        }
        System.out.println(allMessages.size() + " Messages found in " + channelName);
        return allMessages;
    }

}
