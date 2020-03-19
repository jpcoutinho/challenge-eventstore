package net.intelie.challenges;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


public class Teste {
	
	/*
	public static DefaultEventStore tabelinha = new DefaultEventStore();
	public static DefaultEventIterator tabelinhaIter;
	//*/
	
	///*
	public static FasterEventStore tabelinha = new FasterEventStore();
	public static FasterEventIterator tabelinhaIter;
	//*/
	
	public static void main(String[] args) throws IOException {
		
		long sstartTime = System.nanoTime();
		
		String type = "87439";
		long startTime = -13000000;
		long endTime = 13000000;
		
	
		leArquivo(tabelinha);
	
		//tabelinhaIter = (DefaultEventIterator) tabelinha.query(type, startTime, endTime);
		tabelinhaIter = (FasterEventIterator) tabelinha.query(type, startTime, endTime);

		
		long eendTime = System.nanoTime();
		long duration = (eendTime - sstartTime);  //divide by 1000000 to get milliseconds.
		System.out.println(Thread.currentThread().getName() +": "+ duration/1000000.0);
		
		while(true);
	}
	
	public static void leArquivo(EventStore tabelinha) throws IOException {
		
		  File file = new File("C:\\Users\\ohnituock\\Documents\\VisualStudioCode\\Projects\\TestesLua\\test.txt"); 
		  BufferedReader br = new BufferedReader(new FileReader(file)); 
		  
		  String chave;
		  long valor;
		  
		  String str; 
		  
		  while ((str = br.readLine()) != null) {
		    
			  StringTokenizer st1 = new StringTokenizer(str);
		  
			  chave = st1.nextToken();
			  valor = new Long(st1.nextToken());
			  
			  tabelinha.insert(new Event(chave, valor));
		  } 
		  
		  br.close();
	}
}
