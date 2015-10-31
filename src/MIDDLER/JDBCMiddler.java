package MIDDLER;
import java.sql.*;
import java.util.Iterator;
import javax.naming.*;
import javax.sql.*;
import java.util.ArrayList;

/**
 *<P align='justify'>
 *Un <I>objeto</I> <B>JDBCMiddler</B> permite abstraer cualquier
 *conexi�n JDBC.
 *La conexion a una base de datos con JDBC requiere dos pasos
 *fundamentales:<BR>
 *1. Registrar el controlador<BR>
 *2. Establecer la conexion<BR>
 *Para esto es necesario tener los controladores,
 *la URL de recurso de base de datos,
 *el usuario y su clave.<BR>
 *
 *Esta clase permite encapsular todo el trabajo y la informacion
 *de una Base de Datos, en un unico objeto dentro de una aplicacion.
 *</P>
*/
public class JDBCMiddler{
	
	/**
	 *Una cadena con la ubicacion del controlador JDBC, en la forma 
	 *<I>paquete.subpaquetes.DriverClass</I>
	 *Por defecto toma el valor sun.jdbc.odbc.JdbcOdbcDriver.
	*/
	protected String controlador = "sun.jdbc.odbc.JdbcOdbcDriver";
	
	/**Una cadena con la ubicacion del recurso de base de datos en la forma
	 *<I>protocolo:subprotocolo:nombrerecurso</I>. El protocolo por lo general
	 *es jdbc.fabricante, el subprotocolo depende del controlador e igualmente
	 *nombrederecurso.
	*/
	protected String url = "jdbc.odbc.default";
	
	/**Una referencia al objeto Connection de la base de datos*/
	protected Connection conexion;
	
	/**Una cadena con el nombre de usuario(Login)*/
	protected String usuario;
	
	/**Una cadena con la clave (password)*/
	protected String clave=new String();
	
	/**Una cadena SQL*/
	protected String SQL;
	
	/**
	 *Crea un objeto JDBCMiddler vacio sin parametros
	 *de conexion. Constructor por defecto.
	*/
	public JDBCMiddler() {
	 System.out.println("new EJB["+this.hashCode()+"]");
	}//Fin Constructor Default JDBCMiddler
	
	/**
	 *Crea un objeto JDBCMiddler, que encapsula toda la informacion
	 *y metodos de acceso a una base de datos dentro de una aplicacion
	 *en el contexto JDBC.
	 *@param	controlador		Una cadena con el controlador JDBC a emplear
	 *@param	url				Una cadena de conexion JDBC a la Base de Datos	 
	*/
	public JDBCMiddler(String controlador, String url) {
	  this.controlador = controlador;
	  this.url = url;
	}//Fin Constructor JDBCMiddler
	
	/**
	 *Crea un objeto JDBCMiddler, que encapsula toda la informacion
	 *y metodos de acceso a una base de datos dentro de una aplicacion
	 *en el contexto JDBC.
	 *@param	controlador		Una cadena con el controlador JDBC a emplear
	 *@param	url				Una cadena de conexion JDBC a la Base de Datos	 
	 *@param 	usuario			Una cadena con el login
	 *@param	clave			Una cadena con la clave de acceso
	*/
	public JDBCMiddler(String controlador, String url, String usuario, 
						String clave){
	 this.controlador = controlador;
	 this.url=url;
	 this.usuario=usuario;
	 this.clave=clave;
	}//Fin Constructor JDBCMiddler
	
	/**
	 *Averigua si la conexi�n con la Base de Datos est� disponible.
	 *@return	Regresa verdadero (true) si la conexi�n est� disponible.
	 *			La conexi�n est� disponible cuando conexi�n!=null y
	 * !conexi�n.isClosed()
	*/
	public boolean hayConexion(){
	 return this.conexion!=null;
	}//Fin hayConexi�n	
	
	/**
	 *Establece una conexion con la base de datos.
	 *Si existen parametros de conexion los usa
	 *y se conecta de la manera tradicional.
	 *@return	Regresa verdadero (true) si pudo establecer la conexion
	 *			de lo contrario regresa falso (false).
	 *@exception Lanza un error si algo extra�o sucede :)
	*/
	public boolean conectar() throws Exception{
		
	 if(!hayConexion())
	  return conectar(this.usuario, this.clave);
	 else
	  return true;
	}//Fin conectar
	
