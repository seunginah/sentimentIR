from pprint import pprint # pretty printer
import gensim as gs

# tutorial sample documents
docs = ["Human machine interface for lab abc computer applications",
              "A survey of user opinion of computer system response time",
              "The EPS user interface management system",
              "System and human system engineering testing of EPS",
              "Relation of user perceived response time to error measurement",
              "The generation of random binary unordered trees",
              "The intersection graph of paths in trees",
              "Graph minors IV Widths of trees and well quasi ordering",
              "Graph minors A survey"]


# stoplist removal, tokenization
stoplist = set('for a of the and to in'.split())
# for each document: lowercase document, split by whitespace, and add all its words not in stoplist to texts
texts = [[word for word in doc.lower().split() if word not in stoplist] for doc in docs]


# create dict
dict = gs.corpora.Dictionary(texts)
# create corpus
corpus = [dict.doc2bow(text) for text in texts]

# tf-idf
tfidf = gs.models.TfidfModel(corpus)
corpus_tfidf = tfidf[corpus]

# latent semantic indexing with 10 topics
lsi = gs.models.LsiModel(corpus_tfidf, id2word=dict, num_topics =10)

for i in lsi.print_topics():
	print i
