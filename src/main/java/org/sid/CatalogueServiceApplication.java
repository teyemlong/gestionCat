package org.sid;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.sid.dao.CategoryRepository;
import org.sid.dao.ProductRepository;
import org.sid.entities.Category;
import org.sid.entities.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CatalogueServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogueServiceApplication.class, args);
	}
	@Bean
	CommandLineRunner start(CategoryRepository c,ProductRepository p){
		return arg->{
			c.deleteAll();
			Stream.of("C1 Ordianateurs","C2 Imprimante").forEach(c1->c.save(new Category(c1.split(" ")[0], c1.split(" ")[1], new ArrayList<>())));	
			c.findAll().forEach(System.out::println);
			
			p.deleteAll();
			Category c1= c.findById("C1").get();
			Stream.of("P1","P2","P3","P4").forEach(p1->p.save(new Product(null, p1, Math.random()*1000, c1)));
			Category c2= c.findById("C2").get();
			Stream.of("P5","P6","P7","P8").forEach(p1->p.save(new Product(null, p1, Math.random()*1000, c2)));
			p.findAll().forEach(System.out::println);
		};
	}

}

