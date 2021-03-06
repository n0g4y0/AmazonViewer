package com.anncode.amazonviewer.model;

import java.util.ArrayList;
import java.util.Date;

import com.n0g4y0.amazonviewer.dao.MovieDao;


/**
 * Hereda de {@link Film}
 * implementa de {@link IVisualizable}
 * */

public class Movie extends Film implements IVisualizable ,MovieDao{
	
	private int id;
	private int timeViewed;
	
	
	public Movie(){
		
	}
	
	public Movie(String title, String genre, String creator, int duration, short year) {
		super(title, genre, creator, duration);
		setYear(year);
	}

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public int getTimeViewed() {
		return timeViewed;
	}
	public void setTimeViewed(int timeViewed) {
		this.timeViewed = timeViewed;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return  "\n :: MOVIE ::" + 
				"\n Title: " + getTitle() +
				"\n Genero: " + getGenre() + 
				"\n Year: " + getYear() + 
				"\n Creator: " + getCreator() +
				"\n Duration: " + getDuration();
	}
	

	/**
	 * {@inheritDoc}
	 * */

	@Override
	public Date startToSee(Date dateI) {
		// TODO Auto-generated method stub
		return dateI;
	}


	/**
	 * {@inheritDoc}
	 * */
	
	@Override
	public void stopToSee(Date dateI, Date dateF) {
		// TODO Auto-generated method stub
		
		if (dateF.getTime() > dateI.getTime()) {
			setTimeViewed((int)(dateF.getTime() - dateI.getTime()));
		}else {
			setTimeViewed(0);
		}
		
		
	}
	
	public static ArrayList<Movie> makeMoviesList() {
		
		Movie movie = new Movie();
		
		return movie.read();
	}


	/**
	 * {@inheritDoc}
	 * */
	
	@Override
	public void view() {
		
		setViewed(true);
		
		// aqui se agregan algunas instrucciones, para llamar a nuestro objeto e insertar datos en la BD.
		Movie movie = new Movie();
		// se agrega todo el objeto actual de la clase.
		movie.setMovieViewed(this);
		
		
		Date dateI = startToSee(new Date());
		
		
		
		for (int i = 0; i < 100000; i++) {
			System.out.println("..........");
		}
		
		//Termine de verla
		stopToSee(dateI, new Date());
		System.out.println();
		System.out.println("Viste: " + toString());
		System.out.println("Por: " + getTimeViewed() + " milisegundos");

		
	}
	
		public static ArrayList<Integer> viewByDate(Date date) {
		
			Movie movie = new Movie();
			
			return movie.searchByDate(date);
		
	}
	
}







