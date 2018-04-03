/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spamfilter;

import java.util.ArrayList;

/**
 *
 * @author olgac
 */
public class Seleccion {
    ArrayList<Mensaje> Mensaje = new ArrayList<>();

    ArrayList<Mensaje> TrainingList = new ArrayList<>();
    ArrayList<Mensaje> CrossList = new ArrayList<>();
    ArrayList<Mensaje> TestList = new ArrayList<>();
    int mensajeSize;
    
    public Seleccion(ArrayList Mensajes, ArrayList Training, ArrayList Cross, ArrayList Test){
        this.Mensaje = Mensajes;
        this.TrainingList = Training;
        this.CrossList = Cross;
        this.TestList = Test;
        mensajeSize = Mensajes.size();
    }
    
    public void seleccionar(){
        while (this.TrainingList.size() < (int) (this.mensajeSize * 0.8)) {
            int num = (int) Math.random() * this.Mensaje.size();
            this.TrainingList.add(this.Mensaje.get(num));
            this.Mensaje.remove(num);        }


        while (this.CrossList.size() < (int) (this.mensajeSize * 0.1)) {
            int num = (int) Math.random() * this.Mensaje.size();
            this.CrossList.add(this.Mensaje.get(num));
            this.Mensaje.remove(num);
        }
        while (this.TestList.size() < (int) (this.mensajeSize * 0.1)) {
            int num = (int) Math.random() * this.Mensaje.size();
            this.TestList.add(this.Mensaje.get(num));
            this.Mensaje.remove(num);
        }   
    }
    
    public int contarPalabras(){
        ArrayList<String> palabras = new ArrayList<String>();
        int cantPalarasTotal = 0;
        
        for(int i=0; i<this.Mensaje.size(); i++){
            String oracion = this.Mensaje.get(i).Mensaje;
            String [] dividido = oracion.split(" ");
            
            for(int j = 0; j < dividido.length; j++){
                if(!palabras.contains(dividido[j])){
                    palabras.add(dividido[j]);
                }
            }
        }
        cantPalarasTotal = palabras.size();
     return cantPalarasTotal;
    }
    
    public void contPartes(ArrayList<Mensaje> Tipo, ArrayList<String> palHam, ArrayList<String> palSpam){
        
        
        for(int i = 0; i<this.Mensaje.size(); i++){
            String tipoMensaje = this.Mensaje.get(i).Tipo;
            if (tipoMensaje.equals("spam")){
                String oracion = this.Mensaje.get(i).Mensaje;
                String [] dividido = oracion.split(" ");
                for(int j = 0; j<dividido.length; j++){
                   palSpam.add(dividido[j]);
                }
            }
            else if (tipoMensaje.equals("ham")){
                String oracion = this.Mensaje.get(i).Mensaje;
                String [] dividido = oracion.split(" ");
                for(int j = 0; j<dividido.length; j++){
                    palHam.add(dividido[j]);
                }
            }
        }
    }
    
    public double laplaceSmoothingBasic(int k, String palabra, ArrayList tipoPalabra, int palabrasTotales ){
        //contar la probabilidad de la palabra en busca del array
        int contOcurrencia = 0;
        for(int i = 0; i< tipoPalabra.size(); i++ ){
            if(tipoPalabra.get(i).equals(palabra)){
                contOcurrencia++;
            }
        }
        int contObservaciones = tipoPalabra.size();
        return (double)(contOcurrencia + k) / (double)(contObservaciones + k*(palabrasTotales));
              
    }
    /*
     * calculo de la probabilidad de spam/ham
    */
    public double probMain(ArrayList<Mensaje> grupo, String tipo, int k ){
        int contOcurrencia = 0;
        for(int i = 0; i< grupo.size(); i++){
            if(grupo.get(i).Tipo.equals(tipo)){
                contOcurrencia++;
            }
        }
        int contObservaciones = grupo.size();
        return (double)(contOcurrencia + k) / (double)(contObservaciones + k*(2));
    }
    /*
     * calcular probabilidad de mensajes 
    */
    public double probOracion(String [] mensaje, int k, ArrayList tipoPalabra, int palabrasTotales){
        double prob=1;
        for(int i = 0; i<mensaje.length; i++){
            prob = prob * (laplaceSmoothingBasic(k, mensaje[i], tipoPalabra, palabrasTotales ));
        }
        return prob;
    }
    
    public double probTotalSpam(String [] mensaje, int k, ArrayList tipoHam, ArrayList tipoSpam, int palabrasTotales, ArrayList<Mensaje> grupo){
        return (probOracion(mensaje, k, tipoSpam, palabrasTotales) * probMain(grupo, "spam", k ))/(probOracion(mensaje, k, tipoSpam, palabrasTotales) * probMain(grupo, "spam", k ) + probOracion(mensaje,k, tipoHam, palabrasTotales) * probMain(grupo, "ham", k ));
    }
    
    public double probTotalHam(String [] mensaje, int k, ArrayList tipoHam, ArrayList tipoSpam, int palabrasTotales, ArrayList<Mensaje> grupo){
        return (probOracion(mensaje, k, tipoHam, palabrasTotales) * probMain(grupo, "ham", k ))/(probOracion(mensaje, k, tipoHam, palabrasTotales) * probMain(grupo, "ham", k ) + probOracion(mensaje,k, tipoSpam, palabrasTotales) * probMain(grupo, "spam", k ));
    }
}