	/**
	 *Se conecta a un servicio JDBC usando java.naming.
	 *Los parametros de configuracion se manejan para
	 *el contexto de la aplicacion, permitiendo un 
	 *pool de conexiones persistentes disponibles
	 *para toda la aplicacion. Tomcat proporciona este
	 *servicio configurandolo en el archivo web.xml
	 *o server.xml
	 *@param	servicio	Una cadena como "java:comp/env/servicio"
	 *
	*/
	public boolean conectar(String servicio) throws Exception{
		
	 /*
	   *Para conectarse con Tomcat
	   *en el archivo de coniguracion se especifican
	   *los parametros de conexion.
	  */
      long t = System.currentTimeMillis();
      //Context es un objeto que encapsula el contexto de la aplicacion
      Context ctx = new InitialContext();
      //DataSource es el origen de datos, 
      //un servicio JDBC proporcionado mediante java naming
      //El nombre del servicio deberia ser recibido como
      //argumento
      DataSource ds = (DataSource)ctx.lookup(servicio);

      //Ahora si obtiene la conexion
      this.conexion = ds.getConnection();
 
      return this.conexion != null;
	}//Fin conectar

	/**
	 *Establece una conexion con la base de datos, usando el usuario
	 *y clave especificados.
	 *Si ya hay una conexi�n, esta es cerrada.
	 *@param	usuario		Una cadena con el nombre de usuario
	 *@param 	clave	Una cadena con la clave
	 *@return	Regresa verdadero (true) si pudo establecer la conexion
	 *			de lo contrario regresa falso (false).
	*/
	public boolean conectar(String usuario, String clave) throws Exception{
	 
	 //Registra el controlador de manera implicita
	 Class.forName(controlador).newInstance();
	 //Obtiene la conexion
	 System.err.println(url+","+usuario+","+clave);
	 this.conexion = DriverManager.getConnection(url,usuario,clave);
	 //Actualiza usuario y clave del middler
	 this.usuario = usuario;
	 this.clave = clave;
     return this.conexion != null;
	}//Fin conectar

	/**Cierra la conexion con la base de datos*/
	public void desconectar() throws Exception{
     if(this.hayConexion()){
       this.conexion.close();
       this.conexion=null;
      }
	}//Fin desconectar
	
	/**
	 *Ejecuta una sentencia SQL y regresa como resultado un objeto
	 *ResultSet
	 *@param	consultaSQL	Cadena que contiene una sentencia de 
	 *                      consulta SQL:
	 * 						SELECT listaCampos 
	 *						FROM listaTablas 
	 *						WHERE listaCondiciones
	 *@return	Regresa un objeto ResulSet con el resultado de la consulta
	*/
	public ResultSet ejecutarSQL(String consultaSQL) throws Exception{
     if(this.conectar()){
	   Statement sql= this.conexion.createStatement();
       return sql.executeQuery(consultaSQL);
     }
     else return null;
	}//Fin ejecutarSQL
    
	/**
	 *Ejecuta una sentencia SQL y regresa como resultado un objeto
	 *ResultSet. La Consulta requiere de parametros en tiempo de ejecuci�n.
	 *@param	consultaSQL	Cadena que contiene una sentencia de 
	 *                      consulta SQL:
	 * 						SELECT listaCampos FROM listaTablas 
	 *						WHERE listaCondiciones
	 *@param	parametros	Un Iterador de Parametros con los parametros 
	 *						de la consulta.
	 *@return	Regresa un objeto ResulSet con el resultado de la consulta
	 *@see piagev.accesoBD.Parametro
	*/
	public ResultSet ejecutarSQL(String consultaSQL, Iterator parametros) 
					throws Exception{
     if(this.conectar()){
   	   PreparedStatement sql= this.conexion.prepareStatement(consultaSQL);
   	   
   	   //System.err.println(consultaSQL+"-->");
   	   for(int i=1;parametros.hasNext();i++){
   	   	String parametro= parametros.next().toString();
   	   	sql.setString(i,parametro);
   	   	//System.err.println("param["+i+"]="+parametro);
   	   }
   	   	   	   	
   	   return  sql.executeQuery();
      }
     else return null;
	}//Fin ejecutarSQL
	
