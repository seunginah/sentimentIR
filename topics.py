import re # regex
from collections import defaultdict

class LatentTopic:
	"""this class is meant to take in a list of topics and store them into a dictionary"""
	def __init__(self, topiclist):
		self.dict = defaultdict(list) # dictionary of topics
		self.list = topiclist # take in list of topics

	def buildDict(self):
		name = 'topic' # let's just call the topics by number
		num = 1

		# decimal regex. -0.000 or 0.000 probability estimate
		dec = '[+-]?\d+(?:\.\d+)'
		# term regex "word"
		word = '"(.+?)"'
		quotes = '"'


		# for each entry of thelist
		for topic in self.list:
			key = name +str(num) # use this as key
			num = num +1

			# split by whitespace
			tokens = topic.split('+')

			# extract the probability estimates as well as the terms
			for token in tokens:
				prob = float(re.search(dec, token).group(0))
				term = str(re.search(word, token).group(0))
				term = re.sub(quotes, '', term)
				#print 'prob', prob,' term', term
				new_term = {prob:term}
				self.dict[key].append(new_term)



	def printDict(self):
		for key in sorted(self.dict.keys()):
			print 'key: ', key, 
			list = []
			for val in sorted(self.dict[key]):
				#list.append(val)
				for prob in val.keys():
					if prob >0:
						list.append(val)
			print '\n   val: ',list