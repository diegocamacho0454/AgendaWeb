package MODELO;

import java.util.Date;

public class EntityContacto {

	private String NOMBRE,APELLIDO,TELEFONO,VINCULO,IMAGEN;
	private Date FNACIMIENTO;
	private int ID;
	public String getNOMBRE() {
		return NOMBRE;
	}
	public void setNOMBRE(String nOMBRE) {
		NOMBRE = nOMBRE;
	}
	public String getAPELLIDO() {
		return APELLIDO;
	}
	public void setAPELLIDO(String aPELLIDO) {
		APELLIDO = aPELLIDO;
	}
	public String getTELEFONO() {
		return TELEFONO;
	}
	public void setTELEFONO(String tELEFONO) {
		TELEFONO = tELEFONO;
	}
	public String getVINCULO() {
		return VINCULO;
	}
	public void setVINCULO(String vINCULO) {
		VINCULO = vINCULO;
	}
	public String getIMAGEN() {
		return IMAGEN;
	}
	public void setIMAGEN(String iMAGEN) {
		IMAGEN = iMAGEN;
	}
	public Date getFNACIMIENTO() {
		return FNACIMIENTO;
	}
	public void setFNACIMIENTO(Date fNACIMIENTO) {
		FNACIMIENTO = fNACIMIENTO;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public EntityContacto() {
		super();
	}
	public EntityContacto(String nOMBRE, String aPELLIDO, String tELEFONO, String vINCULO, String iMAGEN,
			Date fNACIMIENTO, int iD) {
		
		NOMBRE = nOMBRE;
		APELLIDO = aPELLIDO;
		TELEFONO = tELEFONO;
		VINCULO = vINCULO;
		IMAGEN = iMAGEN;
		FNACIMIENTO = fNACIMIENTO;
		ID = iD;
	}
	@Override
	public String toString() {
		return "EntityContacto [NOMBRE=" + NOMBRE + ", APELLIDO=" + APELLIDO + ", TELEFONO=" + TELEFONO + ", VINCULO="
				+ VINCULO + ", IMAGEN=" + IMAGEN + ", FNACIMIENTO=" + FNACIMIENTO + ", ID=" + ID + "]";
	}
	
	
	
}
