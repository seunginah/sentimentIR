import re # regex

class Dict:
	def __init__(self):
		self.dict = {}
		self.topwords = []

	def addWord(self, word):
		if word in self.dict:
			self.dict[word] = self.dict[word]+1
		else:
			self.dict[word] = 1

	def getTopNWords(self, n):
		largest = []
		[largest.append(self.dict[words]) for words in self.dict.keys()]

		topfreqs = sorted(largest)[-n-1:-1]
		print topfreqs
		
		for word in self.dict.keys():
			if self.dict[word] in topfreqs:
				self.topwords.append(word)

		print self.topwords




class ParseDocs:
	"""parse all docs in a textfile indiscriminately"""
	def __init__(self, filename):
		self.file = open(filename, "r")
		self.docs = []
		self.dict = Dict()
		self.topwords = []

	def parse(self):
		with self.file as txt:
			#[self.docs.append(line.strip()) for line in txt]
			for line in txt:
				self.docs.append(line.strip())
				for token in line.split():
					self.dict.addWord(token)
		self.dict.getTopNWords(100)
		self.topwords = self.dict.topwords

class ParsePolarDocs:
	"""parse out negative or positive docs"""
	def __init__(self, filename):
		self.file = open(filename, "r")
		self.posdocs = []
		self.negdocs = []

	def parse(self):
		start = '\d+\s\d+\s' #regex for the beginning ofa doc (docID_rating_)
		# only collect positive docs
		with self.file as txt:
			for line in txt:
				# match the regex for the docID and rating, then extract rating
				nums = str(re.match(start, line).group(0))
				rating = int(nums[-2:-1])
				if rating is 0:
					self.negdocs.append(line.strip())
				elif rating is 1:
					self.posdocs.append(line.strip())
				else:
					print 'WARNING'


