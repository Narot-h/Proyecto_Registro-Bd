package pf_v1.pkg2;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.InputMismatchException;
public class PF_v12 {
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int opcion = 0;
        Coneccion_Bd.conectar();
    
        do {
            System.out.println("--------[BIENVENIDO]----------");
            System.out.println("1. REGISTRAR ESTUDIANTES");
            System.out.println("2. LISTA DE ESTUDIANTES");
            System.out.println("3. BUSCAR POR CODIGO");
            System.out.println("4. EDITAR DATOS DE UN ALUMNO");
            System.out.println("5. ELIMINAR DATOS DE UN ALUMNO");
            System.out.println("6. SALIR");
            System.out.print("Seleccione una opcion (1 - 6): ");
            do {
                try {
                    opcion = sc.nextInt();
                    if (opcion < 1 && opcion > 5){
                        System.out.println("Opcion invalida. Numero fuera del rango");
                        System.out.print("Ingrese la opcion nuevamente (1 - 6):");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Opcion invalida. No se adminten letras");
                    sc.nextLine();
                    System.out.print("Ingrese la opcion nuevamente (1 - 6):");
                }
            } while (opcion < 1 && opcion > 5);
            
            switch (opcion) {
                case 1:
                    registrar();
                    break;
                case 2:
                    lista();
                    break;
                case 3:
                    buscar();
                    break;
                case 4;
                    editar();
                    break;
                case 5
                    eliminar();
                    break;
                case 6:
                    System.out.println("Hasta luego");
                    break;
            }
        } while (opcion != 5);
    }
    
