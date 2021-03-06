package actions;

import java.io.BufferedReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.grupoatrium.modelo.Autor;
import com.grupoatrium.modelo.Libro;
import com.grupoatrium.negocio.GestionLibreria;

import actionforms.*;

public class BuscarAutorAction extends Action {
	

	@Override
	public ActionForward execute(ActionMapping mapping, 
								 ActionForm form, 
								 HttpServletRequest request,
								 HttpServletResponse response) throws Exception {
		
		int tiempoPeticion;
		
		// Se recoge el dato del filtro
		long ini = (long) request.getAttribute("inicioPeticion");
    	
    	// Se indica que el tipo de respuesta que se devolver� ser� c�digo HTML
    	// en formato UTF-8
    	response.setContentType("text/html;charset=UTF-8");
    	
    	// Se almacena la clave de la respuesta en un objeto BufferedReader   	
    	BufferedReader bf = request.getReader();
    	// Se convierte el objeto BufferedReader a String
    	String str = bf.readLine();
    	
    	// Variable auxiliar para el conteo de autores en la BDD
    	int n = 0;
    	
    	// Variables de tipo String auxiliares que almacenar�n el c�digo html
    	String combo = "";
    	String tablaAutor = "";
    	String tablaLibros = "";
    	String respuesta = "";
    	String separador = "#####"; // Separador

    	// Se crea un objeto de tipo GestionLibreria (fachada) para realizar
    	// las gestiones contra la BDD 
		GestionLibreria commBDD = new GestionLibreria();
		// Ejecutamos el m�todo consutarAutores() de la clase GestionLibreria
		// para optener una colecci�n de todos los objetos Autores (Hibernate
		// se encarga de la conversi�n registro / objeto). Almacenamos los
		// objetos Autor devueltos por la consulta en una variable de tipo List
		List<Autor> autores = commBDD.consultarAutores();
		
    	// Si la clave del objeto XMLHttpRerquest es igual a "inicio" hay que
		// crear el c�digo html para crear el combo y aprovechamos para
		// obtener los datos del primer autor de la lista
    	if (str.equals("inicio")) {

    		// Un combo se crea con las etiquetas
    		// <select>
    		//		<option> </option>
    		// </select>
    		combo += "<div id = \"divcombo\"><select id = \"combo\" name = \"Autores\" onchange=javascript:llamadaADatosAutores();>";
    		
    		// Recorremos todo el List autores, para obtener el nombre de todos
    		// los autores en la base de datos
    		for (Autor a: autores) {
    			String nombre = a.getNombre();
    			// A�adimos el nombre del autor al combo
    			combo = combo + "<option value = \"" + nombre + "\">" + nombre + "</option>";
    			
    			// Si adem�s es el primer nombre del List, consultamos el resto
    			// de datos de este autor para generar la web 
    			if (n == 0) {
    				// Utilizamos el objeto de la clase GestionLibreria creado
    				// anteriormente para obtener tambi�n los libros escritos
    				// por este autor gracias a su m�tedo
    				// consultarLibrosPorAutor(Autor). Almacenamos los objetos
    				// Libro en una variable List de nombre libros
    				List<Libro> libros = commBDD.consultarLibrosPorAutor(a);
    				
    				// Almacenamos la nacionalidad y el comentario de este
    				// autor
    				String nacionalidad = a.getNacionalidad();
    				String comentarios = a.getComentarios();
    				
    				// Generamos el c�digo html para crear la tabla con los
    				// datos obtenidos de este primer autor dde la lista
    				// Una tabla se genera con el c�digo html:
    				// <table>
    				// 		<tr> 			<- Esto es una fila
    				//				<td> 	<- Cada columna de la fila
    				//				<td>	<- Cada columna de la fila
    				//		</tr>
    				// </table>
    				// Incrustamos la tabla dentro de un DIV para poder
    				// darle formato con CSS m�s f�cilmente.
    				
    				tablaAutor += "<div id = \"datosAutor\">";
    				tablaAutor += "<table id = \"tablaAutor\" border>";
    	    		tablaAutor += "<caption>Datos del autor</caption>";
    	    		tablaAutor += "<tbody>";
    	    		tablaAutor += "<tr>";
    	    		tablaAutor += "<td>" + "Nombre" + "</td>";
    	    		tablaAutor += "<td>" + nombre + "</td>";
    	    		tablaAutor += "</tr>";
    	    		tablaAutor += "<tr>";
    	    		tablaAutor += "<td>" + "Nacionalidad" + "</td>";
    	    		tablaAutor += "<td>" + nacionalidad + "</td>";
    	    		tablaAutor += "</tr>";
    	    		tablaAutor += "<tr>";
    	    		tablaAutor += "<td>" + "Comentarios" + "</td>";
    	    		tablaAutor += "<td>" + comentarios + "</td>";
    	    		tablaAutor += "</tr>";
    	    		tablaAutor += "</tbody></table></div>";
    	    		
    	    		// Procedemos de igual manera para general el c�digo html
    	    		// de los libros escritos por el primer autor de la
    	    		// colecci�n. Igualmente, incrustamos la tabla dentro de 
    	    		// un DIV para poder darle formato con CSS m�s f�cilmente.
    	    		tablaLibros += "<div id = \"datosLibros\">";
    				tablaLibros += "<table id = \"tablaLibros\" border>";
    				tablaLibros += "<caption>T�tulos escritos por este autor</caption>";
    				tablaLibros += "<tbody>";
	    			for (Libro l: libros) {
	    				tablaLibros += "<tr><td>" + l.getTitulo() + "</td></tr>";
	    			}
    				tablaLibros += "</tbody></table></div>";
    			}
    			// Una vez obtenidos todos necesarios del primer autor de la
    			// coleccion, incrementamos el valor de referencia para que no
    			// se sobreescriban los datos obtenidos con el resto de autores
    			n++;
    		}
    		
    		// Cuando ya hemos obtenido el nombre de todos los autores de la
    		// BDD podemos cerrar el combo
    		combo += "</select></div>";
    		
    		// Creamos un �nico String sumando todo el c�digo html generado.
    		// Enmarcamos los datos de los autores dentro de un DIV para no
    		// tener que volver a cargar el combo en cada selecci�n
    		respuesta = combo + "<div id = \"datos\">" + tablaAutor + tablaLibros + "</div>";
			
    		// Se incluye el separador de datos
    		respuesta += separador;
    		
    		// obtenemos la hora actual y hallamos la diferencia
    		long fin = System.currentTimeMillis();
    		tiempoPeticion = (int) (fin - ini);
    		
    		// a�adimos el tiempo de la peticion a la respuesta como un
    		// script para luego posicionarlo donde queramos dentro de la
    		// p�gina
    		respuesta += tiempoPeticion;
    		
    		// Lo a�adimos al objeto HttpServletResponse
    		response.getWriter().write(respuesta);
    		
    		

    	
    	// Si la clave del objeto XMLHttpRerquest es igual al nombre de un
    	// autor no es necesario crear el combo y s�lo hay que obtener los
    	// datos necesarios de ese autor.
    	} else {
    		// Recorremos la colecci�n de autores de la base de datos buscando
    		// la coincidencia entre el valor de la clave del objeto
    		// XMLHttpRequest y el nombre del autor 
    		for (Autor a: autores) {
    			// Cuando se produce la coincidencia obtenemos sus datos
    			if (str.equals(a.getNombre())) {
    				
    				// Guardamos los datos del autor en Strings
    				String nombre = a.getNombre();
    				String nacionalidad = a.getNacionalidad();
    				String comentarios = a.getComentarios();
    				
    				// Creamos una colecci�n de objetos Libros escritos
    				// por el autor
    				List<Libro> libros = commBDD.consultarLibrosPorAutor(a);
    				
    				// Generamos el c�digo html para la tabla con los datos
    				// del autor
    				tablaAutor = "";
    				tablaAutor += "<div id = \"datosAutor\">";
    				tablaAutor += "<table id = \"tablaAutor\" border>";
    	    		tablaAutor += "<caption>Datos del autor</caption>";
    	    		tablaAutor += "<tbody>";
    	    		tablaAutor += "<tr>";
    	    		tablaAutor += "<td>" + "Nombre" + "</td>";
    	    		tablaAutor += "<td>" + nombre + "</td>";
    	    		tablaAutor += "</tr>";
    	    		tablaAutor += "<tr>";
    	    		tablaAutor += "<td>" + "Nacionalidad" + "</td>";
    	    		tablaAutor += "<td>" + nacionalidad + "</td>";
    	    		tablaAutor += "</tr>";
    	    		tablaAutor += "<tr>";
    	    		tablaAutor += "<td>" + "Comentarios" + "</td>";
    	    		tablaAutor += "<td>" + comentarios + "</td>";
    	    		tablaAutor += "</tr>";
    	    		tablaAutor += "</tbody></table></div>";
    	    		
    	    		// Generamos el c�digo html para la tabla con los t�tulos
    	    		// de los libros escritos por el autor
    	    		tablaLibros = "";
    	    		if (libros != null) {
        	    		tablaLibros += "<div id = \"datosLibros\">";
        				tablaLibros += "<table id = \"tablaLibros\" border>";
        				tablaLibros += "<caption>T�tulos escritos por este autor</caption>";
        				tablaLibros += "<tbody>";
		    				for (Libro l: libros) {
		    					tablaLibros += "<tr><td>" + l.getTitulo() + "</td></tr>";
		    				}
		    			tablaLibros += "</tbody></table></div>";
    				}
    	    		
    	    		// Una vez hemos encontrado la coincidencia en la colecci�n
    	    		// ya no es necesario seguir recorriendo la colecci�n.
    	    		break;
    			}
    		}

    		// Creamos un �nico String sumando todo el c�digo html generado.
    		respuesta = tablaAutor + tablaLibros;
    		
    		// Se incluye el separador de datos
    		respuesta += separador;
    		
    		// obtenemos la hora actual y hallamos la diferencia
    		long fin = System.currentTimeMillis();
    		tiempoPeticion = (int) (fin - ini);
    		
    		// a�adimos el tiempo de la peticion a la respuesta como un
    		// script para luego posicionarlo donde queramos dentro de la
    		// p�gina
    		respuesta += tiempoPeticion;
    		
    		response.getWriter().write(respuesta);
    	}
    	
    	return null;
	}
	
}
