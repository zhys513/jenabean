package test;

import thewebsemantic.Namespace;

@Namespace("http://example.org/")
public class Post extends Taggable {

	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
