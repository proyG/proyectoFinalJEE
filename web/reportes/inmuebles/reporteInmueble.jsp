<%-- 
    Document   : reporteTransaccion
    Created on : 1/06/2015, 12:44:37 PM
    Author     : Erik
--%>

<%@page import="java.sql.Connection"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.io.File"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="net.sf.jasperreports.engine.JasperRunManager"%>

<!DOCTYPE html>
<%
    /*Parametros para realizar la conexión*/
    Connection conn = null;
    try {
        Class.forName("org.postgresql.Driver").newInstance(); //se carga el driver
        conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/login", "postgres", "postgres");
        out.print("conexion OK");
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    /*Establecemos la ruta del reporte*/
    File reportFile = new File(application.getRealPath("reportes/inmuebles/reporteInmuebles.jasper"));
    /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
     cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
    Map parameters = new HashMap();
    /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
    byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conn);
    /*Indicamos que la respuesta va a ser en formato PDF*/
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition","attachment; filename=\"reporteInmuebles.pdf\";");
    
    response.setContentLength(bytes.length);
    ServletOutputStream ouputStream = response.getOutputStream();
    ouputStream.write(bytes, 0, bytes.length);
    /*Limpiamos y cerramos flujos de salida*/
    ouputStream.flush();
    ouputStream.close();

%>