	/**
	 *Ejecuta una sentencia SQL de insercion
	 *La sentencia requiere de parametros en tiempo de ejecuci�n.
	 *@param	consultaSQL	Cadena que contiene una sentencia de 
	 *                      insercion SQL:
	 * 						INSERT into table values (valores)
	 *@param	parametros	Un Iterador de Parametros con los parametros de la 
	 *						insercion.
	 *@return	falso o verdadero si pudo insertar
	 *@see piagev.accesoBD.Parametro
	*/
	public boolean ejecutarActualizacionSQL(String comandoSQL, 
					Iterator parametros) throws Exception{
	 boolean ok;
     if(this.conectar()){
   	   //La consulta es preparada porque requiere de parametros
   	   //por ejemplo:
   	   //delete from producto where precio=?
   	   //insert into producto values (?,?,?,?,?)
   	   //los ? indican parametros ordenados por posicion
   	   PreparedStatement sql= this.conexion.prepareStatement(comandoSQL);
   	   
   	   //System.err.println(comandoSQL+"-->");
   	   for(int i=1;parametros.hasNext();i++){
   	   	String parametro= parametros.next().toString();
   	   	sql.setString(i,parametro);
   	   	//System.err.println("param["+i+"]="+parametro);
   	   }
   	   
   	   ok = sql.executeUpdate()!=0;
   	   
   	   //importante cerrar la conexion
   	   sql.close();
   	   sql=null;
   	   this.desconectar();
   	   
   	   return ok;
      }
     else return false;
	}//Fin ejecutarSQL
	
	
	
	public boolean ejecutarActualizacionSQL(String comandoSQL) throws Exception{
	 boolean ok;
     if(this.conectar()){
   	   //La consulta es preparada porque requiere de parametros
   	   //por ejemplo:
   	   //delete from producto where precio=?
   	   //insert into producto values (?,?,?,?,?)
   	   //los ? indican parametros ordenados por posicion
   	   PreparedStatement sql= this.conexion.prepareStatement(comandoSQL);
   	   
   	   //System.err.println(comandoSQL+"-->");
   	   /*for(int i=1;parametros.hasNext();i++){
   	   	String parametro= parametros.next().toString();
   	   	sql.setString(i,parametro);
   	   	//System.err.println("param["+i+"]="+parametro);
   	   }*/
   	   
   	   ok = sql.executeUpdate()!=0;
   	   
   	   //importante cerrar la conexion
   	   sql.close();
   	   sql=null;
   	   this.desconectar();
   	   
   	   return ok;
      }
     else return false;
	}//Fin ejecutarSQL
	
	
	
	
	
	
	
	
	
	/*-METODOS DE ACCESO-*/	
	
	/**Metodo de acceso a la propiedad usuario*/
	public String getUsuario(){
	 return this.usuario;
	}//fin getUsuario
	
	/**Metodo de acceso a la propiedad clave*/
	public String getClave(){
	 return this.clave;
	}//fin getClave
	
	/**Metodo de acceso a la propiedad url*/
	public String getUrl(){
	 return this.url;
	}//fin getUrl
	
	/**Metodo de acceso a la propiedad controlador*/
	public String getControlador(){
	 return this.controlador;
	}//fin getControlador
	
	/*-METODOS DE MODIFICACION-*/
	
	/**Metodo de modificacion a la propiedad usuario*/
	public void setUsuario(String usuario){
	 this.usuario=usuario;
	}//fin setUsuario
	
	/**Metodo de modificacion a la propiedad clave*/
	public void setClave(String clave){
	 this.clave = clave;
	}//fin setClave
	
	/**Metodo de modificacion a la propiedad url*/
	public void setUrl(String url){
	 this.url=url;
	}//fin setUrl
	
	/**Metodo de modificacion a la propiedad controlador*/
	public void setControlador(String controlador){
	 this.controlador = controlador;
	}//fin setControlador
	
	/**Metodo de modificacion a la propiedad consulta*/
	public void setSQL(String SQL){
	 this.SQL = SQL;
	}//fin setSQL
	
	/**EJECUTA UNA CONSULTA Y GENERA LA TABLA HTML*/
    public String getHTML(String SQL) throws Exception{
     StringBuffer html = new StringBuffer();
     html.append("<table id=\"sample-table-2\" class=\"table table-striped table-bordered table-hover\">");
     
     if(conectar()){
            html.append("<thead>");
            ResultSet rs = ejecutarSQL(SQL);
            ResultSetMetaData rsm = rs.getMetaData();
            html.append("<tr>\n");
            for(int i=1;i<=rsm.getColumnCount();i++){
                html.append("<th>"+rsm.getColumnName(i)+"</th>\n");  
            }
            html.append("</tr></thead>\n<tbody> ");
             while(rs.next()){
                    html.append("<tr>");        
                    for(int i=1;i<=rsm.getColumnCount();i++){    
                    	try{
                    		html.append("<td class=\"td-actions\">"+rs.getString(i).trim()+"</td>\n");
                    	}catch(Exception e){
                    		html.append("<td class=\"td-actions\">null</td>\n");
                    	}
                    }
                    html.append("</tr>\n");
             }	 
             desconectar();
	 }
     
     html.append("</tbody>");
     html.append("</table>");
     return html.toString();
    }//Fin getHTML
    
