package TestDAO;

import java.text.SimpleDateFormat;
import java.util.Date;

import BASEDEDATOS.BaseDeDatos;
import MODELO.DAOContacto;
import MODELO.EntityContacto;

public class tesDAO {

	public static void main(String[] args) {
		
		 BaseDeDatos d=new BaseDeDatos();

		 EntityContacto entityContacto= new EntityContacto("Juan", "Camacho", "3212210978", "vINCULO", "iMAGEN", new Date(), 3);
		 DAOContacto dao=new DAOContacto(entityContacto, d); 
		 System.out.println(dao.InsertarContaCTO());
		 
		 System.out.println(dao.selectAPELLIDO());
		 System.out.println(dao.selectNOMBRE());
		 System.out.println(dao.selectTelefono());
		 System.out.println(dao.selectVINCULO());
		
		 
		 entityContacto.setAPELLIDO("arbelaez");
		 entityContacto.setNOMBRE("camila");
		 
		 System.out.println(dao.ModificarContacto());
		 
		 System.out.println(dao.EliminarContacto());
		 
	
	}

}