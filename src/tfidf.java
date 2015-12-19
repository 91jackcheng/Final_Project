import org.ansj.lucene4.AnsjAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;

import java.util.ArrayList;
import java.util.List;

/**
 * 利用lucene获取tf-idf
 *
 * @author yong.chen
 *
 */
public class tfidf {
	Directory directory = new RAMDirectory();

	public void index(List<String> docs) {
		try {
			Analyzer analyzer = new AnsjAnalysis(true);
			//Directory directory = FSDirectory.open(new File(INDEX_PATH));
			IndexWriterConfig config = new IndexWriterConfig(
					Version.LATEST, analyzer);
			IndexWriter iwriter = new IndexWriter(directory, config);
			FieldType ft = new FieldType();
			ft.setIndexed(true);// 存储
			ft.setStored(true);// 索引
			ft.setStoreTermVectors(true);
			ft.setTokenized(true);
			ft.setStoreTermVectorPositions(true);// 存储位置
			ft.setStoreTermVectorOffsets(true);// 存储偏移量

			for (String piece: docs) {
				Document doc = new Document();
				doc.add(new Field("text", piece, ft));
				iwriter.addDocument(doc);
			}


			//iwriter.forceMerge(1);// 最后一定要合并为一个segment，不然无法计算idf
			iwriter.close();
		} catch (Exception e) {

		}
	}

	/**
	 * 读取索引，显示词频
	 *
	 * **/
	public void getTF() {
		try {
			//Directory directroy = FSDirectory.open(new File(
					//INDEX_PATH));
			IndexReader reader = DirectoryReader.open(directory);
			for (int i = 0; i < reader.numDocs(); i++) {
				int docId = i;
				System.out.println("第" + (i + 1) + "篇文档：");
				Terms terms = reader.getTermVector(docId, "text");
				if (terms == null)
					continue;
				TermsEnum termsEnum = terms.iterator(null);
				BytesRef thisTerm = null;
				while ((thisTerm = termsEnum.next()) != null) {
					String termText = thisTerm.utf8ToString();
					DocsEnum docsEnum = termsEnum.docs(null, null);
					while ((docsEnum.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
						System.out.println("termText:" + termText + " TF:  "
								+ 1.0 * docsEnum.freq() / terms.size());
					}
				}
			}
			reader.close();
			//directory.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 计算IDF
	 *
	 * *
	 */
	public void getIDF() {
		try {
			IndexReader reader = DirectoryReader.open(directory);
			List<AtomicReaderContext> list = reader.leaves();
			System.out.println("文档总数 : " + reader.maxDoc());
			for (AtomicReaderContext ar : list) {
				System.out.println(1234);
				String field = "text";
				AtomicReader areader = ar.reader();
				Terms terms = areader.terms(field);
				TermsEnum tn = terms.iterator(null);
				BytesRef text;
				while ((text = tn.next()) != null) {
					System.out.println("field=" + field + "; text="
									+ text.utf8ToString() + "   IDF : "
									+ Math.log10(reader.maxDoc() * 1.0 / tn.docFreq())
							 + " 全局词频 :  " + tn.totalTermFreq()
					);
				}
			}
			reader.close();
			directory.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	tfidf(List<String> docs) {
		index(docs);
		//getTF();
		getIDF();
	}

	public static void main(String[] args) {
		List<String> docs = new ArrayList<>();
		docs.add("我是中国人");
		docs.add("我爱中国");
		tfidf test = new tfidf(docs);
	}
}