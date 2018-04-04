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
    
    ArrayList<String> entrada = new ArrayList<>();
    String [] oraciones;
    
    public ReadFile(ArrayList Spam, ArrayList Ham, ArrayList Mensaje)throws FileNotFoundException, IOException{
        this.Spam = Spam;
        this.Ham = Ham;
        this.Mensaje = Mensaje;
        
    }
    
    public ArrayList<String[]> LeerNuevoArchivo(String direccion) throws FileNotFoundException, IOException{
        ArrayList devuelta = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(direccion))) {
            
            String line;
            while ((line = br.readLine()) != null) {
                String [] oracion = line.split("\\t");
                oracion[0] = oracion[0].replaceAll("[^a-zA-Z\\s]+", "");
                oracion[0] = oracion[0].toLowerCase();
                entrada.add(oracion[0]);
 
            }
            
            for (int i=0; i<=entrada.size()-1;i++){
                String[] oraciones=entrada.get(i).split("\\s");
                devuelta.add(oraciones);
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
                    content[1] = content[1].replaceAll("[^a-zA-Z\\s]+", "");
                    content[1] = content[1].toLowerCase();
                    this.Ham.add(content[1]);
                    Mensaje msj = new Mensaje();
                    msj.Tipo = "ham";
                    msj.Mensaje = content[1];
                    this.Mensaje.add(msj);
                }
                else if (content[0].equals("spam")){
                    content[1] = content[1].replaceAll("[^a-zA-Z\\s]+", "");
                    content[1] = content[1].toLowerCase();
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