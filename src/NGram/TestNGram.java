package NGram;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenizer;
import org.apache.lucene.analysis.ngram.Lucene43EdgeNGramTokenizer;
import org.apache.lucene.analysis.ngram.Lucene43NGramTokenizer;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionLengthAttribute;
import org.apache.lucene.analysis.tokenattributes.TermToBytesRefAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.Attribute;
import org.apache.lucene.util.Version;
/**
 * lucene 4.x 使用N-Gram模型和Edge-NGram模型分词器实例。
 * @author yunshouhu
 * 常用统计语言模型，包括了N元文法模型（N-gram Model）、隐马尔科夫模型（Hidden Markov Model，简称HMM）、最大熵模型（Maximum Entropy Model）。
 *	N-Gram这是一种依赖于上下文环境的词的概率分布的统计计算语言模型。
 *  假定，在一个语句中第i个词出现的概率，条件依赖于它前面的N-1个词，即将一个词的上下文定义为该词前面出现的N-1个词，
 *  这样的语言模型叫做N-gram模型（N元文法统计模型）。公式如下：
 */
public class TestNGram {
	public TestNGram() {
		String s = "dd add addd adddd 编码规范从根本上解决了程序维护员的难题；规范的编码阅读和理解起来更容易，也可以快速的不费力气的借鉴别人的编码。对将来维护你编码的人来说，你的编码越优化，他们就越喜欢你的编码，理解起来也就越快。";
		StringReader sr = new StringReader(s);
		//N-gram模型分词器
		Tokenizer tokenizer = new NGramTokenizer(Version.LUCENE_4_10_2,sr);
		//Edge-NGram 边缘模型，范围模型分词器
		//Tokenizer tokenizer=new EdgeNGramTokenizer(Version.LUCENE_46, sr, 1, 10);
		//Tokenizer tokenizer=new Lucene43NGramTokenizer(sr);
		//Tokenizer tokenizer=new Lucene43EdgeNGramTokenizer(Version.LUCENE_46, sr, 1, 10);
		System.out.println("yes");
		testtokenizer(tokenizer);
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
			while(tokenizer.incrementToken())
			{
				//CharTermAttribute
				//TermToBytesRefAttribute
				//PositionIncrementAttribute
				//PositionLengthAttribute
				//OffsetAttribute
				CharTermAttribute charTermAttribute=tokenizer.addAttribute(CharTermAttribute.class);
				TermToBytesRefAttribute termToBytesRefAttribute=tokenizer.addAttribute(TermToBytesRefAttribute.class);
				PositionIncrementAttribute positionIncrementAttribute=tokenizer.addAttribute(PositionIncrementAttribute.class);
				PositionLengthAttribute positionLengthAttribute=tokenizer.addAttribute(PositionLengthAttribute.class);
				OffsetAttribute offsetAttribute=tokenizer.addAttribute(OffsetAttribute.class);
				TypeAttribute typeAttribute = tokenizer.addAttribute(TypeAttribute.class);
				//System.out.println(attribute.toString());
				System.out.println("term="+charTermAttribute.toString()+","+offsetAttribute.startOffset()+"-"+offsetAttribute.endOffset()
						+",type="+typeAttribute.type()+",PositionIncrement="+positionIncrementAttribute.getPositionIncrement()
						+",PositionLength="+positionLengthAttribute.getPositionLength());

			}
			tokenizer.end();
			tokenizer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}