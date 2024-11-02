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
        System.out.println("Creación de medicamento:");
        System.out.println("Nombre medicamento: Valium");
        System.out.println("Dosis: 500 mg");
    }




    // Llama a los métodos de los test para ver los resultados por consola impresos
    @After
    public void imprimeConsola() {
        mostrarCreaciónUsuario();
        mostrarMedicacion();
    }

}