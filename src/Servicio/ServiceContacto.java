package Servicio;

import java.util.Date;

import BASEDEDATOS.BaseDeDatos;
import MODELO.DAOContacto;
import MODELO.EntityContacto;

public class ServiceContacto {
	
	private BaseDeDatos BaseDeDatosdeServicios;
	private DAOContacto dao;
	
	

	public DAOContacto getInsertar() {
		return dao;
	}

	public void setInsertar(DAOContacto insertar) {
		this.dao = insertar;
	}

	public BaseDeDatos getBaseDeDatosdeServicios() {
		return BaseDeDatosdeServicios;
	}

	public void setBaseDeDatosdeServicios(BaseDeDatos baseDeDatosdeServicios) {
		BaseDeDatosdeServicios = baseDeDatosdeServicios;
	}

	public ServiceContacto(BaseDeDatos baseDeDatosdeServicios) {
		
		BaseDeDatosdeServicios = baseDeDatosdeServicios;
	}
	

	public ServiceContacto(DAOContacto insertar) {
		
		this.dao = insertar;
	}
	


	public ServiceContacto() {
		
	}
	
    public String insertar(String nOMBRE, String aPELLIDO, String tELEFONO, String vINCULO, String iMAGEN, String fNACIMIENTO, String iD){ 
    
    	EntityContacto entityContactos= new EntityContacto (nOMBRE,aPELLIDO,tELEFONO,vINCULO,iMAGEN, new Date(fNACIMIENTO),Integer.parseInt(iD));
    	dao=new DAOContacto(entityContactos, BaseDeDatosdeServicios);
    	return dao.InsertarContaCTO();
     
    }
    
    public String Eliminar(String nOMBRE, String aPELLIDO, String tELEFONO, String vINCULO, String iMAGEN, String fNACIMIENTO, String iD){ 
        
    	EntityContacto entityContactos= new EntityContacto (nOMBRE,aPELLIDO,tELEFONO,vINCULO,iMAGEN, new Date(fNACIMIENTO),Integer.parseInt(iD));
    	dao=new DAOContacto(entityContactos, BaseDeDatosdeServicios);
    	return dao.EliminarContacto();
    	
    }
    
  public String Modificar(String nOMBRE, String aPELLIDO, String tELEFONO, String vINCULO, String iMAGEN, String fNACIMIENTO, String iD){ 
        
    	EntityContacto entityContactos= new EntityContacto (nOMBRE,aPELLIDO,tELEFONO,vINCULO,iMAGEN, new Date(fNACIMIENTO),Integer.parseInt(iD));
    	dao=new DAOContacto(entityContactos, BaseDeDatosdeServicios);
    	return dao.ModificarContacto();
    	
  }
  
  public String ConsultarNombre(String nOMBRE){ 
      
  	EntityContacto entityContactos= new EntityContacto ();
  	entityContactos.setNOMBRE(nOMBRE);
  	dao=new DAOContacto(entityContactos, BaseDeDatosdeServicios);
  	return dao.selectNOMBRE();
  }  
  
  public String ConsultarApellido(String Apellido){ 
      
	  	EntityContacto entityContactos= new EntityContacto ();
	  	entityContactos.setAPELLIDO(Apellido);
	  	dao=new DAOContacto(entityContactos, BaseDeDatosdeServicios);
	  	return dao.selectAPELLIDO();
	  }
  public String ConsultarTelefono(String Telefono){ 
      
	  	EntityContacto entityContactos= new EntityContacto ();
	  	entityContactos.setTELEFONO(Telefono);
	  	dao=new DAOContacto(entityContactos, BaseDeDatosdeServicios);
	  	return dao.selectTelefono();
}
  
  public String ConsultarVinculo(String Vinculo){
	  
	  EntityContacto entityContactos= new EntityContacto ();
	  	entityContactos.setVINCULO(Vinculo);
	  	dao=new DAOContacto(entityContactos, BaseDeDatosdeServicios);
	  	return dao.selectVINCULO();
	  
	  
  }
}




