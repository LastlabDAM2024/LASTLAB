package es.ifp.labsalut.seguridad;

import java.math.BigInteger;
import java.security.SecureRandom;

public class CifradoRSA {

    private BigInteger p;
    private BigInteger q;
    private BigInteger n;
    private BigInteger euclidesN;
    private BigInteger e;
    private BigInteger d;
    private SecureRandom aleatorio;
    private SecureRandom aleatorio1;
    private int bits;


    public CifradoRSA() {
        e = BigInteger.ZERO;
        n = BigInteger.ZERO;
    }

    public CifradoRSA(int bits) {
        this.bits = bits / 2;
        calcularPyQ();
        calcularN();
        calcularEuclidesN();
        calcularE(bits);
        calcularD();
    }

    private static BigInteger intABig(int x) {
        return BigInteger.valueOf(x);
    }

    // Calculamos los dos numeros primos p y q:
    private void calcularPyQ() {
        boolean esPrimo = false;
        while (!esPrimo) {
            aleatorio = new SecureRandom();
            p = BigInteger.probablePrime(bits, aleatorio);
            if (p.isProbablePrime(10)) {
                esPrimo = true;
            }
        }
        esPrimo = false;
        while (!esPrimo) {
            aleatorio1 = new SecureRandom();
            q = BigInteger.probablePrime(bits, aleatorio1);
            if (!q.equals(p)) {
                esPrimo = q.isProbablePrime(10);
            }
        }
    }

    private void calcularN() {
        setN(p.multiply(q));
    }

    private void calcularEuclidesN() {
        euclidesN = p.subtract(intABig(1)).multiply(q.subtract(intABig(1)));
    }

    private void calcularE(int x) {
        aleatorio1 = new SecureRandom();
        BigInteger i = BigInteger.probablePrime(x, aleatorio1);
        while (i.gcd(euclidesN).compareTo(intABig(1)) != 0) {
            i = i.nextProbablePrime();
        }
        setE(i);
    }

    private void calcularD() {
        boolean igual = true;
        while (igual) {
            d = getE().modInverse(euclidesN);
            if (!d.equals(e)) {
                igual = false;
            } else {
                calcularE(bits * 2);
            }
        }
    }


    public BigInteger cifrar(BigInteger x) {
        return x.modPow(getE(), getN());
    }

    public BigInteger descifrar(BigInteger y) {
        return y.modPow(d, getN());
    }

    public String convertirAString(BigInteger x) {
        String retorno = new String(x.toByteArray());
        return retorno;
    }

    public BigInteger convertirString(String x) {
        byte[] a;
        BigInteger retorno;
        if (x.getBytes().length * 8 > getN().bitLength()) {
            char[] devuelto = new char[getN().bitLength() / 8];
            x.getChars(0, getN().bitLength() / 8, devuelto, 0);
            String s = new String(devuelto);
            a = s.getBytes();
            retorno = new BigInteger(a);
        } else {
            a = x.getBytes();
            retorno = new BigInteger(a);
        }
        return retorno;
    }

    /**
     * @return the n
     */
    public BigInteger getN() {
        return n;
    }

    /**
     * @return the e
     */
    public BigInteger getE() {
        return e;
    }

    /**
     * @param n the n to set
     */
    public void setN(BigInteger n) {
        this.n = n;
    }

    /**
     * @param e the e to set
     */
    public void setE(BigInteger e) {
        this.e = e;
    }

}