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
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author olgac
 */
public class FilterSpam {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) throws IOException {
        
        ArrayList<String> Ham = new ArrayList<>();
        ArrayList<String> Spam = new ArrayList<>();
        
        ArrayList<String> palHam = new ArrayList<>();
        ArrayList<String> palSpam = new ArrayList<>();
        
        ArrayList<Mensaje> TrainingList = new ArrayList<>();
        ArrayList<Mensaje> CrossList = new ArrayList<>();
        ArrayList<Mensaje> TestList = new ArrayList<>();
        
        ArrayList<Mensaje> Mensaje = new ArrayList<>();
        ArrayList<String[]> ingreso = new ArrayList<>();
        
        ArrayList<Mensaje> MensajeSalida = new ArrayList<>();
        ArrayList<Mensaje> CrossSalida = new ArrayList<>();
        
        
        //leer el archivo 
        ReadFile reader = new ReadFile(Spam, Ham, Mensaje);
        reader.LeerArchivo();  
        /*task 1.2 y 1.3 selecionar y sanitizar*/
        Seleccion sel = new Seleccion(Mensaje, TrainingList, CrossList, TestList);
        
        //cantidad total de palabras 
        int cantPalabras = sel.contarPalabras();
        sel.contPartes(TrainingList, palHam, palSpam);
        
        double hola = sel.laplaceSmoothingBasic(1, "until", palHam, cantPalabras);
        
        double hola2 = sel.probMain(Mensaje,"ham", 1);
        /*System.out.println("probabilidad " +hola);
        System.out.println("probabilidad ham " + hola2);
        System.out.println("Palabras Ham: "+ palHam.size());
        System.out.println("Palabras Spam:  "+ palSpam.size());
        System.out.println(cantPalabras);*/
        sel.seleccionar();
        
        /*
         * Cross Validation
         */
        
        for (int i = 0; i <= CrossList.size()-1; i++){
            double pruebaOracionSpam = sel.probTotalSpam(CrossList.get(i).Mensaje.split("\\s"), 1, palHam, palSpam, cantPalabras, TrainingList);
            //System.out.println("prob oracion spam " +pruebaOracionSpam);
            double pruebaOracionHam = sel.probTotalHam(CrossList.get(i).Mensaje.split("\\s"), 1, palHam, palSpam, cantPalabras, TrainingList);
            //System.out.println("prob oracion ham " +pruebaOracionHam);
            
            Mensaje mensajeCr = new Mensaje();
            
            if (pruebaOracionHam< pruebaOracionSpam){
                //System.out.println("Es SPAM");
                mensajeCr.setTipo("spam");
                mensajeCr.setMensaje((String.join(" ", CrossList.get(i).Mensaje)));
                CrossSalida.add(mensajeCr);               
                
            }
            else if (pruebaOracionHam> pruebaOracionSpam){
                //System.out.println("Es HAM");
                mensajeCr.setTipo("ham");
                mensajeCr.setMensaje((String.join(" ", CrossList.get(i).Mensaje)));
                CrossSalida.add(mensajeCr);
            }
        }
        //calcular eficiencia
        
        double porcentaje = sel.rendimiento(CrossSalida, CrossList);
        System.out.println("Porcentaje: " + porcentaje);
        
        //abrir archivo 
        ingreso = reader.LeerNuevoArchivo("prueba.txt");
        int tama = ingreso.size();
        System.out.println(tama);
        
        
 
        for (int i = 0; i<ingreso.size();i++){
            
            double pruebaOracionSpam = sel.probTotalSpam(ingreso.get(i), 1, palHam, palSpam, cantPalabras, TrainingList);
            System.out.println("prob oracion spam " +pruebaOracionSpam);
            double pruebaOracionHam = sel.probTotalHam(ingreso.get(i), 1, palHam, palSpam, cantPalabras, TrainingList);
            System.out.println("prob oracion ham " +pruebaOracionHam);
            
            Mensaje mensaje = new Mensaje();
            
            if (pruebaOracionHam< pruebaOracionSpam){
                System.out.println("Es SPAM");
                mensaje.setTipo("spam");
                mensaje.setMensaje((String.join(" ", ingreso.get(i))));
                MensajeSalida.add(mensaje);               
                
            }
            else if (pruebaOracionHam> pruebaOracionSpam){
                System.out.println("Es HAM");
                mensaje.setTipo("ham");
                mensaje.setMensaje((String.join(" ", ingreso.get(i))));
                MensajeSalida.add(mensaje);
                
            }
            
        }
        PrintWriter writer = new PrintWriter("respuestas.txt", "UTF-8");
        for (int i=0; i<=MensajeSalida.size()-1; i++){
            writer.println(MensajeSalida.get(i).Tipo + "\t" + MensajeSalida.get(i).Mensaje + "\n");
            
        }
        writer.close();
        
    }
    
}
