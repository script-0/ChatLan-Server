/*
 * @Isaac
 */
package server;

import java.io.Serializable;

/**
 *
 * @author Isaac
 */
public class ServerMessage implements Serializable{
    private String content;
    private String emetteur = "SERVER";
    private String date;
    private String type = "MESSAGE";

    public ServerMessage(){
        content="";
        date = LaunchServer.getTime();
    }    
    
    public ServerMessage(String content){
        this.content=content;
    }
    
    public ServerMessage(String content, String emetteur, String date) {
        this.content = content;
        this.emetteur = emetteur;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(String emetteur) {
        this.emetteur = emetteur;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    
}
