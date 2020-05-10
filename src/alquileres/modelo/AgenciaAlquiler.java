/* @author: Yumurdzhan Yalmaz*/
package alquileres.modelo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * La clase guarda en una colección List (un ArrayList) la flota de vehículos
 * que una agencia de alquiler posee
 * 
 * Los vehículos se modelan como un interface List que se instanciará como una
 * colección concreta ArrayList
 */
public class AgenciaAlquiler {
	public static final String FICHERO_ENTRADA = "flota.csv";
	public static final String FICHERO_SALIDA = "marcasmodelos.txt";
	private String nombre; // el nombre de la agencia
	private List<Vehiculo> flota; // la lista de vehículos

	/**
	 * Constructor
	 * 
	 * @param nombre el nombre de la agencia
	 */
	public AgenciaAlquiler(String nombre) {
		this.nombre = nombre;
		this.flota = new ArrayList<>();
	}

	/**
	 * añade un nuevo vehículo solo si no existe
	 * 
	 */
	public void addVehiculo(Vehiculo v) {
		if (!flota.contains(v)) {
			flota.add(v);
		} else {
			System.out.println("Vehiculo ya existente, no se ha podido añadir");
		}
	}

	/**
	 * Extrae los datos de una línea, crea y devuelve el vehículo correspondiente
	 * 
	 * Formato de la línea: C,matricula,marca,modelo,precio,plazas para coches
	 * F,matricula,marca,modelo,precio,volumen para furgonetas
	 * 
	 * 
	 * Asumimos todos los datos correctos. Puede haber espacios antes y después de
	 * cada dato
	 */
	private Vehiculo obtenerVehiculo(String linea) {
		
		String[] datos = linea.split(",");
		for (int i = 0; i < datos.length; i++) {
			datos[i] = datos[i].trim();
		}
		String matricula = datos[1].toUpperCase();
		String marca = datos[2].toUpperCase();
		String modelo = datos[3].toUpperCase();
		double precio = Double.parseDouble(datos[4]);
		
		if (datos[0].startsWith("C")) {
			int plazas = Integer.parseInt(datos[5]);
			Coche coche = new Coche(matricula, marca, modelo, precio, plazas);
			return coche;
			
		} else {
			double volumen = Double.parseDouble(datos[5]);
			Furgoneta furgoneta = new Furgoneta(matricula, marca, modelo, precio, volumen);
			return furgoneta;
		}
		
		
	}

	/**
	 * La clase Utilidades nos devuelve un array con las líneas de datos de la flota
	 * de vehículos
	 */
	public int cargarFlota() {
		int errorLinea = 0;
		BufferedReader br = null;
		try {
			File fil = new File(FICHERO_ENTRADA);
			br = new BufferedReader(new FileReader(fil));
			String linea = br.readLine();
			while (linea != null) {
				try {
					Vehiculo v = obtenerVehiculo(linea);
					addVehiculo(v);

				} 
				catch (IllegalFormatException ife) {
					System.out.println("Error en conversión de formato: " + ife.getMessage());
					errorLinea++;
				}
				linea = br.readLine();
			}
		} 
		catch (IOException ex) {
			System.out.println("Error al leer del fichero");
		} 
		finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
		return errorLinea;
	}

	/**
	 * Representación textual de la agencia
	 */
	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder(this.nombre + "\n");
		for (Vehiculo v : flota) {
			sb.append(v.toString() + "\n");
		}
		return sb.toString();

	}

	/**
	 * Busca todos los coches de la agencia Devuelve un String con esta información
	 * y lo que costaría alquilar cada coche el nº de días indicado *
	 * 
	 */
	public String buscarCoches(int dias) {

		String str = "";
		for (Vehiculo v : flota) {
			if (v instanceof Coche) {
				double costeAlquiler = v.calcularPrecioAlquiler(dias);
				str += v.toString() + "\nCoste alquiler " + dias + " días: " + costeAlquiler + "\n";
			}
		}
		return str;

	}

	/**
	 * Obtiene y devuelve una lista de coches con más de 4 plazas ordenada por
	 * matrícula - Hay que usar un iterador
	 * 
	 */
	public List<Coche> cochesOrdenadosMatricula() {

		ArrayList<Coche> coches = new ArrayList<>();
		Iterator<Vehiculo> it = flota.iterator();
		while (it.hasNext()) {
			Vehiculo v = it.next();
			if (v instanceof Coche) {
				if (((Coche) v).getPlazas() > 4) {

					coches.add((Coche) v);
				}

			}

		}
		coches.sort(Comparator.naturalOrder());
		return coches;
	}

	/**
	 * Devuelve la relación de todas las furgonetas ordenadas de mayor a menor
	 * volumen de carga
	 * 
	 */
	public List<Furgoneta> furgonetasOrdenadasPorVolumen() {
		int i = 0;
		List<Furgoneta> furgo = new ArrayList<>();
		Iterator<Vehiculo> it = flota.iterator();
		while (it.hasNext()) {
			Vehiculo v = it.next();
			if (v instanceof Furgoneta) {
				furgo.add(i, (Furgoneta) v);
				i++;

			}
		}
		furgo.sort(new Comparator<Furgoneta>() {
			public int compare(Furgoneta f1, Furgoneta f2) {
				return Double.compare(f2.getVolumen(), f1.getVolumen());
			}
		});
		return furgo;

	}

	/**
	 * Genera y devuelve un map con las marcas (importa el orden) de todos los
	 * vehículos que hay en la agencia como claves y un conjunto (importa el orden)
	 * de los modelos en cada marca como valor asociado
	 */
	public Map<String, Set<String>> marcasConModelos() {
		TreeMap<String, Set<String>> marcasConModelos = new TreeMap<>();
		Iterator<Vehiculo> it = flota.iterator();
		while (it.hasNext()) {
			Vehiculo vehi = it.next();
			String marca = vehi.getMarca();
			String modelo = vehi.getModelo();
			if (marcasConModelos.get(marca) == null) {
				TreeSet<String> nombres = new TreeSet<>();
				nombres.add(modelo);
				marcasConModelos.put(marca, nombres);
			} else {
				marcasConModelos.get(marca).add(modelo);
			}
		}
		return marcasConModelos;
	}

	public void guardarMarcasModelos() throws IOException {
		PrintWriter pw = null;
		try {
			File f = new File(FICHERO_SALIDA);
			pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));
			Map<String, Set<String>> marcasModelos = marcasConModelos();
			for (Map.Entry<String, Set<String>> entrada : marcasModelos.entrySet()) {
				pw.append("Marca: " + entrada.getKey() + "\n\tModelos : " + entrada.getValue());
			}
		} finally {
			pw.close();
		}

	}

}