    public String getHTMLSimple(String SQL) throws Exception{
     StringBuffer html = new StringBuffer();
     html.append("<table id=\"sample-table-1\" class=\"table table-striped table-bordered table-hover\">");
     
     if(conectar()){
            html.append("<thead>");
            ResultSet rs = ejecutarSQL(SQL);
            ResultSetMetaData rsm = rs.getMetaData();
            html.append("<tr><th colspan='"+rsm.getColumnCount()+"'><img src=\"../images/plogo.jpg\" width='50%'></tr>\n");
            if(rs.next())
                html.append("<tr><th colspan='"+rsm.getColumnCount()+"'>Grupos</tr>\n");
            rs = ejecutarSQL(SQL);
            html.append("</thead>\n<tbody> ");
            String grupo="";
             while(rs.next()){
                    html.append("<tr>");        
                    for(int i=2;i<=rsm.getColumnCount();i++){ 
                        if(i==2 && !rs.getString(1).trim().equals(grupo))
                        {
                            grupo=rs.getString(1).trim();
                            html.append("<tr><th colspan='"+rsm.getColumnCount()+"'>Grupo: "+grupo+"</tr><tr>\n");
                            for(int j=2;j<=rsm.getColumnCount();j++){
                                html.append("<th>"+rsm.getColumnName(j)+"</th>\n");  
                            }
                            html.append("</tr>\n");
                        }
                        html.append("<td class=\"td-actions\">"+rs.getString(i).trim()+"</td>\n");
                    }
                    html.append("</tr>\n");
             }	 
             desconectar();
	 }
     
     html.append("</tbody>");
     html.append("</table>");
     return html.toString();
    }//Fin getHTML
    
    public ArrayList<String> getSQL(String SQL) throws Exception{
     StringBuffer html = new StringBuffer();
     ArrayList<String> v=new ArrayList<String>();
     
     
     
	 if(conectar()){
	  ResultSet rs = ejecutarSQL(SQL);
      ResultSetMetaData rsm = rs.getMetaData();
     
  
    while(rs.next()){
  String r="";
            for(int i=1;i<=rsm.getColumnCount();i++){
	   	r+=rs.getString(i)+"-";
	   }
       v.add(r);
	  }	 
	  desconectar();
	 }
     
     
     return v;
    }//Fin getHTML
    
    
    
    
    
    
	/**EJECUTA UNA CONSULTA Y GENERA XML*/
    public String getXML() throws Exception{
     StringBuffer xml = new StringBuffer();
     xml.append("<registros>");
     
	 if(conectar()){
	  ResultSet rs = ejecutarSQL(this.SQL);
	  while(rs.next()){
       ResultSetMetaData rsm = rs.getMetaData();
       xml.append("<registro>");
   	   for(int i=1;i<=rsm.getColumnCount();i++){
        xml.append("<"+rsm.getColumnName(i)+">");
	   	xml.append(rs.getString(i));
	   	xml.append("</"+rsm.getColumnName(i)+">");
	   }
       xml.append("</registro>");
	  }	 
	  desconectar();
	 }
     
     xml.append("</registros>");
     return xml.toString();
    }//Fin getXML
	    
	
	/**
	 *M�todo de prueba
	 *Recibe como argumento el nombre del archivo de 
	 *configuracion.
	 *Para usarlo:
	 *java JDBCMiddler archivo.conf
	*/
	public static void main(String args[]) throws Exception{
	 //Se recibe como argumento del main el archivo de 
	 //configuracion que contiene el driver
	 //la url, el login y la clave
	 String parametros[] = leerParametros(args[0]);
	 int n = 1;
	 JDBCMiddler middler = new JDBCMiddler(parametros[0],
	 									   parametros[1],
	 									   parametros[2],
	 									   parametros[3]);
	 if(middler.conectar()){
	  System.out.println(parametros[4]);
	  ResultSet rs = middler.ejecutarSQL(parametros[4]);
	  while(rs.next()){
	   ResultSetMetaData rsm = rs.getMetaData();
	   System.out.println("****************************");
	   System.out.println("Registro: "+(n++));
	   for(int i=1;i<=rsm.getColumnCount();i++){
	   	System.out.print(rsm.getColumnName(i)+": ");
	   	System.out.println(rs.getString(i));
	   }
	  }	 
	  middler.desconectar();
	  System.out.println("todo bien");
	 }
	}//fin main
	
