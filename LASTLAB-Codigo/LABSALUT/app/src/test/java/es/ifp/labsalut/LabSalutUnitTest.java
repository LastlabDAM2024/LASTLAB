package es.ifp.labsalut;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import es.ifp.labsalut.negocio.CitaMedica;
import es.ifp.labsalut.negocio.Medicamento;
import es.ifp.labsalut.negocio.Usuario;
import es.ifp.labsalut.seguridad.CifradoAES;

import javax.crypto.SecretKey;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 33) // Importante poner esto! O no funcionará Robotics para el mock data (su versión máxima es 33)
public class LabSalutUnitTest {

 // Variables que se van a usar en los test siguientes

    private  Usuario usuario;
    private CifradoAES cifrado;
    private SecretKey secretKey;


 // Método que se va a ejecutar antes de cada test


@Before
public void setUp(){
    usuario =  new Usuario("Nella", "1993-01-01", "nells@gmail.com", "123");
    cifrado = new CifradoAES();
    secretKey = cifrado.generarSecretKey("semillaPrueba");
}


// Primer test: Verificación de creación de usuario

    /**
     * En este primer test unitario en el que se usará JUnit y Robotics, se crearán datos mock y luego se hará la simulación
     * de creación de usuario.
     * Si todo va bien y el test pasa la prueba, en Logcat aparecerá como TestPassed y se imprimirá en consola los datos de registro de usuario
     */


    @Test
    public void testCreacionUsuario(){

        //Se verifica que el usuario sea creado correctamente

        assertNotNull("Hey, el usuario no tiene que ser nulo!", usuario);

        // Se verifica después que cada campo tiene el valor que se espera
        // Los datos usados son los mocks creados en el paso anterior!

        assertEquals("Nella", usuario.getNombre());
        assertEquals("nells@gmail.com", usuario.getEmail());
        assertEquals("1993-01-01", usuario.getFechaNacimiento());
        assertEquals("123", usuario.getContrasena());
    }


    // Método para mostrar  los datos del usuario creado
    public void mostrarCreaciónUsuario() {
        System.out.println("Creación de usuario:");
        System.out.println("Nombre: Nella");
        System.out.println("Usuario: nells@gmail.com");
        System.out.println("Contraseña: 123");
    }



//Segundo Test: Gestión de medicamentos

    /**
     * Este segundo Test Unitario lo que hará es crear de manera ficticia un medicamento con sus respectivos parámetros y a través del método
     * mostrarMedicacion() imprimirá en consola los detalles de los datos ingresados en caso de que el test de positivo
     */


    @Test
    public void testGestionMedicamentos(){

        //Creación de medicamento mock con sus respectivos parámetros

        Medicamento medicamento = new Medicamento();
        medicamento.setNombre("Valium");
        medicamento.setDosis("500 mg");
        medicamento.setFrecuencia("Cada 12 hs");


        //Añadir medicamento al usuario


        usuario.setMedicamento(medicamento);

        //Se verifica que el usuario se haya añadido bien

        ArrayList medicamentos = usuario.getAllMedicamentos();
        assertTrue("La lista de medicamentos no tiene que estar vacía!", medicamentos.size() > 0);
        assertEquals("Tendría que haber exactamente 1 medicamento", 1, medicamentos.size());

        // Verificamos que los datos del medicamento son correctos
        Medicamento medicamentoRecuperado = (Medicamento) medicamentos.get(0);
        assertEquals("Valium", medicamentoRecuperado.getNombre());
        assertEquals("500 mg", medicamentoRecuperado.getDosis());
    }

    // Método para mostrar los datos del medicamento creado
    public void mostrarMedicacion() {
        System.out.println("===========================================");
        System.out.println("Creación de medicamento:");
        System.out.println("Nombre medicamento: Valium");
        System.out.println("Dosis: 500 mg");
        System.out.println("===========================================");
    }


    // Tercer Test: Sistema de cifrado

    /**
     * Esta tercera prueba unitaria es para ver qe el sistema de cifrado funcione correctamente
     * Al igual que las últimas dos pruebas, se mostrará impreso en consola los datos fake/ mock en casod e que la prueba
     * de positiva.
     * De esta manera verificamos que el sistema de seguridad de la app sea funcional y protega los datos del usuario.
     */


    private String textoOriginal;
    private String textoCifrado;
    private String textoDescifrado;

    @Test
    public void testCifradoDescifrado() {
        try {
            // Texto que vamos a cifrar
            textoOriginal = "Holis! Esto es un texto de prueba para cifrar";

            // Ciframos el texto
            textoCifrado = cifrado.encrypt(textoOriginal.getBytes(), secretKey);

            // Verificamos que el texto cifrado no es igual al original
            assertNotEquals("El texto cifrado debe ser diferente al original!",
                    textoOriginal, textoCifrado);

            // Desciframos el texto
            textoDescifrado = cifrado.decrypt(textoCifrado, secretKey);

            // Verificamos que el texto descifrado es igual al original
            assertEquals("El texto descifrado debe ser igual al original",
                    textoOriginal, textoDescifrado);

        } catch (Exception e) {
            fail("No debería haber excepciones en el proceso de cifrado/descifrado!!");
        }
    }


    // Método para mostrar los resultados de cifrado y descifrado
    private void mostrarResultadosCifradoDescifrado(String original, String cifrado, String descifrado) {
        System.out.println("=== Resultados de Cifrado y Descifrado ===");
        System.out.println("Texto Original: " + original);
        System.out.println("Texto Cifrado: " + cifrado);
        System.out.println("Texto Descifrado: " + descifrado);
        System.out.println("===========================================");
    }




    // CUARTO TEST: Gestión de citas médicas
    @Test
    public void testGestionCitasMedicas() {
        // Creamos una nueva cita médica
        CitaMedica cita = new CitaMedica();
        cita.setNombre("Dr. Test");
        cita.setFecha("2024-11-02");
        cita.setHora("10:30");
        cita.setDescripcion("Revisión general");

        // Añadimos la cita al usuario
        usuario.setCitaMedica(cita);

        // Verificamos que se añadió correctamente
        ArrayList citas = usuario.getAllCitas();
        assertTrue("La lista de citas no debería estar vacía", citas.size() > 0);
        assertEquals("Debería haber exactamente 1 cita", 1, citas.size());

        // Verificamos que los datos de la cita son correctos
        CitaMedica citaRecuperada = (CitaMedica) citas.get(0);
        assertEquals("Dr. Test", citaRecuperada.getNombre());
        assertEquals("2024-11-02", citaRecuperada.getFecha());
        assertEquals("10:30", citaRecuperada.getHora());
    }


    // Llama a los métodos de los test para ver los resultados por consola impresos
    @After
    public void imprimeConsola() {
        System.out.println("===========================================");
        mostrarCreaciónUsuario();
        mostrarMedicacion();
        mostrarResultadosCifradoDescifrado(textoOriginal, textoCifrado, textoDescifrado);
        System.out.println("===========================================");
    }

}