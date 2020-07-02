/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.concurrent.Task;
import main.Server;

/**
 *
 * @author Isaac
 */
public class LaunchServer extends Task<Void>{
    
    int nClient = 0;
    int port = 4321; 
    
    ArrayList<String> users = new ArrayList<>();
    
    public LaunchServer(int port){
        this.port= port;
        Server.services.addListener((Observable observable) -> {
            nClient = Server.services.size();
            updateMessage("Server Up: "+nClient+" client"+(nClient>1?"s":""));
        });
    }
    
    void initService(Socket s,String name,ObjectInputStream i,ObjectOutputStream o){
        ServerService service = new ServerService(s,name,i,o);
            service.setOnSucceeded((event) -> {
                Server.services.remove(service);
            });
            service.setOnCancelled((event) -> {
               Server.services.remove(service);
            });
            
            service.setOnFailed((event) -> {
                Server.services.remove(service);
            });
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(service);
            executorService.shutdown();
    }
    
    public static ServerMessage createMsg(String content){
        ServerMessage message = new ServerMessage(content);        
        return message;
    }
    
    static Calendar c = GregorianCalendar.getInstance();
    
    static public String getTime(){
         c.setTime(new Date());
         return String.format("%2d : %2d",c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));
    }
    
    @Override
    protected Void call() throws ClassNotFoundException {
        ServerSocket ss = null;
        try {
            updateMessage("Server Starting on port "+port+" ... ");
           // InetAddress a =  InetAddress.getLocalHost();
            ss = new ServerSocket(port);
            updateMessage("Server Up(port:"+port+") : Waiting client ...");
            
            
            ObjectOutputStream output;
            ObjectInputStream input;
            String tmp;
            while(!ss.isClosed()){
                try{
                    Socket s = ss.accept();
                    nClient++;
                    updateMessage("Server Up: "+nClient+" client"+(nClient>1?"s":""));
 
                    input = new ObjectInputStream(s.getInputStream());
                    tmp = (String)input.readObject();
                    
                    for(ObjectOutputStream  sTmp: Server.streams){
                        sTmp.writeObject("n:"+tmp);
                        sTmp.flush();
                    }
                    
                    output = new ObjectOutputStream(s.getOutputStream());
                    Server.streams.add(output);
                    for(String u:users){
                            output.writeObject("u:"+u);
                            output.flush();
                    }
                    users.add(tmp);
                    System.out.println(users.toString());
                    Server.outputs.add(s);
                    
                    initService(s,tmp,input,output);
                } catch (IOException ex) {
                    updateMessage("Server: Error on Client"+nClient);
                }finally{
                    
                }
            }
            return null;
        } catch (IOException ex) {
           updateMessage("Loading Server Failed");
        }finally{
            try {
                if(ss!=null)  ss.close();
            } catch (IOException ex) {
                Logger.getLogger(LaunchServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
}
