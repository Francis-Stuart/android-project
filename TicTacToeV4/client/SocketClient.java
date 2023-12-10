import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketClient {

    private static SocketClient instance;

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private Gson gson;


    public SocketClient(){

    }

    public getInstance(){
        if (instance == null){
            instance = new SocketClient();
        }
        return instance;
    }
    public close(){
        try{
            if (inputStream != null){
                inputStream.close();
            }
            if (outputStream != null){
                outputStream.close();
            }
            if (socket != null){
                socket.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response sendRequest(Request request){
        Response response null;
        try{
            socket = new Socket("128.153.187.3", 5000);

            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getInputStream());

            String jsonRequest = serializeRequestToJson(request);

            outputStream.writeUTF(jsonRequest);
            outputStream.flush();

            String jsonResponse = inputStream.readUTF();
            response deserializeResponseFromJson(jsonResponse);
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return response;
    }

    private String serializeRequestToJson(Request request) {
        Gson gson = new Gson();
        return gson.toJson(request);
    }

    private Response deserializeResponseFromJson(String json Response){
        Gson gson = new Gson();
        try {
            return gson.fromJson(jsonResponse, Response.class);

        } catch (JsonSyntaxException e){
            e.printStackTrace();
            return new Response();
        }
    }

}