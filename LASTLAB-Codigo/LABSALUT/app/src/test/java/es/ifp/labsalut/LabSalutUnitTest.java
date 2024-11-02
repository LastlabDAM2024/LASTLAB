package es.ifp.labsalut;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
@Config(sdk = 33)
public class LabSalutUnitTest {

 // Variables que se van a usar en los test siguientes

private  Usuario usuario;
private CifradoAES cifrado;
private SecretKey secretKey;

 // Método que se va a ejecutar antes de cada test


@Before
public void setUp(){
    usuario =  new Usuario("Test User", "1993-01-01", "nells@gmail.com", "123");
    cifrado = new CifradoAES();
    secretKey = cifrado.generarSecretKey("semillaPrueba");
}


// Primer test: Verificación de creación de usuario


    @Test
    public void testCreacionUsuario(){

        //Se verifica que el usuario sea creado correctamente

        assertNotNull("Hey, el usuario no tiene que ser nulo!", usuario);

        // Se verifica después que cada campo tiene el valor que se espera
        // Los datos usados son los mocks creados en el paso anterior!

        assertEquals("Test User", usuario.getNombre());
        assertEquals("nells@gmail.com", usuario.getEmail());
        assertEquals("1993-01-01", usuario.getFechaNacimiento());
        assertEquals("123", usuario.getContrasena());
    }


}