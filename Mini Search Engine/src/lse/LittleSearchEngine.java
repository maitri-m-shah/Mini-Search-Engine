package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {

	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;

	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;

	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 *
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile)
	throws FileNotFoundException {
		//finsihed
	//if docFile is null throw an exception
		if(docFile==null) {
			throw new FileNotFoundException ();
		}

		//declare the hashmap
		HashMap<String, Occurrence> ans = new HashMap<String, Occurrence>(1000,2.0f); //values are given ^^

		//load through scanner and create loop
		Scanner sc = new Scanner(new File(docFile));
			while(sc.hasNext()) {
				//remove whitespace
				String trimkey = sc.next().trim();
				//check to see the word is a keyword
				String key = getKeyword(trimkey);

				if(key!=null) {
					if(ans.containsKey(key)) {
						//load up
						Occurrence occ = ans.get(key);
						occ.frequency++;
						ans.put(key,occ);
					}
					else {
						// Create a new entry
						ans.put(key, new Occurrence(docFile, 1));
					}

				}
			}
			sc.close();
			return ans;

		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code

	}

	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table.
	 * This is done by calling the insertLastOccurrence method.
	 *
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		//finished
		//iterate through the hashmap
		Iterator<String> i = kws.keySet().iterator();

		//go to the next value
		while(i.hasNext()) {
			String key = i.next();
			ArrayList<Occurrence> oc = new ArrayList<Occurrence>();

				if(keywordsIndex.containsKey(key)) {
					oc = keywordsIndex.get(key);
				}

				oc.add(kws.get(key)); //add to arraylist
				insertLastOccurrence(oc); //last occ
				keywordsIndex.put(key,oc); //put in hashmap
		}
	}

	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 *
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 *
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 *
	 * See assignment description for examples
	 *
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		//FINISHED BUT DOUBLE CHECK
		word=word.toLowerCase().trim(); //make everything lowercase and get rid of whitespace

		//Given an input word read from a document
		//it checks if the word is a keyword, and returns the keyword equivalent if it is.

		if (word.length() <= 0)
			return null;

		char punctuation [] = new char[] {'.','!','?',':',';',','}; //array filled with punctuation

		//if everything is a letter
	if(isAlpha(word)==true) {
	return word;
	}


		while(!Character.isLetter(word.charAt(word.length() - 1))) { //while the last thing is not a letter
			char lastChar = word.charAt(word.length() - 1);
			boolean value= false;

			//loop through punct array
			for(int i=0; i<punctuation.length; i++) {
				if (lastChar == punctuation[i]) { //if the lastchar is a punc
					value = true; //value is true
					break; //break out of the loop
				}
			}

			if(value==true){ //if we find a punct
				if (word.length() > 1) {
				word=word.substring(0, word.length()-1);
			}

				else {
					return null;
				}

			}
			if(value==false) {
				return null; //last not is punc, so this isn't a word we can work with
			}


		}
			for(int i=0; i<word.length();i++) { //at this point everything should be proper
				if(!Character.isLetter(word.charAt(i))){ //this means that there is something that is not a punct or letter
					return null; //return null
				}
			}
			//checking to see if it contains noise words, if so return null
			if (noiseWords.contains(word)) {
				return null;
			}

		return word;
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code


	}

	private boolean isAlpha(String name) {
		if(name.matches("[a-zA-Z]")) {
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 *
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		//finished

		if(occs.size()<=1) {
			return null;
		}
		ArrayList<Integer> ans = new ArrayList<Integer> ();
		Occurrence temp = occs.remove(occs.size() - 1);
		int low = 0;
		int high = occs.size()-1; //idx
		//binary search to find the right spot

		while(high>=low) {
			int mid = (low+high)/2;
			ans.add(mid);
			int frequency = occs.get(mid).frequency;
			//organize by frequency
			if(frequency == temp.frequency) {
				high=mid;
				low=high+1; //break the loop
			}
			else if(frequency < temp.frequency) {
				high = mid - 1;
			}
			else if (frequency > temp.frequency) {
				low = mid + 1; //freq is more than removed so mid+1
			}
		}

		occs.add(high + 1, temp); //index and then value
		return ans;
		// following line is a placeholder to make the program compile
		// you should modify it as needed when you write your code
		//return null;
	}

	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 *
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile)
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}

		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}

	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies.
	 *
	 * Note that a matching document will only appear once in the result.
	 *
	 * Ties in frequency values are broken in favor of the first keyword.
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same
	 * frequency f1, then doc1 will take precedence over doc2 in the result.
	 *
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 *
	 * See assignment description for examples
	 *
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches,
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		ArrayList<String> ans = new ArrayList<String>();
		ArrayList<Occurrence> oc1 = new ArrayList<Occurrence>();
		ArrayList<Occurrence> oc2 = new ArrayList<Occurrence>();
		ArrayList<Occurrence> oc3 = new ArrayList<Occurrence>();

		if(keywordsIndex.containsKey(kw1)) {
			oc1 = keywordsIndex.get(kw1);
		}

		if(keywordsIndex.containsKey(kw2)) {
			oc1 = keywordsIndex.get(kw2);
		}

		oc3.addAll(oc1);
		oc3.addAll(oc2);

		int count = 0;
		if (oc1 == null && oc2 == null) {
			return ans;
		}

		if(oc1==null && oc2!=null) {
			count =0;
			int x=0;

			while(x<oc2.size()&& (count<5)) {
				ans.add(oc2.get(x).document);
				x++;
				count++;
			}
		}

		if(oc1!=null && oc2==null) {
			count =0;
			int y=0;

			while(y<oc1.size()&& count<5) {
				ans.add(oc1.get(y).document);
				y++;
				count++;
			}
		}

		//create varibles
		int x = 0;
		int y = 0;


		if(!oc1.isEmpty()) { //if there are kw1
			if(!oc2.isEmpty()) { //if there are kw2
				count = 0;


			while ((x < oc1.size() || y < oc2.size()) && count < 5){

				if(oc1.get(x).frequency > oc2.get(y).frequency) {
					if(!ans.contains(oc1.get(x).document)) { //prevent duplicate
						ans.add(oc1.get(x).document);
						x++;
						count++;
						}
					}


				else if(oc1.get(x).frequency < oc2.get(y).frequency) {
					if(!ans.contains(oc2.get(y).document)) {
						ans.add(oc2.get(y).document);
						y++;
						count++;
						}
					}



				else {
					if (!ans.contains(oc1.get(x).document)){
						if(count<5){
						ans.add(oc1.get(x).document);
						count++;
						x++;
						}
					}
					else {
						x++;
					}


					if(!ans.contains(oc2.get(y).document)) {
						if(count<5) {
						ans.add(oc2.get(y).document);
							count++;
							y++;
						}
					}
					else {
						y++;
						}
					}

			}
		}
	}
		return ans;
	}

			}
