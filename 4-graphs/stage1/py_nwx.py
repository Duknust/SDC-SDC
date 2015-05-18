import networkx as nx
import matplotlib.pyplot as plt
from random import randint

G=nx.Graph()

for i in range(0,10):
  G.add_node(i)

print G.number_of_nodes()

'''
for i in range(0,10):
  num = randint(0,10)
  print "num "+str(num)
  if len(G.neighbors(i))==0:
    G.add_edge(i,num)
  else:
    i=i-1


for i in range(0,1000):
  if len(G.neighbors(i))<2:
    num = randint(0,1000)
    G.add_edge(i,num)
'''

while not nx.is_connected(G):
  num = randint(0,10)
  num2 = randint(0,10)
  G.add_edge(num,num2)

print G.number_of_nodes()
print G.number_of_edges()

nx.draw(G)
#plt.show()
plt.savefig("path.png")
