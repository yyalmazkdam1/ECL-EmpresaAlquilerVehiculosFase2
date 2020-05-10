/* @author: Yumurdzhan Yalmaz;*/
package alquileres.modelo;

/**
 * Un coche es un vehículo que añade un nº de plazas
 * 
 * El coste final de alquiler depende no solo del nº de días de alquiler 
 * sino también del nº de plazas. Si el nº de plazas es > 4 se añaden 5€ más por día
 */
public class Coche extends Vehiculo {
	private int plazas;

	public Coche(String matricula, String marca, String modelo, double precioDia, int plazas) {
		super(matricula, marca, modelo, precioDia);
		this.plazas = plazas;
		// TODO Auto-generated constructor stub
	}

	public int getPlazas() {
		return plazas;
	}
	public double calcularPrecioAlquiler(int dias) {
		double total = dias * 5;
		if (plazas > 4) {
			return super.calcularPrecioAlquiler(dias) + total;	
		}
		return super.calcularPrecioAlquiler(dias);
		
	}
	
	public String toString() {
		return getClass().getSimpleName() + super.toString() + " | " + "Plazas: " + getPlazas();
		
	}
}


