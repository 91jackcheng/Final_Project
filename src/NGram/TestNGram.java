package NGram;

import org.ansj.lucene4.AnsjAnalysis;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.tokenattributes.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
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
 * lucene 4.x 使用N-Gram模型和Edge-NGram模型分词器实例。
 *
 * @author yunshouhu
 *         常用统计语言模型，包括了N元文法模型（N-gram Model）、隐马尔科夫模型（Hidden Markov Model，简称HMM）、最大熵模型（Maximum Entropy Model）。
 *         N-Gram这是一种依赖于上下文环境的词的概率分布的统计计算语言模型。
 *         假定，在一个语句中第i个词出现的概率，条件依赖于它前面的N-1个词，即将一个词的上下文定义为该词前面出现的N-1个词，
 *         这样的语言模型叫做N-gram模型（N元文法统计模型）。公式如下：
 */
public class TestNGram {
	public TestNGram() throws IOException {
		String s = "dd add addd adddd 编码规范从根本上解决了程序维护员的难题；规范的编码阅读和理解起来更容易，也可以快速的不费力气的借鉴别人的编码。对将来维护你编码的人来说，你的编码越优化，他们就越喜欢你的编码，理解起来也就越快。";
		System.out.println(s);
		StringReader sr = new StringReader(s);
		//N-gram模型分词器
		Tokenizer tokenizer = new NGramTokenizer(1, 3);
		tokenizer.setReader(sr);
		Analyzer analyzer = new AnsjAnalysis();
		//Edge-NGram 边缘模型，范围模型分词器
		//Tokenizer tokenizer=new EdgeNGramTokenizer(Version.LUCENE_46, sr, 1, 10);
		//Tokenizer tokenizer=new Lucene43NGramTokenizer(sr);
		//Tokenizer tokenizer=new Lucene43EdgeNGramTokenizer(Version.LUCENE_46, sr, 1, 10);
		//testtokenizer(tokenizer);
		//Lucene Document的域名
		String fieldName = "text";
		//检索内容
		String text = "循环链表报数问题,n个人编号分别为1,2,3,……n，从第k个编号开始数1,2到m个然后删除第m个人，然后下一个人再从1开始数数到第m个，然后再删除m人";
		//实例化IKAnalyzer分词器
		Directory directory = null;
		IndexWriter iwriter = null;
		IndexReader ireader = null;
		IndexSearcher isearcher = null;
		try {

			//建立内存索引对象

			directory = new RAMDirectory();

			//配置IndexWriterConfig

			IndexWriterConfig iwConfig = new IndexWriterConfig(analyzer);

			iwConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

			iwriter = new IndexWriter(directory, iwConfig);

			//写入索引

			Document doc = new Document();

			doc.add(new StringField("ID", "10000", Field.Store.YES));

			doc.add(new TextField(fieldName, text, Field.Store.YES));

			iwriter.addDocument(doc);

			iwriter.close();


			//搜索过程**********************************

			//实例化搜索器

			ireader = DirectoryReader.open(directory);

			isearcher = new IndexSearcher(ireader);

			String keyword = "循环链表";

			//使用QueryParser查询分析器构造Query对象

			QueryParser qp = new QueryParser(fieldName, analyzer);

			qp.setDefaultOperator(QueryParser.AND_OPERATOR);

			Query query = qp.parse(keyword);

			System.out.println("Query = " + query);


			//搜索相似度最高的5条记录

			TopDocs topDocs = isearcher.search(query, 5);

			System.out.println("命中：" + topDocs.totalHits);

			//输出结果

			ScoreDoc[] scoreDocs = topDocs.scoreDocs;

			for (int i = 0; i < topDocs.totalHits; i++) {

				Document targetDoc = isearcher.doc(scoreDocs[i].doc);

				System.out.println("内容：" + targetDoc.toString());

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

	private static void testtokenizer(Tokenizer tokenizer) {

		try {
			/*
			Iterator<Class<? extends Attribute>> iterator = tokenizer
					.getAttributeClassesIterator();
			while (iterator.hasNext()) {
				Class<? extends Attribute> attrClass = iterator.next();
				System.out.println(attrClass.getSimpleName());
			}*/
			tokenizer.reset();
			while (tokenizer.incrementToken()) {
				//CharTermAttribute
				//TermToBytesRefAttribute
				//PositionIncrementAttribute
				//PositionLengthAttribute
				//OffsetAttribute
				CharTermAttribute charTermAttribute = tokenizer.addAttribute(CharTermAttribute.class);
				TermToBytesRefAttribute termToBytesRefAttribute = tokenizer.addAttribute(TermToBytesRefAttribute.class);
				PositionIncrementAttribute positionIncrementAttribute = tokenizer.addAttribute(PositionIncrementAttribute.class);
				PositionLengthAttribute positionLengthAttribute = tokenizer.addAttribute(PositionLengthAttribute.class);
				OffsetAttribute offsetAttribute = tokenizer.addAttribute(OffsetAttribute.class);
				TypeAttribute typeAttribute = tokenizer.addAttribute(TypeAttribute.class);
				//System.out.println(attribute.toString());
				System.out.println("term=" + charTermAttribute.toString() + "," + offsetAttribute.startOffset() + "-" + offsetAttribute.endOffset()
						+ ",type=" + typeAttribute.type() + ",PositionIncrement=" + positionIncrementAttribute.getPositionIncrement()
						+ ",PositionLength=" + positionLengthAttribute.getPositionLength());

			}
			tokenizer.end();
			tokenizer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}