/**
 * 
 */
package org.sid.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Robert
 *
 */
@Document
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
	private String id;
	private String name;
	private double prix; 
	private Category category;

}
