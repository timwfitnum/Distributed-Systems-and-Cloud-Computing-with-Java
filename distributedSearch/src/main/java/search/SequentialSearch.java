package search;

import model.DocumentData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SequentialSearch {
    public static final String BOOKS_DIRECTORY = "./resources/books";
    public static final String SEARCH_QUERY_1 = "The best detective that catches many criminals using his deductive methods";
    public static final String SEARCH_QUERY_2 = "The girl that falls through a rabbit hole into a fantasy wonderland";
    public static final String SEARCH_QUERY_3 = "A war between Russian and France in the cold winter";


    public static void main(String[] args) throws FileNotFoundException {
        File documentsDirectory = new File(BOOKS_DIRECTORY);

        List<String> documents = Arrays.asList(documentsDirectory.list())
                .stream().map( documentName -> BOOKS_DIRECTORY + "/" + documentName)
                .collect(Collectors.toList());
        //Search Query needs to go inside argument below
        List<String> terms = TFIDF.getWordsFromLine(SEARCH_QUERY_2);
        findMostRelevantDocument(documents, terms);
    }

    private static void findMostRelevantDocument(List<String> documents, List<String> terms) throws FileNotFoundException {
        Map<String, DocumentData> documentsResults = new HashMap<>();
        for(String document: documents){
            BufferedReader bufferedReader = new BufferedReader(new FileReader(document));
            List<String> lines = bufferedReader.lines().collect(Collectors.toList());
            List<String> words = TFIDF.getWordsFromLines(lines);
            DocumentData documentData = TFIDF.createDocumentData(words, terms);
            documentsResults.put(document,documentData);
        }
        Map<Double, List<String>> documentByScore = TFIDF.getDocumentsSortedByScore(terms, documentsResults);
        printResults(documentByScore);

    }

    public static void printResults(Map<Double, List<String>> documentsByScore){
        for(Map.Entry<Double, List<String>> docScorePair : documentsByScore.entrySet()) {
            double score = docScorePair.getKey();
            for(String document: docScorePair.getValue()){
                System.out.println(String.format("Book : %s - score %f", document.split("/")[3],score));
            }
        }
    }
}