	/**
	 *Servicio que permite Leer los parametros de entrada
    */   
    public static String [] leerParametros (String rutaArchivo) 
    					throws Exception{
  	 String [] parametros = new String[5];
  	 java.io.BufferedReader flujoE = new java.io.BufferedReader(
  	 								 new java.io.FileReader (rutaArchivo));
  	 parametros[0] = flujoE.readLine();
  	 parametros[1] = flujoE.readLine();
  	 parametros[2] = flujoE.readLine();
  	 parametros[3] = flujoE.readLine();
  	 parametros[4] = flujoE.readLine();
  	 flujoE.close();
  	 return parametros;
    }//fin leerParametros
	
    public String getHTMLOrden(String SQL) throws Exception{
     StringBuffer html = new StringBuffer();
     html.append("<table id=\"sample-table-2\" class=\"table table-striped table-bordered table-hover\">");
     
     if(conectar()){
            html.append("<thead>");
            ResultSet rs = ejecutarSQL(SQL);
            ResultSetMetaData rsm = rs.getMetaData();
            html.append("<tr>\n");
            for(int i=1;i<=rsm.getColumnCount();i++){
                if(i<7)
                    html.append("<th>"+rsm.getColumnName(i)+"</th>\n");  
            }
            html.append("<th>Procentaje</th>\n");
            
            html.append("</tr></thead>\n<tbody> ");
            
            int cantidad=0, total=0;
            String prioridad="";
             while(rs.next()){
                    html.append("<tr>");                       
                    for(int i=1;i<=rsm.getColumnCount();i++){   
                        if(i==5){
                            prioridad=rs.getString(i).trim();
                            if(prioridad.equals("C"))
                                html.append("<td class=\"td-actions\"><div class=\"span6\"><span class=\"label label-large label-important arrowed-in\">Cancelada</span>\n</p>\n</div><!--/span--></td>\n");
                            else if(prioridad.equals("P"))
                                html.append("<td class=\"td-actions\"><div class=\"span6\"><p>\n<span class=\"label label-large label-warning arrowed-in\">Pendiente</span>\n</p>\n</div><!--/span--></td>\n");
                            else 
                                html.append("<td class=\"td-actions\"><div class=\"span6\"><p>\n<span class=\"label label-large label-success arrowed-in\">Finalizada</span>\n</p>\n</div><!--/span--></td>\n");
                        }
                        else if(i==6){
                            prioridad=rs.getString(i).trim();
                            if(prioridad.equals("A"))
                                html.append("<td class=\"td-actions\"><div class=\"span6\"><span class=\"label label-large label-important arrowed-in\">Alta</span>\n</p>\n</div><!--/span--></td>\n");
                            else if(prioridad.equals("M"))
                                html.append("<td class=\"td-actions\"><div class=\"span6\"><p>\n<span class=\"label label-large label-warning arrowed-in\">Media</span>\n</p>\n</div><!--/span--></td>\n");
                            else 
                                html.append("<td class=\"td-actions\"><div class=\"span6\"><p>\n<span class=\"label label-large label-yellow arrowed-in\">Baja</span>\n</p>\n</div><!--/span--></td>\n");
                        }
                        else if(i==7){
                            cantidad=rs.getInt(i);
                            //html.append("<td class=\"td-actions\">"+cantidad+"</td>\n");
                        }
                        else if(i==8) {
                            total=rs.getInt(i);
                            //html.append("<td class=\"td-actions\">"+total+"</td>\n");
                        }
                        else
                            html.append("<td class=\"td-actions\">"+rs.getString(i).trim()+"</td>\n");
                    }
                    
                    if(cantidad>0 && cantidad==total)
                        html.append("<td class=\"td-actions\"><div class=\"progress progress-warning progress-small progress-striped active\" data-percent=\"100%\">\n<div class=\"bar\" style=\"width: 100%;\"></div>\n</div></td>\n");
                    else if(cantidad>0)
                        html.append("<td class=\"td-actions\"><div class=\"progress progress-warning progress-small progress-striped active\" data-percent=\""+(100/cantidad)*total+"%\">\n<div class=\"bar\" style=\"width: "+(100/cantidad)*total+"%;\"></div>\n</div></td>\n");                        
                    else
                        html.append("<td class=\"td-actions\"><div class=\"progress progress-warning progress-small progress-striped active\" data-percent=\"0%\">\n<div class=\"bar\" style=\"width: 0%;\"></div>\n</div></td>\n");
                    html.append("</tr>\n");
             }	 
             desconectar();
	 }
     
     html.append("</tbody>");
     html.append("</table>");
     return html.toString();
    }//Fin getHTML
    
}//Fin JDBCMiddler