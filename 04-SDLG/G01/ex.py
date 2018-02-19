import matplotlib.pyplot as plt
import networkx as nx
import random

G = nx.Graph()
G.add_nodes_from(range(20))

while(not nx.is_connected(G)):
    [x,y] = random.sample(G.nodes, 2)
    G.add_edge(x, y)

nx.draw(G)
plt.savefig("graph.png")
