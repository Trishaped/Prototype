package fr.umlv.waterfowl.dictionary;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

public class Dictionary {
	
	private static Dictionary instance = null;
	private String filename ;
	private static BiMap<String,Long> dictS=null,dictP=null,dictO=null;
	private static Map<Long, Long> statS = new HashMap<Long,Long>();
	private static Map<Long, Long> statP = new HashMap<Long,Long>();
	private static Map<Long, Long> statO = new HashMap<Long,Long>();
	private Map<String, Long> tmpDictS = new HashMap<String, Long>();
	private Map<String, Long> tmpDictP = new HashMap<String, Long>();
	private Map<String, Long> tmpDictO = new HashMap<String, Long>();
	private InputStream in;
	private static final String EXT = "wdk";
	
	private Dictionary(String filename) {
		this.filename = filename;		
	}
	public static Dictionary getInstance(String filename) {
		if(instance==null)
				instance = new Dictionary(filename);
		return instance;
	}
	
	public String getFilename() {
		return filename;
	}
	public void openFile() {
		try {
			System.out.println(filename);
			in = new FileInputStream(filename+"."+EXT); 
		} catch ( FileNotFoundException ex) {
			System.out.println("File: "+filename+" not found " + ex.getMessage());
		}
	}
	public void load() {
		BufferedReader br = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
		String line;
		int stage = 0;
		try {
			while ((line = br.readLine()) != null) {
					if ("-- predicate".equals(line))
						stage=1;
					else if("-- object".equals(line))
						stage=2;
					else {
						switch(stage) {
						case 0: addToDictionary(tmpDictS, line); addToStat(statS, line); break; 
						case 1: addToDictionary(tmpDictO, line); addToStat(statO, line);break;
						case 2: addToDictionary(tmpDictP, line); addToStat(statP, line);break;
						}
					}
			}
			dictS = ImmutableBiMap.copyOf(Collections.unmodifiableMap(tmpDictS));
			dictP = ImmutableBiMap.copyOf(Collections.unmodifiableMap(tmpDictP));
			dictO = ImmutableBiMap.copyOf(Collections.unmodifiableMap(tmpDictO));
			System.out.println("size dictS "+dictS.size());
			System.out.println("size dictP "+dictP.size());
			System.out.println("size dictO "+dictO.size());
		} catch(IOException ex) {
			System.out.println("Erreur à la lecture de "+filename);
		}
		System.out.println("done");
	}
	public void clear() {
		dictS=null;
		dictP=null;
		dictO=null;
		try {
		statS.clear();
		statP.clear();
		statO.clear();
		tmpDictS.clear();
		tmpDictP.clear();
		tmpDictO.clear();
		} catch(UnsupportedOperationException e) {
			System.out.println("Can not clear "+e.getMessage());
		}
		instance = null;
		System.out.println("cleared");
	}
	public Long getPredicateValue(String url) {
		return (dictP.containsKey(url.trim()) ? dictP.get(url) : -1);
	}
	public String getPredicateKey(Long id) {
		return (dictP.containsValue(id) ? dictP.inverse().get(id) : null);
	}
	
	public Long getSubjectValue(String url) {
		return (dictS.containsKey(url.trim()) ? dictS.get(url) : -1);
	}
	public String getSubjectKey(Long id) {
		return (dictS.containsValue(id) ? dictS.inverse().get(id) : null);
	}
	
	public Long getObjectValue(String url) {
		return (dictO.containsKey(url.trim()) ? dictO.get(url) : -1);
	}
	public String getObjectKey(Long id) {
		return (dictO.containsValue(id) ? dictO.inverse().get(id) : null);
	}
	public Long getStatS(String key) {
		try {
			Long val = Long.parseLong(key);
			return statS.containsKey(val) ? statS.get(val) : -1;
		}catch (NumberFormatException e) {
			return new Long(-1);
		}
	}
	public Long getStatP(String key) {
		try {
			Long val = Long.parseLong(key);
			return statP.containsKey(val) ? statP.get(val) : -1;
		}catch (NumberFormatException e) {
			return new Long(-1);
		}
	}
	public Long getStatO(String key) {
		try {
			Long val = Long.parseLong(key);
			return statO.containsKey(val) ? statO.get(val) : -1;
		}catch (NumberFormatException e) {
			return new Long(-1);
		}
	}
	public static Map<Long, Long> getStatP() {
		return statP;
	}
	public static Map<Long, Long> getStatO() {
		return statO;
	}
	public void addToDictionary(Map<String, Long> dict, String line) {
		String tmp[] = line.split("\t");
		dict.put(tmp[0].trim(), Long.parseLong(tmp[1]));
	}
	public void addToStat(Map<Long, Long> stat, String line) {
		String tmp[] = line.split("\t");
		stat.put(Long.parseLong(tmp[1]),Long.parseLong(tmp[2]));
	}
}