    private static void registrar() {
        String NombreC, ApellidoC;
        int edad = 0, codigo = 0, opcion = 0;
        
        do {
            System.out.print("Ingrese codigo del Estudiante: ");
            do {
                try {
                    codigo = sc.nextInt();
                    if (codigo < 99 && codigo > 999){
                        System.out.println("Codigo de estudiante fuera de rango");
                        System.out.print("Cree nuevamente el codigo: ");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Solo se permiten ingresar numeros no otras variables");
                    sc.nextLine();
                    System.out.print("Ingrese nuevamente el codigo: ");
                }
            }while (codigo < 99 || codigo > 999);
            sc.nextLine();
            
            System.out.println("Ingrese solo nombres completos: ");
            //Validador del Nombre completo del estudiante 
            do {
                NombreC= sc.nextLine();
                if (NombreC.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ]") == false){
                    System.out.println("Caracter incorrecto");
                    System.out.println("Ingrese el nombre nuevamente:");
                }
            }while(NombreC.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ]+") == false);
            sc.nextLine();
            
            System.out.println("Ingrese sus apellidos: ");
            //Validador del apellido
            do {
                ApellidoC= sc.nextLine();           
                if (ApellidoC.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ]") == false){
                    System.out.println("Caracter incorrecto");
                    System.out.println("Ingrese el apellido nuevamente:");
                }
            }while(ApellidoC.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ]") == false);
            
            System.out.print("Ingrese edad del estudiante: ");
            //Validador de la edad del estudiante
            do {
                try {
                edad=sc.nextInt();
                if (edad < 16 || edad > 35){
                    System.out.println("Edad incorrecta rango de edad -> (16 - 35)");
                    System.out.print("Ingrese la edad de nuevo: ");
                }
                } catch(InputMismatchException e){
                    System.out.println("Error: Solo se permiten ingresar numeros no otras variables");
                    sc.nextLine();
                    System.out.print("Ingrese nuevamente la edad: ");
                }
            }while (edad < 16 || edad > 35);
            
            String sql = "INSERT INTO Estudiantes (Codigo, nombre, apellido, edad) VALUES (?, ?, ?, ?)";
            
            try (Connection Conext = Coneccion_Bd.conectar(); PreparedStatement Consult = Conext.prepareStatement(sql)){
                Consult.setInt(1, codigo);
                Consult.setString(2, NombreC);
                Consult.setString(3, ApellidoC);
                Consult.setInt(4, edad);
                Consult.executeUpdate();
                System.out.println("Estudiante registrado correctamente!!!");
            } catch (SQLException e){
                System.out.println("Error al registrar: " + e.getMessage());
            }
            
            do {
                System.out.println("--------[SELECCIONA UNA OPCION]--------");
                System.out.println("1.  Registrar otro alumno");
                System.out.println("2.  Regresar al menu");
                System.out.println("---------------------------------------");
                //Nuevo
                //Aqui falto poner un validador para las opcioens 1 y 2 pq cuando le puse 3 o una letra me salio error
                do {
                    try {
                        opcion = sc.nextInt();
                        if (opcion<1 || opcion>2){
                            System.out.println("Opcion invalida; solo ingresar opciones (1-2)");
                            System.out.print("Ingrese nuevamente la opcion: ");
                        }
                    }catch (InputMismatchException e){
                        System.out.println("Las letras no estan permitidas como opciones");
                        sc.nextLine();
                        System.out.print("Selecciona nuevamente una opcion: ");
                                                
                    }
                }while (opcion<1||opcion>2);
            } while (opcion != 1 && opcion !=2 );
        } while (opcion != 2);    
    }
    
    private static void lista() {
        String sql = "SELECT Codigo, nombre, apellido, edad FROM Estudiantes";
        
        try (Connection Conext = Coneccion_Bd.conectar(); Statement Paloma = Conext.createStatement()){
            ResultSet Envio = Paloma.executeQuery(sql);
            boolean Entregado = false;
            System.out.println("--------------------------[LISTA]------------------------");
            System.out.println("[CODIGO]     [EDAD]                [ALUMNO]              ");
            while (Envio.next()){
                Entregado = true;
                int codigo = Envio.getInt("codigo");
                String nombre = Envio.getString("nombre");
                String apellido = Envio.getString("apellido");
                int edad = Envio.getInt("edad");
                System.out.println("" + codigo + "          " + edad + "          " + nombre + " " + apellido);
            }
            System.out.println("---------------------------------------------------------");
            if (Entregado == false) {
                System.out.println("No hay alumnos registrados");
            }
        } catch (SQLException e){
            System.out.println("Error al listar: " + e.getMessage());
        }
    }
    
    private static void buscar() {
        int codBuscar=0, opcion = 0;
        boolean encontrado;
        
        do {
            System.out.println("Ingrese el codigo del alumno que quiere buscar");
            //Nuevo
            //Validador para bsucar codigo
            do{
                try {
                    codBuscar = sc.nextInt();
                    if (codBuscar < 99 || codBuscar > 999){
                        System.out.println("Codigo de estudiante fuera de rango");
                        System.out.print("Ingrese nuevamente el codigo a buscar: ");
                    }
                } catch(InputMismatchException e){
                    System.out.println("Error: Solo se permiten ingresar numeros no otras variables");
                    sc.nextLine();
                    System.out.print("Ingrese nuevamente el codigo: ");
                }
            }while (codBuscar < 99 || codBuscar > 999);
            
            String sql = "SELECT Codigo, nombre, apellido, edad FROM Estudiantes  WHERE Codigo=?";
            encontrado = false;
            
            try(Connection Conext = Coneccion_Bd.conectar(); PreparedStatement Consult = Conext.prepareStatement(sql)){
                Consult.setInt(1, codBuscar);
                ResultSet Envio = Consult.executeQuery();
                if (Envio.next()){
                    encontrado = true;
                    System.out.println("Alumno encontrado!");
                    System.out.println("---------------------------------------------------------");
                    System.out.println("[CODIGO]     [EDAD]                [ALUMNO]              ");
                    System.out.println(" " + Envio.getInt("Codigo") + "          " + Envio.getInt("edad") + "          " + Envio.getString("nombre") + " " + Envio.getString("apellido"));
                    System.out.println("---------------------------------------------------------");        
                }
            }catch (SQLException Error101){
                System.out.println("Error de busqueda: "+Error101.getMessage());
            }
            
            if (encontrado == false) {
                System.out.println("El alumno con el codigo " + codBuscar + " no se encuentra en el registro");
            }
                    
            do {
                System.out.println("--------[SELECCIONA UNA OPCION]--------");
                System.out.println("1. Buscar otro alumno");
                System.out.println("2. Regresar al menu");
                System.out.println("---------------------------------------");
                //Nuevo
                //Aqui falto poner un validador para las opcioens 1 y 2 pq cuando le puse 3 o una letra me salio error
                do {
                    try {
                        opcion = sc.nextInt();
                        if (opcion<1 || opcion>2){
                            System.out.println("Opcion invalida; solo ingresar opciones (1-2)");
                            System.out.print("Ingrese nuevamente la opcion: ");
                        }
                    }catch (InputMismatchException e){
                        System.out.println("Las letras no estan permitidas como opciones");
                        sc.nextLine();
                        System.out.print("Selecciona nuevamente una opcion: ");
                                                
                    }
                }while (opcion<1||opcion>2);
            } while (opcion != 1 && opcion !=2 );
        } while (opcion != 2);    
    }


}
