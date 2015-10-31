package MODELO;

import java.text.SimpleDateFormat;
import java.util.Date;

import BASEDEDATOS.BaseDeDatos;

public class DAOContacto {
	
	private EntityContacto entityContacto;
	
	private BaseDeDatos baseDeDatos;
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	public DAOContacto() {
		
		super();
	}
	
	public DAOContacto(EntityContacto entityContacto, BaseDeDatos baseDeDatos) {
	
		this.entityContacto = entityContacto;
		
		this.baseDeDatos = baseDeDatos;
	}
	
	public String selectTelefono(){
		String sql="select * from contacto where telefono like '%"+entityContacto.getTELEFONO()+"%'";
		return baseDeDatos.getTablaHTML(sql);
	}
	
	public String selectNOMBRE(){
		String sql="select * from contacto where NOMBRE like '%"+entityContacto.getNOMBRE()+"%'";
		return baseDeDatos.getTablaHTML(sql);
	
	}
	
	public String selectAPELLIDO(){
		String sql="select * from contacto where APELLIDO like '%"+entityContacto.getAPELLIDO()+"%'";
		return baseDeDatos.getTablaHTML(sql);
	}
	
	public String selectVINCULO(){
		String sql="select * from contacto where VINCULO like '%"+entityContacto.getVINCULO()+"%'";
		return baseDeDatos.getTablaHTML(sql);
}
	public String InsertarContaCTO(){
		String sql="INSERT INTO contacto (NOMBRE, APELLIDO, TELEFONO, FNACIMIENTO, VINCULO, IMAGEN, ID) "
				+ "VALUES ('"+entityContacto.getNOMBRE()+"', '"+entityContacto.getAPELLIDO()+"', '"
				+entityContacto.getTELEFONO()+"', '"+format.format(entityContacto.getFNACIMIENTO())+"', '"+entityContacto.getVINCULO()+
				"', '"+entityContacto.getIMAGEN()+"', "+entityContacto.getID()+")";
		 if(baseDeDatos.ejecutarActualizacionSQL(sql))
			 return "Si pudo insertar";
	     else
	         return "No pudo insertar";

	}
	
	public String EliminarContacto(){
		String sql="DELETE FROM contacto WHERE NOMBRE='"+entityContacto.getNOMBRE()+"' or APELLIDO='"+entityContacto.getAPELLIDO()+"'or TELEFONO= '"
				+entityContacto.getTELEFONO()+"'or FNACIMIENTO= '"+format.format(entityContacto.getFNACIMIENTO()) +
				"'or ID='"+entityContacto.getID()+")";
		 if(baseDeDatos.ejecutarActualizacionSQL(sql))
			 return "Si pudo ELIMINAR";
	     else
	         return "No pudo ELIMINAR";

	}
	
	public String ModificarContacto(){
		
		String sql = "UPDATE contacto SET NOMBRE ='"+entityContacto.getNOMBRE()+"', APELLIDO ='"+entityContacto.getAPELLIDO()+"', TELEFONO='"+entityContacto.getTELEFONO()+"', FNACIMIENTO='"+format.format(entityContacto.getFNACIMIENTO())+
				"' WHERE  ID="+entityContacto.getID();
		 if(baseDeDatos.ejecutarActualizacionSQL(sql))
			 return "Si pudo MODEFICAR";
	     else
	         return "No pudo MODIFICAR";
		
	}

	
}

