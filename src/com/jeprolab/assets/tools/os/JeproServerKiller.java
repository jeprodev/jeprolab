package com.jeprolab.assets.tools.os;

import com.jeprolab.JeproLab;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class JeproServerKiller {
    public static boolean quitJeproServer() {
        String settingsDirectory = System.getProperty("user.home");
        File portFile;
        File file = new File(settingsDirectory);
        portFile = new File(file, ".jeprolab/server");

        if(portFile.exists()){
            try{
                BufferedReader input = new BufferedReader(new FileReader(portFile));
                String check = input.readLine();
                if(!check.equals("b")){
                    System.out.println(JeproLab.getBundle().getString("JEPROLAB_WRONG_PORT_FILE_FORMAT"));
                    return false;
                }

                int port = Integer.parseInt(input.readLine());
                int key = Integer.parseInt(input.readLine());

                Socket socket = new Socket(InetAddress.getByName(null), port);
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                output.writeInt(key);

                String script = "JeproLab.exit(null, true);\n";
                output.writeUTF(script);

                /** block until the server is closed **/
                try{
                    socket.getInputStream().read();
                }catch (Exception ignored){}

                input.close();
                output.close();
            }catch (FileNotFoundException | UnknownHostException ignored){

            } catch (IOException io){
                System.out.println(JeproLab.getBundle().getString("JEPROLAB_EXCEPTION_WHILE_TRYING_TO_CONNECT_TO_EXISTING_SERVER_MESSAGE"));
                System.out.println(io.toString());
                System.out.println(JeproLab.getBundle().getString("JEPROLAB_DO_NOT_WORRY_TOO_MUCH_MESSAGE"));
                return false;
            }
        }
        return  true;
    }

    /**
     * Try to connect to any running server instance and close it. Exist with an error code on failure, but not if no server
     * was found.
     * @param args
     */
    public static void main(String[] args){
        boolean success = quitJeproServer();
        if (!success) {
            System.exit(-1);
        }
    }
}