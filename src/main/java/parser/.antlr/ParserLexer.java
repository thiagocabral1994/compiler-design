// Generated from /home/tcabral-pyyne/Desktop/compiler-design/src/main/java/parser/Parser.g4 by ANTLR 4.9.2

    package parser;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ParserLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		INTEGER_LITERAL=1, FLOAT_LITERAL=2, CHAR_LITERAL=3, TRUE_LITERAL=4, FALSE_LITERAL=5, 
		NULL_LITERAL=6, IDENTIFIER=7, TYPE_KEYWORD=8, DATA_KEYWORD=9, INT_KEYWORD=10, 
		CHAR_KEYWORD=11, FLOAT_KEYWORD=12, BOOL_KEYWORD=13, IF_KEYWORD=14, ELSE_KEYWORD=15, 
		ITERATE_KEYWORD=16, READ_KEYWORD=17, PRINT_KEYWORD=18, RETURN_KEYWORD=19, 
		NEW_KEYWORD=20, EQUALITY=21, INEQUALITY=22, GREATER_THAN_OR_EQUAL=23, 
		LESS_THAN_OR_EQUAL=24, GREATER_THAN=25, LESS_THAN=26, ASSIGNMENT=27, ADDITION=28, 
		SUBTRACTION=29, MULTIPLICATION=30, DIVISION=31, MODULUS=32, AND=33, OR=34, 
		NEGATION=35, OPEN_PARENTHESIS=36, CLOSE_PARENTHESIS=37, OPEN_BRACE=38, 
		CLOSE_BRACE=39, OPEN_BRACKET=40, CLOSE_BRACKET=41, COMMA=42, SEMICOLON=43, 
		TYPE_ASSIGNMENT=44, COLON=45, DOT=46, NEWLINE=47, WS=48, LINE_COMMENT=49, 
		COMMENT=50, CHAR=51;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"INTEGER_LITERAL", "FLOAT_LITERAL", "CHAR_LITERAL", "TRUE_LITERAL", "FALSE_LITERAL", 
			"NULL_LITERAL", "IDENTIFIER", "TYPE_KEYWORD", "DATA_KEYWORD", "INT_KEYWORD", 
			"CHAR_KEYWORD", "FLOAT_KEYWORD", "BOOL_KEYWORD", "IF_KEYWORD", "ELSE_KEYWORD", 
			"ITERATE_KEYWORD", "READ_KEYWORD", "PRINT_KEYWORD", "RETURN_KEYWORD", 
			"NEW_KEYWORD", "EQUALITY", "INEQUALITY", "GREATER_THAN_OR_EQUAL", "LESS_THAN_OR_EQUAL", 
			"GREATER_THAN", "LESS_THAN", "ASSIGNMENT", "ADDITION", "SUBTRACTION", 
			"MULTIPLICATION", "DIVISION", "MODULUS", "AND", "OR", "NEGATION", "OPEN_PARENTHESIS", 
			"CLOSE_PARENTHESIS", "OPEN_BRACE", "CLOSE_BRACE", "OPEN_BRACKET", "CLOSE_BRACKET", 
			"COMMA", "SEMICOLON", "TYPE_ASSIGNMENT", "COLON", "DOT", "NEWLINE", "WS", 
			"LINE_COMMENT", "COMMENT", "CHAR"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, "'true'", "'false'", "'null'", null, null, "'data'", 
			"'Int'", "'Char'", "'Float'", "'Bool'", "'if'", "'else'", "'iterate'", 
			"'read'", "'print'", "'return'", "'new'", "'=='", "'!='", "'>='", "'<='", 
			"'>'", "'<'", "'='", "'+'", "'-'", "'*'", "'/'", "'%'", "'&&'", "'||'", 
			"'!'", "'('", "')'", "'{'", "'}'", "'['", "']'", "','", "';'", "'::'", 
			"':'", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "INTEGER_LITERAL", "FLOAT_LITERAL", "CHAR_LITERAL", "TRUE_LITERAL", 
			"FALSE_LITERAL", "NULL_LITERAL", "IDENTIFIER", "TYPE_KEYWORD", "DATA_KEYWORD", 
			"INT_KEYWORD", "CHAR_KEYWORD", "FLOAT_KEYWORD", "BOOL_KEYWORD", "IF_KEYWORD", 
			"ELSE_KEYWORD", "ITERATE_KEYWORD", "READ_KEYWORD", "PRINT_KEYWORD", "RETURN_KEYWORD", 
			"NEW_KEYWORD", "EQUALITY", "INEQUALITY", "GREATER_THAN_OR_EQUAL", "LESS_THAN_OR_EQUAL", 
			"GREATER_THAN", "LESS_THAN", "ASSIGNMENT", "ADDITION", "SUBTRACTION", 
			"MULTIPLICATION", "DIVISION", "MODULUS", "AND", "OR", "NEGATION", "OPEN_PARENTHESIS", 
			"CLOSE_PARENTHESIS", "OPEN_BRACE", "CLOSE_BRACE", "OPEN_BRACKET", "CLOSE_BRACKET", 
			"COMMA", "SEMICOLON", "TYPE_ASSIGNMENT", "COLON", "DOT", "NEWLINE", "WS", 
			"LINE_COMMENT", "COMMENT", "CHAR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public ParserLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Parser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\65\u0152\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \4!\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t"+
		"+\4,\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64"+
		"\t\64\3\2\3\2\7\2l\n\2\f\2\16\2o\13\2\3\3\3\3\7\3s\n\3\f\3\16\3v\13\3"+
		"\3\3\3\3\7\3z\n\3\f\3\16\3}\13\3\3\3\3\3\3\3\7\3\u0082\n\3\f\3\16\3\u0085"+
		"\13\3\5\3\u0087\n\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\7\b\u009f\n\b\f\b\16\b\u00a2\13"+
		"\b\3\t\3\t\7\t\u00a6\n\t\f\t\16\t\u00a9\13\t\3\n\3\n\3\n\3\n\3\n\3\13"+
		"\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16"+
		"\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23"+
		"\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\26"+
		"\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\30\3\31\3\31\3\31\3\32\3\32\3\33"+
		"\3\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3\""+
		"\3#\3#\3#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3*\3*\3+\3+\3,\3,\3-\3"+
		"-\3-\3.\3.\3/\3/\3\60\5\60\u0126\n\60\3\60\3\60\3\60\3\60\3\61\6\61\u012d"+
		"\n\61\r\61\16\61\u012e\3\61\3\61\3\62\3\62\3\62\3\62\7\62\u0137\n\62\f"+
		"\62\16\62\u013a\13\62\3\62\3\62\3\62\3\62\3\63\3\63\3\63\3\63\7\63\u0144"+
		"\n\63\f\63\16\63\u0147\13\63\3\63\3\63\3\63\3\63\3\63\3\64\3\64\3\64\5"+
		"\64\u0151\n\64\3\u0145\2\65\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25"+
		"\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32"+
		"\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a"+
		"\62c\63e\64g\65\3\2\n\3\2\62;\3\2c|\6\2\62;C\\aac|\3\2C\\\4\2\13\13\""+
		"\"\4\2\f\f\17\17\5\2\13\f\17\17^^\t\2$$))^^ddppttvv\2\u015d\2\3\3\2\2"+
		"\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3"+
		"\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2"+
		"\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2"+
		"\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2"+
		"\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3"+
		"\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2"+
		"\2\2K\3\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2"+
		"W\3\2\2\2\2Y\3\2\2\2\2[\3\2\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3"+
		"\2\2\2\2e\3\2\2\2\2g\3\2\2\2\3i\3\2\2\2\5\u0086\3\2\2\2\7\u0088\3\2\2"+
		"\2\t\u008c\3\2\2\2\13\u0091\3\2\2\2\r\u0097\3\2\2\2\17\u009c\3\2\2\2\21"+
		"\u00a3\3\2\2\2\23\u00aa\3\2\2\2\25\u00af\3\2\2\2\27\u00b3\3\2\2\2\31\u00b8"+
		"\3\2\2\2\33\u00be\3\2\2\2\35\u00c3\3\2\2\2\37\u00c6\3\2\2\2!\u00cb\3\2"+
		"\2\2#\u00d3\3\2\2\2%\u00d8\3\2\2\2\'\u00de\3\2\2\2)\u00e5\3\2\2\2+\u00e9"+
		"\3\2\2\2-\u00ec\3\2\2\2/\u00ef\3\2\2\2\61\u00f2\3\2\2\2\63\u00f5\3\2\2"+
		"\2\65\u00f7\3\2\2\2\67\u00f9\3\2\2\29\u00fb\3\2\2\2;\u00fd\3\2\2\2=\u00ff"+
		"\3\2\2\2?\u0101\3\2\2\2A\u0103\3\2\2\2C\u0105\3\2\2\2E\u0108\3\2\2\2G"+
		"\u010b\3\2\2\2I\u010d\3\2\2\2K\u010f\3\2\2\2M\u0111\3\2\2\2O\u0113\3\2"+
		"\2\2Q\u0115\3\2\2\2S\u0117\3\2\2\2U\u0119\3\2\2\2W\u011b\3\2\2\2Y\u011d"+
		"\3\2\2\2[\u0120\3\2\2\2]\u0122\3\2\2\2_\u0125\3\2\2\2a\u012c\3\2\2\2c"+
		"\u0132\3\2\2\2e\u013f\3\2\2\2g\u0150\3\2\2\2im\t\2\2\2jl\t\2\2\2kj\3\2"+
		"\2\2lo\3\2\2\2mk\3\2\2\2mn\3\2\2\2n\4\3\2\2\2om\3\2\2\2pt\t\2\2\2qs\t"+
		"\2\2\2rq\3\2\2\2sv\3\2\2\2tr\3\2\2\2tu\3\2\2\2uw\3\2\2\2vt\3\2\2\2w{\7"+
		"\60\2\2xz\t\2\2\2yx\3\2\2\2z}\3\2\2\2{y\3\2\2\2{|\3\2\2\2|\u0087\3\2\2"+
		"\2}{\3\2\2\2~\177\7\60\2\2\177\u0083\t\2\2\2\u0080\u0082\t\2\2\2\u0081"+
		"\u0080\3\2\2\2\u0082\u0085\3\2\2\2\u0083\u0081\3\2\2\2\u0083\u0084\3\2"+
		"\2\2\u0084\u0087\3\2\2\2\u0085\u0083\3\2\2\2\u0086p\3\2\2\2\u0086~\3\2"+
		"\2\2\u0087\6\3\2\2\2\u0088\u0089\7)\2\2\u0089\u008a\5g\64\2\u008a\u008b"+
		"\7)\2\2\u008b\b\3\2\2\2\u008c\u008d\7v\2\2\u008d\u008e\7t\2\2\u008e\u008f"+
		"\7w\2\2\u008f\u0090\7g\2\2\u0090\n\3\2\2\2\u0091\u0092\7h\2\2\u0092\u0093"+
		"\7c\2\2\u0093\u0094\7n\2\2\u0094\u0095\7u\2\2\u0095\u0096\7g\2\2\u0096"+
		"\f\3\2\2\2\u0097\u0098\7p\2\2\u0098\u0099\7w\2\2\u0099\u009a\7n\2\2\u009a"+
		"\u009b\7n\2\2\u009b\16\3\2\2\2\u009c\u00a0\t\3\2\2\u009d\u009f\t\4\2\2"+
		"\u009e\u009d\3\2\2\2\u009f\u00a2\3\2\2\2\u00a0\u009e\3\2\2\2\u00a0\u00a1"+
		"\3\2\2\2\u00a1\20\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a3\u00a7\t\5\2\2\u00a4"+
		"\u00a6\t\4\2\2\u00a5\u00a4\3\2\2\2\u00a6\u00a9\3\2\2\2\u00a7\u00a5\3\2"+
		"\2\2\u00a7\u00a8\3\2\2\2\u00a8\22\3\2\2\2\u00a9\u00a7\3\2\2\2\u00aa\u00ab"+
		"\7f\2\2\u00ab\u00ac\7c\2\2\u00ac\u00ad\7v\2\2\u00ad\u00ae\7c\2\2\u00ae"+
		"\24\3\2\2\2\u00af\u00b0\7K\2\2\u00b0\u00b1\7p\2\2\u00b1\u00b2\7v\2\2\u00b2"+
		"\26\3\2\2\2\u00b3\u00b4\7E\2\2\u00b4\u00b5\7j\2\2\u00b5\u00b6\7c\2\2\u00b6"+
		"\u00b7\7t\2\2\u00b7\30\3\2\2\2\u00b8\u00b9\7H\2\2\u00b9\u00ba\7n\2\2\u00ba"+
		"\u00bb\7q\2\2\u00bb\u00bc\7c\2\2\u00bc\u00bd\7v\2\2\u00bd\32\3\2\2\2\u00be"+
		"\u00bf\7D\2\2\u00bf\u00c0\7q\2\2\u00c0\u00c1\7q\2\2\u00c1\u00c2\7n\2\2"+
		"\u00c2\34\3\2\2\2\u00c3\u00c4\7k\2\2\u00c4\u00c5\7h\2\2\u00c5\36\3\2\2"+
		"\2\u00c6\u00c7\7g\2\2\u00c7\u00c8\7n\2\2\u00c8\u00c9\7u\2\2\u00c9\u00ca"+
		"\7g\2\2\u00ca \3\2\2\2\u00cb\u00cc\7k\2\2\u00cc\u00cd\7v\2\2\u00cd\u00ce"+
		"\7g\2\2\u00ce\u00cf\7t\2\2\u00cf\u00d0\7c\2\2\u00d0\u00d1\7v\2\2\u00d1"+
		"\u00d2\7g\2\2\u00d2\"\3\2\2\2\u00d3\u00d4\7t\2\2\u00d4\u00d5\7g\2\2\u00d5"+
		"\u00d6\7c\2\2\u00d6\u00d7\7f\2\2\u00d7$\3\2\2\2\u00d8\u00d9\7r\2\2\u00d9"+
		"\u00da\7t\2\2\u00da\u00db\7k\2\2\u00db\u00dc\7p\2\2\u00dc\u00dd\7v\2\2"+
		"\u00dd&\3\2\2\2\u00de\u00df\7t\2\2\u00df\u00e0\7g\2\2\u00e0\u00e1\7v\2"+
		"\2\u00e1\u00e2\7w\2\2\u00e2\u00e3\7t\2\2\u00e3\u00e4\7p\2\2\u00e4(\3\2"+
		"\2\2\u00e5\u00e6\7p\2\2\u00e6\u00e7\7g\2\2\u00e7\u00e8\7y\2\2\u00e8*\3"+
		"\2\2\2\u00e9\u00ea\7?\2\2\u00ea\u00eb\7?\2\2\u00eb,\3\2\2\2\u00ec\u00ed"+
		"\7#\2\2\u00ed\u00ee\7?\2\2\u00ee.\3\2\2\2\u00ef\u00f0\7@\2\2\u00f0\u00f1"+
		"\7?\2\2\u00f1\60\3\2\2\2\u00f2\u00f3\7>\2\2\u00f3\u00f4\7?\2\2\u00f4\62"+
		"\3\2\2\2\u00f5\u00f6\7@\2\2\u00f6\64\3\2\2\2\u00f7\u00f8\7>\2\2\u00f8"+
		"\66\3\2\2\2\u00f9\u00fa\7?\2\2\u00fa8\3\2\2\2\u00fb\u00fc\7-\2\2\u00fc"+
		":\3\2\2\2\u00fd\u00fe\7/\2\2\u00fe<\3\2\2\2\u00ff\u0100\7,\2\2\u0100>"+
		"\3\2\2\2\u0101\u0102\7\61\2\2\u0102@\3\2\2\2\u0103\u0104\7\'\2\2\u0104"+
		"B\3\2\2\2\u0105\u0106\7(\2\2\u0106\u0107\7(\2\2\u0107D\3\2\2\2\u0108\u0109"+
		"\7~\2\2\u0109\u010a\7~\2\2\u010aF\3\2\2\2\u010b\u010c\7#\2\2\u010cH\3"+
		"\2\2\2\u010d\u010e\7*\2\2\u010eJ\3\2\2\2\u010f\u0110\7+\2\2\u0110L\3\2"+
		"\2\2\u0111\u0112\7}\2\2\u0112N\3\2\2\2\u0113\u0114\7\177\2\2\u0114P\3"+
		"\2\2\2\u0115\u0116\7]\2\2\u0116R\3\2\2\2\u0117\u0118\7_\2\2\u0118T\3\2"+
		"\2\2\u0119\u011a\7.\2\2\u011aV\3\2\2\2\u011b\u011c\7=\2\2\u011cX\3\2\2"+
		"\2\u011d\u011e\7<\2\2\u011e\u011f\7<\2\2\u011fZ\3\2\2\2\u0120\u0121\7"+
		"<\2\2\u0121\\\3\2\2\2\u0122\u0123\7\60\2\2\u0123^\3\2\2\2\u0124\u0126"+
		"\7\17\2\2\u0125\u0124\3\2\2\2\u0125\u0126\3\2\2\2\u0126\u0127\3\2\2\2"+
		"\u0127\u0128\7\f\2\2\u0128\u0129\3\2\2\2\u0129\u012a\b\60\2\2\u012a`\3"+
		"\2\2\2\u012b\u012d\t\6\2\2\u012c\u012b\3\2\2\2\u012d\u012e\3\2\2\2\u012e"+
		"\u012c\3\2\2\2\u012e\u012f\3\2\2\2\u012f\u0130\3\2\2\2\u0130\u0131\b\61"+
		"\2\2\u0131b\3\2\2\2\u0132\u0133\7/\2\2\u0133\u0134\7/\2\2\u0134\u0138"+
		"\3\2\2\2\u0135\u0137\n\7\2\2\u0136\u0135\3\2\2\2\u0137\u013a\3\2\2\2\u0138"+
		"\u0136\3\2\2\2\u0138\u0139\3\2\2\2\u0139\u013b\3\2\2\2\u013a\u0138\3\2"+
		"\2\2\u013b\u013c\5_\60\2\u013c\u013d\3\2\2\2\u013d\u013e\b\62\2\2\u013e"+
		"d\3\2\2\2\u013f\u0140\7}\2\2\u0140\u0141\7/\2\2\u0141\u0145\3\2\2\2\u0142"+
		"\u0144\13\2\2\2\u0143\u0142\3\2\2\2\u0144\u0147\3\2\2\2\u0145\u0146\3"+
		"\2\2\2\u0145\u0143\3\2\2\2\u0146\u0148\3\2\2\2\u0147\u0145\3\2\2\2\u0148"+
		"\u0149\7/\2\2\u0149\u014a\7\177\2\2\u014a\u014b\3\2\2\2\u014b\u014c\b"+
		"\63\2\2\u014cf\3\2\2\2\u014d\u0151\n\b\2\2\u014e\u014f\7^\2\2\u014f\u0151"+
		"\t\t\2\2\u0150\u014d\3\2\2\2\u0150\u014e\3\2\2\2\u0151h\3\2\2\2\17\2m"+
		"t{\u0083\u0086\u00a0\u00a7\u0125\u012e\u0138\u0145\u0150\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}