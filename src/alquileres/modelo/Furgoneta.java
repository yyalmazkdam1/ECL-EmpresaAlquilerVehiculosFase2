/* @author: Yumurdzhan Yalmaz;*/
package alquileres.modelo;


/**
 * Una furgoneta es un vehículo que añade la característica del volumen de carga
 * (valor de tipo double)
 * 
 * El coste de alquiler de una furgoneta no solo depende del nº de días de
 * alquiler
 * Tendrá un incremento que dependerá de su volumen de carga: hasta 5 m3
 * exclusive ( metros cúbicos) de volumen el incremento sobre el precio
 * final será de 10€, entre 5 y 10 (inclusive) el incremento sobre el precio
 * final será de 15€, si volumen > 10 el incremento sobre el precio final será de
 * 25€
 * 
 */
public class Furgoneta extends Vehiculo{
	private double volumen;

	public Furgoneta(String matricula, String marca, String modelo, double precioDia,double volumen2) {
		super(matricula, marca, modelo, precioDia);
		this.volumen = volumen2;
		// TODO Auto-generated constructor stub
	}

	public double getVolumen() {
		return volumen;
	}
	public double calcularPrecioAlquiler(int dias) {
		double total = super.calcularPrecioAlquiler(dias);
		if (volumen < 5) {
			return total += 10;
		}
		if (volumen >= 5 & volumen <= 10) {
			return total += 15;
		}
		if (volumen > 10) {
			return total += 25;
		}
		return total;
		
	}
	
	
	
	public String toString() {
		return getClass().getSimpleName() + super.toString() + " | "  + "Volumen: " + getVolumen() + "(m3)";
	}
}

