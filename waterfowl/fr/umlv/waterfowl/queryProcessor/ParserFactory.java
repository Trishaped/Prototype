package fr.umlv.waterfowl.queryProcessor;

public class ParserFactory {
	public static Parser getParser(String query) {
		return new ParserJena(query);
		}
}
