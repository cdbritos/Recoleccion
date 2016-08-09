package recoleccion.modelo.jornada;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import recoleccion.modelo.data.Coordenada;
import recoleccion.modelo.data.TipoResiduo;
import recoleccion.modelo.domicilios.Domicilio;
import recoleccion.modelo.domicilios.Pedido;
import recoleccion.modelo.vehiculos.Camion;
import recoleccion.modelo.vehiculos.TipoVehiculo;
import recoleccion.modelo.vehiculos.Vehiculo;

public class GenerateFromExcel {

	private static HashMap<String, Double> VIVIENDAS_POR_MUNICIPO = new HashMap<>();
	
	static { 
		VIVIENDAS_POR_MUNICIPO.put("A", 13.56);
		VIVIENDAS_POR_MUNICIPO.put("B",	14.02);
		VIVIENDAS_POR_MUNICIPO.put("C",	12.16);
		VIVIENDAS_POR_MUNICIPO.put("CH",14.92);
		VIVIENDAS_POR_MUNICIPO.put("D",	12.18);
		VIVIENDAS_POR_MUNICIPO.put("E",	11.88);
		VIVIENDAS_POR_MUNICIPO.put("F",	11.06);
		VIVIENDAS_POR_MUNICIPO.put("G",	10.21);
	}
			
	public static void main(String[] args) throws Exception{
		
		int tamano_muestra = args.length > 0 ? Integer.valueOf(args[0]).intValue() : 300;
		
		if (tamano_muestra > 400)
			throw new Exception("TAMANO MUESTRA INCORRECTO");
		
		FileInputStream file = new FileInputStream(new File("Domicilios_Municipio.xls"));
		
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		List<Domicilio> domiciliosInstancia = new ArrayList<Domicilio>();
		
		for (String municipio : VIVIENDAS_POR_MUNICIPO.keySet()) {
			HSSFSheet sheet = workbook.getSheet(municipio);
			List<Domicilio> domiciliosMunicipio = getDomiciliosMunicipio(sheet,municipio);
			
			int cantAgregar = (int) (tamano_muestra * ((Double)VIVIENDAS_POR_MUNICIPO.get(municipio)) / 100);
			
			agregarDomiciliosInstancia(cantAgregar,domiciliosMunicipio,domiciliosInstancia);

		}
		
		/* 
		 * Aca ya tengo los domicilio de la instancia de tamano_muestra creados
		 * en forma proporcional a la cantidad de domicilios por municipio 
		 */
		
		int i = 0;
		while (i < domiciliosInstancia.size()){
			Domicilio d = domiciliosInstancia.get(i);
			List<Pedido> pedidos = new ArrayList<Pedido>();
			
			for (TipoResiduo tr : TipoResiduo.values()) {
				if (new Random().nextBoolean()){
					long pedidoCant = (new Random()).nextInt(Vehiculo.getRandomCapacidadTope());
					if (pedidoCant > 0)
						pedidos.add(new Pedido(tr,pedidoCant));
				}
			}
			
			if (CollectionUtils.isNotEmpty(pedidos)){
				d.setPedidos(pedidos);
				i++;
			}
		} 
		
		/* aca ya le agregue a cada domicilio algun pedido */
		for (Domicilio domicilio : domiciliosInstancia) {
			System.out.println(domicilio.toStringGenerador());
		}
		
	}

	private static void agregarDomiciliosInstancia(int cantAgregar, List<Domicilio> domiciliosMunicipio,List<Domicilio> domiciliosInstancia) {
		for (int i = cantAgregar; i>0; i--){
			Domicilio d = domiciliosMunicipio.get((new Random()).nextInt(domiciliosMunicipio.size()));
			domiciliosInstancia.add(d);
			domiciliosMunicipio.remove(d);
		}
		
	}

	private static List<Domicilio> getDomiciliosMunicipio(HSSFSheet sheet,String municipio) {
		List<Domicilio> domicilios = new ArrayList<Domicilio>();
		int rowIndex = 0;
		while (sheet.getRow(rowIndex) != null && !isCellEmpty(sheet.getRow(rowIndex).getCell(0))){
			double latitud = Double.valueOf(sheet.getRow(rowIndex).getCell(0).getStringCellValue());
			double longitud = Double.valueOf(sheet.getRow(rowIndex).getCell(1).getStringCellValue());
			Coordenada coord = new Coordenada(latitud, longitud);
			Domicilio d = new Domicilio(municipio, coord);
			domicilios.add(d);
			rowIndex++;
		}
		
		return domicilios;
	}
	
	public static boolean isCellEmpty(final HSSFCell cell) {
	    if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
	        return true;
	    }

	    if (cell.getCellType() == Cell.CELL_TYPE_STRING && cell.getStringCellValue().isEmpty()) {
	        return true;
	    }

	    return false;
	}
}
