package com.n0g4y0.amazonviewer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.anncode.amazonviewer.model.Movie;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.util.ResultSetUtil;
import com.n0g4y0.amazonviewer.db.IDBConnection;

import static com.n0g4y0.amazonviewer.db.DataBase.*;

public interface MovieDao extends IDBConnection {
	
	// mediante este metodo, agregaremos informacion a la BD, mediante el INSERT de SQL.
	/*
	 * Este metodo,tambien aplicaria cuando queremos actualizar la BD.
	 * 
	 * */
	
	default Movie setMovieViewed(Movie movie) {
		
		try(Connection connection = connectToDB()) {
			
			java.sql.Statement statement = connection.createStatement();
			String query = "INSERT INTO " + TVIEWED+
							" ("+TVIEWED_IDMATERIAL+", "+TVIEWED_IDELEMENT+", "+TVIEWED_IDUSUARIO+")" +
							" VALUES("+ID_MATERIALS[0]+", "+movie.getId()+", "+TUSER_IDUSUARIO+")";
			
			// se pone mayor a cero, para comprobar si se afecto tuplas de datos.
			
			if (statement.executeUpdate(query) > 0) {
				
				System.out.println("Se marco en Visto..)");	
				
			}
			
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		return movie;
	}
	
	
	default ArrayList<Movie> read(){
		
		ArrayList<Movie> movies = new ArrayList<>();
		
		try (Connection connection = connectToDB()){
			String query = "SELECT * FROM " + TMOVIE;
			
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				
				// se van creando objetos de tipo MOVIE, siempre y cuando se hayan encontrado resultados luego de la consulta.
				
				Movie movie =  new Movie(
						rs.getString(TMOVIE_TITLE),
						rs.getString(TMOVIE_GENRE),
						rs.getString(TMOVIE_CREATOR),
						Integer.valueOf(rs.getString(TMOVIE_DURATION)),
						Short.valueOf(rs.getString(TMOVIE_YEAR)));
				
				// se supone que se hace coincidir el ID del registro, con el ID del objeto creado.
				
				movie.setId(Integer.valueOf(rs.getString(TMOVIE_ID)));
				
				// la siguiente instruccion, modifica los valores de "VISTO" de los valores rescatados en un Arraylist, los cambios
				//no son persistentes. 
				
				movie.setViewed(getMovieViewed(preparedStatement,
						connection,
						Integer.valueOf(rs.getString(TMOVIE_ID))));
				
				movies.add(movie);
				
			}
			
		}catch (SQLException e) {
			// TODO: handle exception
		}
		
		return movies;
	}
	
	private boolean getMovieViewed(PreparedStatement preparedStatement, Connection connection, int id_movie){
	
		// este es un ejemplo de como se puede realizar consultas mas detalladas
		
		boolean viewed = false;
		String query = "SELECT * FROM " + TVIEWED +
				" WHERE "+ TVIEWED_IDMATERIAL+"= ?"+
				" AND " + TVIEWED_IDELEMENT + "= ?" + 
				" AND " + TVIEWED_IDUSUARIO + "= ? ";
		
		ResultSet rs = null;
		
		try {
			
			preparedStatement = connection.prepareStatement(query);
			
			// las siguientes intrucciones, reemplazar los caracteres "?" por orden de la cada QUERY de arriba.
			preparedStatement.setInt(1, ID_MATERIALS[0]);
			preparedStatement.setInt(2, id_movie);
			preparedStatement.setInt(3, TUSER_IDUSUARIO);
			
			rs = preparedStatement.executeQuery();
			
			viewed = rs.next();
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
			
			// imprimira el error, para el desarrollador
			e.printStackTrace();
		}
		
		
		return viewed;
	}
	
	default ArrayList<Integer> searchByDate(Date date){
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = df.format(date);
		
		
		ArrayList<Integer> buscados = new ArrayList<>();
		
		try (Connection connection = connectToDB()){
			String query = "SELECT * FROM " + TVIEWED+" WHERE fecha >= '"+dateString+" 00:00:00:' ";
			
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				
				
				buscados.add(Integer.valueOf(rs.getString(TVIEWED_IDELEMENT)));
				
			}
			
		}catch (SQLException e) {
			// TODO: handle exception
		}
		
		return buscados;
	}
	
}
