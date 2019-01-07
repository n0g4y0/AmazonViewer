package com.n0g4y0.amazonviewer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.anncode.amazonviewer.model.Movie;
import com.mysql.jdbc.util.ResultSetUtil;
import com.n0g4y0.amazonviewer.db.IDBConnection;

import static com.n0g4y0.amazonviewer.db.DataBase.*;

public interface MovieDao extends IDBConnection {
	
	default Movie setMovieViewed(Movie movie) {
		return movie;
	}
	
	
	default ArrayList<Movie> read(){
		ArrayList<Movie> movies = new ArrayList<>();
		
		try (Connection connection = connectToDB()){
			String query = "SELECT * FROM " + TMOVIE;
			
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
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
	
	
}
