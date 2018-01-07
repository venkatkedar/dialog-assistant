package edu.sbu.dialog.core.feature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.sbu.dialog.core.dialogmanager.Context;
import edu.sbu.dialog.core.utilities.Constants;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.Options;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.BasicDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * This class performs the action of extracting the POS from the user speech
 * @author Kedar
 *
 */
public class NLPBehaviour implements Behaviour<String,Map<String,List<String>>> {

	 
	List<String> requiredTagList=Arrays.asList(new String[] {"NN","CD","RB","DT"});
	public Map<String, List<String>> doAction(Context context,String input) {
		Map<String, List<String>> posMap=extractPOSMap(input);
		return posMap;
	}

	
	private Map<String,List<String>> extractPOSMap(String text) {
		MaxentTagger m = new MaxentTagger(Constants.TAGGER_PATH+"\\english-left3words-distsim.tagger"); //"src/main/resources/english-left3words-distsim.tagger");
		
		LexicalizedParser parser;
		StanfordCoreNLP nlp;
		//nlp.
		String[] tokens = m.tagTokenizedString(text).split(" ");
		List<String> nouns=new ArrayList<String>();
		
		Map<String,List<String>> outputMap=new HashMap<String, List<String>>();
		for(String token:tokens) {
			System.out.println(token);
			String words[]=token.split("_");
			if(words!=null&&words.length==2) {
				if(requiredTagList.contains(words[1])) {
					List<String> tokenList=outputMap.get(words[1]);
					if(tokenList==null){
						tokenList=new ArrayList<String>();
						outputMap.put(words[1],tokenList);
					}
					tokenList.add(words[0]);
				}
			}
				
		}
		System.out.println(outputMap);
		return outputMap;
	}

	
	public void abc() {
		Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit,pos, lemma, ner, parse, dcoref");
	    //MaxentTagger.loadModel(Constants.TAGGER_PATH+"\\english-left3words-distsim.tagger");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

	    // read some text in the text variable
	    String text = "I ate grilled cheese sandwich at fine dine restaurant near my house"; // Add your text here!

	    // create an empty Annotation just with the given text
	    Annotation document = new Annotation(text);

	    // run all Annotators on this text
	    pipeline.annotate(document);

	    // these are all the sentences in this document
	    // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

	    for(CoreMap sentence: sentences) {
	      // traversing the words in the current sentence
	      // a CoreLabel is a CoreMap with additional token-specific methods
	      for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	        // this is the text of the token
	        String word = token.get(TextAnnotation.class);
	        // this is the POS tag of the token
	        String pos = token.get(PartOfSpeechAnnotation.class);
	        // this is the NER label of the token
	        String ne = token.get(NamedEntityTagAnnotation.class);       
	      }

	      // this is the parse tree of the current sentence
	      Tree tree = sentence.get(TreeAnnotation.class);
	      //edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation
	      // this is the Stanford dependency graph of the current sentence
	      SemanticGraph dependencies = sentence.get(CollapsedDependenciesAnnotation.class);
	      System.out.println(dependencies);
	      
	      
	    }

	    // This is the coreference link graph
	    // Each chain stores a set of mentions that link to each other,
	    // along with a method for getting the most representative mention
	    // Both sentence and token offsets start at 1!
	    Map<Integer, CorefChain> graph = 
	      document.get(CorefChainAnnotation.class); 
		
	}
	
	public void bcd() {
		//LexicalizedParser p=new LexicalizedParser("englishPCFG.ser.gz", new edu.stanford.nlp.parser.lexparser.Options());
		//LexicalizedParser.
	}
	
	public static void main(String[] args) {
		NLPBehaviour np=new NLPBehaviour();
		np.abc();
	}

}
