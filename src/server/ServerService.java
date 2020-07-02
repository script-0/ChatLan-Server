/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;

import main.Server;

/**
 *
 * @author Isaac
 */
public class ServerService extends Task<Void> {
    Socket s;
    String id;
    int taille;
    Object tmp;
    ObjectInputStream input;
    ObjectOutputStream output;
    
    static short iTmp=0;

    public ServerService(Socket s, String id,ObjectInputStream i,ObjectOutputStream o) {
        this.s = s;
        this.id = id;
        input=i;
        output=o;
    }
    
    
    void multicast(Object t){
        Server.streams.remove(this.output);
        Server.streams.forEach((sTmp) -> {                
            try {
                sTmp.writeObject(t);
                sTmp.flush();
            } catch (IOException ex) {
                Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        Server.streams.add(this.output);
    }
    
   // @Override
    @Override
    protected Void call(){
        try{
                while(!s.isClosed()){
                    tmp = input.readObject();
                    multicast(tmp);
                }
        } catch (IOException ex) {
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            Server.outputs.remove(this.s);
            Server.services.remove(this);
            try {
                s.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
