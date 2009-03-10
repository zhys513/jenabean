package example.thing;

import thewebsemantic.Thing;
import thewebsemantic.vocabulary.Rdfs;
import thewebsemantic.vocabulary.Sioc;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;

public class SiocWriteExample {
	private static final String uri9 = "http://johnbreslin.com/blog/category/semantic-web/";
	private static final String literal1 = "SIOC provides a unified vocabulary for content and interaction description: a semantic layer that can co-exist with existing discussion platforms.";
	private static final String uri8 = "http://johnbreslin.com/blog/index.php?sioc_type=comment&amp;sioc_id=123928";
	private static final String uri7 = "http://johnbreslin.com/blog/2006/09/07/creating-connections-between-discussion-clouds-with-sioc/#comment-123928";
	private static final String uri6 = "http://johnbreslin.com/blog/category/blogs/";
	private static final String uri4 = "http://johnbreslin.com/blog/index.php?sioc_type=user&amp;sioc_id=1";
	private static final String uri3 = "http://johnbreslin.com/blog/author/cloud/";
	private static final String uri2 = "http://johnbreslin.com/blog/index.php?sioc_type=site#weblog";
	private static final String uri1 = "http://johnbreslin.com/blog/2006/09/07/creating-connections-between-discussion-clouds-with-sioc/";

	public static void main(String[] args) {
		OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
		m.setNsPrefix("sioc", "http://rdfs.org/sioc/ns#");

		Thing thing = new Thing(m);
		thing.at(uri1).
			isa(Sioc.Post.class).
			has_container(thing.at(uri2)).
			has_creator(
			  thing.at(uri3).isa(Sioc.User.class).
			  seeAlso( thing.at(uri4))).
			content(literal1).
			topic(thing.at(uri6).as(Rdfs.class).label("blogs")).
			topic(thing.at(uri9).as(Rdfs.class).label("SemanticWeb")).
			has_reply(
			  thing.at(uri7).
			  isa(Sioc.Post.class).
			  seeAlso(thing.at(uri8)));
		
		// this intends to format the rdf as closely as possible
		// with the example being duplicated.
		RDFWriter w = m.getWriter("RDF/XML-ABBREV");		
		w.setProperty( "blockRules" , "" );
		w.write(m.getBaseModel(), System.out, null);

	}
}