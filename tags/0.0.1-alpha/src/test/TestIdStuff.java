package test;

import java.util.Collection;

import org.junit.Test;
import static org.junit.Assert.*;

import thewebsemantic.Bean2RDF;
import thewebsemantic.NotFoundException;
import thewebsemantic.RDF2Bean;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class TestIdStuff {
	@Test
	public void testId() throws Exception {
		String id = "myuniqueid";
		TestIDBean bean = new TestIDBean(id);
		bean.setAddress("123 Oak Circle");
		bean.setAge(32);
		OntModel m = ModelFactory.createOntologyModel();
		Bean2RDF writer = new Bean2RDF(m);
		writer.save(bean);
		RDF2Bean reader = new RDF2Bean(m);
		TestIDBean bean2 = reader.load(TestIDBean.class, id);
		assertEquals("123 Oak Circle", bean2.getAddress());
		assertEquals(32, bean2.getAge());
	}

	@Test
	public void testConstructor()  throws Exception {
		OntModel m = ModelFactory.createOntologyModel();
		Bean2RDF writer = new Bean2RDF(m);
		writer.save(new Flute("a"));
		RDF2Bean reader = new RDF2Bean(m);
		Flute a = reader.load(Flute.class, "a");
		assertEquals("a", a.getMyId());
		assertEquals(1, a.i);
		// its uri should be http://package/classname/id
		Individual i = m.getIndividual("http://test/Flute/a");
		assertNotNull(i);
		writer.save(a);
	}

	@Test
	public void testHashCode() throws Exception  {
		OntModel m = ModelFactory.createOntologyModel();
		Bean2RDF writer = new Bean2RDF(m);
		Trumpet t = new Trumpet();
		t.setId("bach");
		writer.save(t);
		RDF2Bean reader = new RDF2Bean(m);
		Collection<Trumpet> trumpets = reader.load(Trumpet.class);
		assertEquals(1, trumpets.size());
		writer.save(t);
		trumpets = reader.load(Trumpet.class);
		assertEquals(1, trumpets.size());
		t.setId("conn");
		writer.save(t);
		trumpets = reader.load(Trumpet.class);
		assertEquals(2, trumpets.size());	
		
	}
	
	@Test
	public void testDelete()  {
		OntModel m = ModelFactory.createOntologyModel();
		Bean2RDF writer = new Bean2RDF(m);
		Trumpet t = new Trumpet();
		t.setId("conn");
		writer.save(t);
		RDF2Bean reader = new RDF2Bean(m);
		Collection<Trumpet> brass = reader.load(Trumpet.class);
		assertEquals(1, brass.size());
		
		writer.delete(t);
  	    brass = reader.load(Trumpet.class);
  	    assertEquals(0, brass.size());
  	    
  	    boolean found = false;
  	    try {
  	    	reader.load(Trumpet.class, "connss");
  	    } catch (NotFoundException e) {
  	    	found = true;
  	    }
  	    assertTrue(found);
		
		
	}
	
	
}