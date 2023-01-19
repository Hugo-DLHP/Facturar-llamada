import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class facturacion_llamada {
    public static void main(String[] args) {        
        /*Pedir fecha y hora de comienzo ademas de la duracion de la llamada*/

        Scanner leer = new Scanner(System.in);

        //VARIABLES HORA Y DURACION LLAMADA
        int hora_comienzo, minuto_comienzo, segundo_comienzo;
        int hora_duracion, minuto_duracion, segundo_duracion;
        LocalTime comienzo_hora, duracion_hora;

        //VARIABLES FECHA LLAMADA
        int año, mes, dia;
        boolean condicion = false;
        LocalDate fecha_llamada = LocalDate.of(1111,1,1);

        
        //VARIABLES FACTURACION LLAMADA
        int aux_hora_duracion, aux_minuto_duracion;
        float aux_tiempo_duracion, diferencia, aux_segundo_duracion;
        final int MAXIMO = 60;
        float precio_total = (float) 0.0;
        int suma_tiempos;

        //VARIABLES IMPUESTOS
        int suma_tiempo_llamada;
        final int COMPROBANTE = 129;
        final int IMPUESTO_MAÑANA = 15;
        final int IMPUESTO_TARDE = 10;
        final int IMPUESTO_NO_LABORABLE = 3;
        float impuesto_aplicar_turno, impuesto_aplicar_laborable, impuesto_aplicar_total;
        int aux_minuto_comienzo, aux_segundo_comienzo;
        final LocalDate SABADO = LocalDate.of(2022, 11, 19);
        final LocalDate DOMINGO = LocalDate.of(2022, 11, 20);
        /**************************************************************************/


        //DARLE FORMATO A LA FECHA PARA QUE SE VEA COMO SE SUELE REPRESENTAR EN ESPAÑA
        //LO HE ENCONTRADO EN GOOGLE 
        DateTimeFormatter esDateFormatLargo = DateTimeFormatter.ofPattern("EEEE, dd 'de' MMMM 'de' yyyy 'a las' ");


        do {
            //FECHA DE COMIENZO DE LA LLAMADA
            año = tiempo(2020, 2022, "año", "la llamada");
            mes = tiempo(1, 12, "mes", "la llamada");
            dia = tiempo(1, 31, "dia", "la llamada");
            try {
                fecha_llamada = LocalDate.of(año, mes, dia);
                condicion = true;         
            } catch (Exception e) {
                System.out.print("La fecha introducida no es valida");
                leer.nextLine();
            }
        } while (condicion != true);
        /**************************************************************************/


        //HORA DE COMIENZO DE LA LLAMADA
        hora_comienzo = tiempo(0, 23, "hora", "comienzo");
        minuto_comienzo = tiempo(0, 59, "minuto", "comienzo");
        if ((hora_comienzo == 0) && (minuto_comienzo == 0)) {
            segundo_comienzo = tiempo(1, 59, "segundo", "comienzo");
        } else {
            segundo_comienzo = tiempo(0, 59, "segundo", "comienzo");
        }
        
        comienzo_hora = LocalTime.of(hora_comienzo, minuto_comienzo, segundo_comienzo);
        /**************************************************************************/


        //HORA DE DURACION DE LA LLAMADA
        hora_duracion = tiempo(0, 23, "hora", "duracion");
        minuto_duracion = tiempo(0, 59, "minuto", "duracion");
        if ((hora_duracion == 0) && (minuto_duracion == 0)) {
            segundo_duracion = tiempo(1, 59, "segundo", "duracion");
        } else {
            segundo_duracion = tiempo(0, 59, "segundo", "duracion");
        }

        duracion_hora = LocalTime.of(hora_duracion, minuto_duracion, segundo_duracion);        
        /**************************************************************************/


        //CALCULAR PRECIO LLAMADA
        aux_hora_duracion = hora_duracion;
        aux_minuto_duracion = minuto_duracion;
        aux_segundo_duracion = segundo_duracion;

        aux_tiempo_duracion = (aux_hora_duracion * MAXIMO) + aux_minuto_duracion + (aux_segundo_duracion/MAXIMO);
        
        suma_tiempos = 5 + 3 + 2;
        diferencia = aux_tiempo_duracion;

        if (diferencia <= 5) {
            precio_total = (float) (0.5 * diferencia);
        } else if (diferencia <= (5 + 3)) {
            diferencia = diferencia - 5;
            precio_total = (float) ((0.5 * 5) + (0.4 * diferencia)); 
        } else if (diferencia <= suma_tiempos) {
            diferencia = diferencia - (5 + 3);
            precio_total = (float) ((0.5 * 5) + (0.4 * 3) + (0.3 * diferencia));  
        } else {
            diferencia = diferencia - suma_tiempos;
            precio_total = (float) ((0.5 * 5) + (0.4 * 3) + (0.3 * 2) + (0.15 * diferencia));
        }

        System.out.println("Precio sin impuestos: " + precio_total);
        /**************************************************************************/

        //CALCULAR IMPUESTOS
        //IMPUESTO DE TURNO (MAÑANA/TARDE)
        if (minuto_comienzo == 0) {
            aux_minuto_comienzo = 60;
        } else {
            aux_minuto_comienzo = minuto_comienzo;
        }
        if (segundo_comienzo == 0) {
            aux_segundo_comienzo = 60;
        } else aux_segundo_comienzo = segundo_comienzo;

        suma_tiempo_llamada = hora_comienzo + aux_minuto_comienzo + aux_segundo_comienzo;

        if (suma_tiempo_llamada > COMPROBANTE) {
            impuesto_aplicar_turno = precio_total * IMPUESTO_TARDE / 100;
        } else {
            impuesto_aplicar_turno = precio_total * IMPUESTO_MAÑANA / 100;
        }
        /**************************************************************************/

        //IMPUESTO FIN DE SEMANA
        if (SABADO.getDayOfWeek() == fecha_llamada.getDayOfWeek() || DOMINGO.getDayOfWeek() == fecha_llamada.getDayOfWeek()) {
            impuesto_aplicar_laborable = precio_total * IMPUESTO_NO_LABORABLE / 100;
            impuesto_aplicar_total = impuesto_aplicar_laborable;
        } else {
            impuesto_aplicar_total = impuesto_aplicar_turno;
        }
        /**************************************************************************/

        precio_total = precio_total + impuesto_aplicar_total;
        /**************************************************************************/



        //MOSTRAR RESULTADO
        System.out.println("\n\nLa fecha y hora de inicio de la llamada es: " + fecha_llamada.format(esDateFormatLargo) + comienzo_hora + 
                           "\nY la llamada ha durado: " + duracion_hora);
        System.out.printf("La llamada te costara: " + "%.2f", precio_total);
        /**************************************************************************/

    }

     
    //COMPROBACION DATOS VALIDOS
    public static int tiempo(int desde, int hasta, String texto, String extra) {
        Scanner leer = new Scanner(System.in);
        boolean comprobacion = false;
        int dato = -0;

        do {
            try {
                System.out.print("\n\nIntruduzca " + texto + " de " + extra + "(" + desde + "-" + hasta + "):");
                dato = leer.nextInt();
                if ((dato >= desde) && (dato <= hasta)) {   
                    comprobacion = true;
                } else {    
                    System.out.print("El valor introducido no es valido");
                }
            } catch (Exception e) {
                System.out.println("El valor introducido no es valido");
                leer.nextLine();
            }
        } while (comprobacion != true);

        return dato;
    }
    /**************************************************************************/
}
