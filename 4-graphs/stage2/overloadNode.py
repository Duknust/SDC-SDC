import networkx as nx
import matplotlib.pyplot as plt
from random import randint

numNodes=100
count=0
nodesValue = []
G=nx.Graph()

for i in range(0,numNodes):
    G.add_node(i) #add nodes to graph
        nodesValue.append([i,1]) #add nodes to list with value 1
  count+=1

#print G.number_of_nodes()

while not nx.is_connected(G): #this will happen until every node is connected
  num = randint(0,count)
  num2 = randint(0,count)
  tmpSum=0
  for i in range(0,count): #nodes with high value are prefered
    tmpSum+=(nodesValue[i])[1]
    if tmpSum==num:
      num = i
      (nodesValue[i])[1]+=1
      break;
  tmpSum=0
  for i in range(0,count): #nodes with high value are prefered
    tmpSum+=(nodesValue[i])[1]
    if tmpSum==num2:
      num2 = i
      (nodesValue[i])[1]+=1
      break;   
  G.add_edge(num,num2) #chosen nodes are linked


print G.number_of_nodes()
print G.number_of_edges()

nx.draw(G)
#plt.show()
plt.savefig("path.png")
