package test.bean;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;

import thewebsemantic.Bean2RDF;
import thewebsemantic.NotFoundException;
import thewebsemantic.RDF2Bean;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class TestArrays {

	@Test public void basic() {
		ExampleArraysBean bean = new ExampleArraysBean();
		bean.setId("34fwe");
		Date d1 = new Date();
		Date d2 = new Date();
		Date d3 = new Date();
		Person p1 = new Person();
		p1.setFirstName("Amer");
		Person p2 = new Person();
		p2.setFirstName("Sam");
		Person p3 = new Person();
		p3.setFirstName("Surekha");
		bean.setAges(new int[] {1,2,3,4,5} );
		bean.setNames(new String[] {"bob", "sarah", "jimmy"});
		bean.setTimes(new Date[] {d1, d2, d3});
		bean.setPeople(new Person[] {p1,p2,p3});
		
		OntModel m = ModelFactory.createOntologyModel();	
		m.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
	
		Bean2RDF writer = new Bean2RDF(m);
		writer.saveDeep(bean);

	
		
		RDF2Bean reader = new RDF2Bean(m);
		Collection<ExampleArraysBean>  beans = reader.load(ExampleArraysBean.class);
		assertEquals(1, beans.size());
		
		ExampleArraysBean loadedBean = beans.iterator().next();
		assertEquals(0, loadedBean.getAges().length);
		reader.fill(loadedBean).with("ages");
		assertEquals(5, loadedBean.getAges().length);
		reader.fill(loadedBean).with("people");
		for (Person p: loadedBean.getPeople()) {
			System.out.println(p.getFirstName());
		}
		reader.fill(loadedBean).with("times");
		for (Date d : loadedBean.getTimes()) {
			System.out.println(d);
		}
		
		bean.getAges()[0] = 100;
		writer.save(bean);
	}
	
	@Test
	public void cycles() throws NotFoundException {
		OntModel model = ModelFactory.createOntologyModel();	
		model.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");	
		Bean2RDF writer = new Bean2RDF(model);
		RDF2Bean reader = new RDF2Bean(model);
		
		Molecule m = new Molecule();
		ArrayList<Molecule> molecules = new ArrayList<Molecule>();
		for(int i=0; i<10; i++)
			molecules.add(new Molecule());
		m.setNeighbors(molecules.toArray(new Molecule[0]));
		writer.saveDeep(m);
		Molecule actual = reader.load(Molecule.class, m.id(), new String[] {"neighbors"});
		assertEquals(10, actual.neighbors.length);

		molecules.remove(0);
		molecules.remove(0);
		molecules.remove(0);
		molecules.remove(0);
	    m.setNeighbors(molecules.toArray(new Molecule[0]));	    
		writer.saveDeep(m);	
		actual = reader.loadDeep(Molecule.class, m.id());
		assertEquals(6, actual.neighbors.length);
		
		for (int i=0;i<20;i++)
			molecules.add(new Molecule());
		molecules.add(null);
	    m.setNeighbors(molecules.toArray(new Molecule[0]));	    
	    writer.saveDeep(m);
		actual = reader.loadDeep(Molecule.class, m.id());
		assertEquals(26, actual.neighbors.length);
		
	}
}