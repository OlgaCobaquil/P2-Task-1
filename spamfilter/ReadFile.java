/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spamfilter;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author olgac
 */
public class ReadFile {
    ArrayList<String> Spam = new ArrayList<>();
    ArrayList<String> Ham = new ArrayList<>();
    ArrayList<Mensaje> Mensaje = new ArrayList<>();

    
    public ReadFile(ArrayList Spam, ArrayList Ham, ArrayList Mensaje)throws FileNotFoundException, IOException{
        this.Spam = Spam;
        this.Ham = Ham;
        this.Mensaje = Mensaje;
        
    }
    
    public ArrayList<Mensaje> LeerNuevoArchivo(String direccion) throws FileNotFoundException, IOException{
        ArrayList<Mensaje> devuelta = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(direccion))) {
            
            String line;
            while ((line = br.readLine()) != null) {
                
                String [] oracion = line.split("\t");
                System.out.println(oracion[0].toString());
                if (oracion[0].equals("ham")){
                    oracion[1] = oracion[1].replaceAll("[^a-zA-Z0-9\\s\']+", "");
                    
                    Mensaje mensaje = new Mensaje();
                    mensaje.Tipo = "ham";
                    mensaje.Mensaje = oracion[1];
                    devuelta.add(mensaje);
                }
                else if (oracion[0].equals("spam")){
                    oracion[1] = oracion[1].replaceAll("[^a-zA-Z0-9\\s\']+", "");
                    
                    Mensaje mensaje = new Mensaje();
                    mensaje.Tipo = "spam";
                    mensaje.Mensaje = oracion[1];
                    devuelta.add(mensaje);
                }
                
                // process the line.
            }
            
            // line is not visible here.
        }
        return devuelta;
    }
    
    public void LeerArchivo() throws FileNotFoundException, IOException{
        try (BufferedReader br = new BufferedReader(new FileReader("corpus.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                
                String [] content = line.split("\t");
                if (content[0].equals("ham")){
                    content[1] = content[1].replaceAll("[^a-zA-Z0-9\\s\']+", "");
                    this.Ham.add(content[1]);
                    Mensaje msj = new Mensaje();
                    msj.Tipo = "ham";
                    msj.Mensaje = content[1];
                    this.Mensaje.add(msj);
                }
                else if (content[0].equals("spam")){
                    content[1] = content[1].replaceAll("[^a-zA-Z0-9\\s\']+", "");
                    this.Spam.add(content[1]);
                    Mensaje msj = new Mensaje();
                    msj.Tipo = "spam";
                    msj.Mensaje = content[1];
                    this.Mensaje.add(msj);
                }
            }
        }
    }
}