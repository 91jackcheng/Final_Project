package test;

import org.ansj.lucene4.AnsjAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.StringReader;


/**
 * 使用IKAnalyzer进行Lucene索引和查询的演示
 * 2012-3-2
 * <p>
 * 以下是结合Lucene4.0 API的写法
 */
public class LuceneIndexAndSearchDemo {

	public static void main(String[] args) throws Exception {
		LuceneIndexAndSearchDemo demo = new LuceneIndexAndSearchDemo();
		String s = "中新网3月12日电 据中国政府网消息，3月12日上午10时15分，李克强总理参加完政协闭幕会后来到国务院应急指挥中心，与前方中国搜救船长通话，了解马航MH370失联客机搜救最新进展情况。李克强要求各有关部门调集一切可能力量，加大搜救密度和力度，不放弃任何一线希望。";
				//demo.position(s);
		//List<Term> parse = IndexAnalysis.parse("主副食品");
		//parse = IndexAnalysis.parse("中国政府网");
    //System.out.println(parse);
		demo.demo(s);
		demo.test();
	}
	void demo(String s) throws IOException, ParseException {
		Analyzer analyzer = new AnsjAnalysis();

    // Store the index in memory:
    Directory directory = new RAMDirectory();
    // To store an index on disk, use this instead:
    //Directory directory = FSDirectory.open("/tmp/testindex");
    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_CURRENT, analyzer);
    IndexWriter iwriter = new IndexWriter(directory, config);
    Document doc = new Document();
    String text = "This is the text to be indexed.";
    doc.add(new Field("fieldname", text, TextField.TYPE_STORED));
    iwriter.addDocument(doc);
    iwriter.close();

    // Now search the index:
    DirectoryReader ireader = DirectoryReader.open(directory);
    IndexSearcher isearcher = new IndexSearcher(ireader);
    // Parse a simple query that searches for "text":
    QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, "fieldname", analyzer);
    Query query = parser.parse("shabi");
    ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
    // Iterate through the results:
    for (int i = 0; i < hits.length; i++) {
      Document hitDoc = isearcher.doc(hits[i].doc);
      System.out.println("This is the text to be indexed." + hitDoc.get("fieldname"));
    }
    ireader.close();
    directory.close();
	}
	/**
	 * 模拟：
	 * 创建一个单条记录的索引，并对其进行搜索
	 *
	 * @param
	 */
	void test() {
		//Lucene Document的域名
		String fieldName = "text";
		//检索内容
		String text = "IK Analyzer是一个结合词典分词和文法分词的中文分词开源工具包。它使用了全新的正向迭代最细粒度切分算法。";
		String[] slist = new String[]{
			"我 爱 中国",
				"我爱 中国 的 筷子",
				"我爱 中国的 美食",
		};

		//实例化IKAnalyzer分词器
		Analyzer analyzer = new AnsjAnalysis(true);
		analyzer = new WhitespaceAnalyzer();

		Directory directory = null;
		IndexWriter iwriter = null;
		IndexReader ireader = null;
		IndexSearcher isearcher = null;
		try {
			//建立内存索引对象
			directory = new RAMDirectory();

			//配置IndexWriterConfig
			IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_40, analyzer);
			iwConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			iwriter = new IndexWriter(directory, iwConfig);
			//写入索引
			for (int i = 0; i < slist.length; i++) {
				text = slist[i];
				Document doc = new Document();
				doc.add(new StringField("ID", "10000", Field.Store.YES));
				doc.add(new TextField(fieldName, text, Field.Store.YES));
				iwriter.addDocument(doc);
			}
			iwriter.close();


			//搜索过程**********************************
			//实例化搜索器
			ireader = DirectoryReader.open(directory);
			isearcher = new IndexSearcher(ireader);

			String keyword = "我 爱 中国 北京";
			//使用QueryParser查询分析器构造Query对象
			QueryParser qp = new QueryParser(Version.LATEST, fieldName, analyzer);
			qp.setDefaultOperator(QueryParser.OR_OPERATOR);
			Query query = qp.parse(keyword);
			System.out.println("Query = " + query);

			//搜索相似度最高的5条记录
			TopDocs topDocs = isearcher.search(query, 5);
			System.out.println("命中：" + topDocs.totalHits);
			//输出结果
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (int i = 0; i < topDocs.totalHits; i++) {
				Document targetDoc = isearcher.doc(scoreDocs[i].doc);
				System.out.println("内容：" + targetDoc.toString() + scoreDocs[i].score);
			}

		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (ireader != null) {
				try {
					ireader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (directory != null) {
				try {
					directory.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void position(String word) throws Exception {

		Analyzer analyzer = new AnsjAnalysis();//IK分词
		TokenStream token = analyzer.tokenStream("a", new StringReader(word));
		token.reset();
		CharTermAttribute term = token.addAttribute(CharTermAttribute.class);//term信息
		OffsetAttribute offset = token.addAttribute(OffsetAttribute.class);//位置数据
		while (token.incrementToken()) {
			System.out.println(term + "   " + offset.startOffset() + "   " + offset.endOffset());
		}
		token.end();
		token.close();
	}
}