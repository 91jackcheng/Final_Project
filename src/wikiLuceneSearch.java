import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

/**
 * Created by 万只羊 on 2015/12/16.
 */
public class wikiLuceneSearch {
	static final String indexPath = "./QA/wikiIndex";
	static final String filePath = "./QA/wiki";
	static final String fieldName = "content";
	static Analyzer analyzer = new WhitespaceAnalyzer();
	static IndexSearcher indexSearcher;

	wikiLuceneSearch() {
		openIndex();
	}

	static void search(String keyword) {
		QueryParser qp = new QueryParser(Version.LATEST, fieldName, analyzer);
		qp.setDefaultOperator(QueryParser.OR_OPERATOR);
		Query query = null;
		try {
			query = qp.parse(keyword);
		} catch (ParseException e) {
			System.out.println(keyword + " parse fail");
		}
		System.out.println("Query = " + query);
		TopDocs topDocs = null;
		try {
			topDocs = indexSearcher.search(query, 5);
		} catch (IOException e) {
			System.out.println(keyword + " search fail");
		}
		System.out.println("命中：" + topDocs.totalHits);
		//输出结果
		ScoreDoc[] scoreDocs = topDocs.scoreDocs;
		for (int i = 0; i < 5; i++) {
			Document targetDoc = null;
			try {
				targetDoc = indexSearcher.doc(scoreDocs[i].doc);
			} catch (IOException e) {
				System.out.println(keyword + "get doc fail");
			}
			System.out.println("内容：" + targetDoc.toString() + scoreDocs[i].score);
		}
	}

	static void openIndex() {
		Directory directory = null;
		DirectoryReader ireader = null;
		try {
			directory = FSDirectory.open(new File(indexPath));
			ireader = DirectoryReader.open(directory);
		} catch (IOException e) {
			e.printStackTrace();
		}
		indexSearcher = new IndexSearcher(ireader);
	}

	static void buildIndex() {
		Directory directory = null;
		try {
			directory = FSDirectory.open(new File(indexPath));
		} catch (IOException e) {
			System.out.println("open indexPath fail");
		}
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
		IndexWriter indexWriter = null;
		try {
			indexWriter = new IndexWriter(directory, config);
		} catch (IOException e) {
			System.out.println("config fail");
		}
		Date date1 = new Date();
		File[] files = new File(filePath).listFiles();
		for (int i = 0; i < files.length; i++) {
			FileReader file = null;
			try {
				file = new FileReader(files[i]);
			} catch (FileNotFoundException e) {
				System.out.println("read file" + files[i].getName() + "fail");
			}


			Document document = new Document();
			document.add(new TextField(fieldName, file));
			document.add(new StringField("filename", files[i].getName(), Field.Store.YES));
			try {
				indexWriter.addDocument(document);
			} catch (IOException e) {
				System.out.println(files[i].getName() + "add fail");
			}
		}
		try {
			indexWriter.close();
		} catch (IOException e) {
			System.out.println("index writer close fail");
		}
	}

	public static void main(String... args) {
		//wikiLuceneSearch.buildIndex();
		wikiLuceneSearch.openIndex();
		wikiLuceneSearch.search("中国 皇帝 溥仪");
		wikiLuceneSearch.search("历史 事件 戚 继 光 抗 倭 发生 在 哪个 朝代 ");
		wikiLuceneSearch.search("阿根廷 国家 足球队 赢 得 过 多少 次 美洲 杯 冠军");
		wikiLuceneSearch.search("平均 人口 密度 最高 的 大 洲 是");
	}
}
