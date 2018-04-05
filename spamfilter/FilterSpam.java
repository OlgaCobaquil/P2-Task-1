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
        ArrayList<Mensaje> TestSalida = new ArrayList<>();
        int mejorK =0;
        double temporal = 0;
        
        
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
         * CrossSalida: array 
         */
        for (int k = 1; k<=4; k++ ){
            CrossSalida = new ArrayList<Mensaje>();
            for (int i = 0; i <= CrossList.size()-1; i++){
                double pruebaOracionSpam = sel.probTotalSpam(CrossList.get(i).Mensaje.split("\\s"), k, palHam, palSpam, cantPalabras, TrainingList);
                double pruebaOracionHam = sel.probTotalHam(CrossList.get(i).Mensaje.split("\\s"), k, palHam, palSpam, cantPalabras, TrainingList);
              
                Mensaje mensajeCr = new Mensaje();
            
                if (pruebaOracionHam< pruebaOracionSpam){
                    mensajeCr.setTipo("spam");
                    mensajeCr.setMensaje((String.join(" ", CrossList.get(i).Mensaje)));
                    CrossSalida.add(mensajeCr);               

                }
                else if (pruebaOracionHam> pruebaOracionSpam){
                    mensajeCr.setTipo("ham");
                    mensajeCr.setMensaje((String.join(" ", CrossList.get(i).Mensaje)));
                    CrossSalida.add(mensajeCr);
                }
            }
            //calcular eficiencia
            double porcentaje = sel.rendimiento(CrossSalida, CrossList);
            if (porcentaje > temporal){
                temporal = porcentaje;
                mejorK = k;
                
            }
        }
        System.out.println("El mejor k es: " + mejorK);
        System.out.println("Porcentaje: " + temporal);

        
        /*
         * Test
         */
        
        for (int i = 0; i <= TestList.size()-1; i++){
            double pruebaOracionSpam = sel.probTotalSpam(TestList.get(i).Mensaje.split("\\s"), mejorK, palHam, palSpam, cantPalabras, TrainingList);
            double pruebaOracionHam = sel.probTotalHam(TestList.get(i).Mensaje.split("\\s"), mejorK, palHam, palSpam, cantPalabras, TrainingList);
              
            Mensaje mensajeTest= new Mensaje();
            
            if (pruebaOracionHam< pruebaOracionSpam){
                mensajeTest.setTipo("spam");
                mensajeTest.setMensaje((String.join(" ", TestList.get(i).Mensaje)));
                TestSalida.add(mensajeTest);               

            }
            else if (pruebaOracionHam> pruebaOracionSpam){
                mensajeTest.setTipo("ham");
                mensajeTest.setMensaje((String.join(" ", TestList.get(i).Mensaje)));
                TestSalida.add(mensajeTest);
            }
        }
        double porcentaje1 = sel.rendimiento(TestSalida, TestList);
        System.out.println("Porcentaje final: "+ porcentaje1);
        
        //abrir archivo 
        ingreso = reader.LeerNuevoArchivo("prueba.txt");
 
        for (int i = 0; i<ingreso.size();i++){
            
            double pruebaOracionSpam = sel.probTotalSpam(ingreso.get(i), mejorK, palHam, palSpam, cantPalabras, TrainingList);
            //System.out.println("prob oracion spam " +pruebaOracionSpam);
            double pruebaOracionHam = sel.probTotalHam(ingreso.get(i), mejorK, palHam, palSpam, cantPalabras, TrainingList);
            //System.out.println("prob oracion ham " +pruebaOracionHam);
            
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
