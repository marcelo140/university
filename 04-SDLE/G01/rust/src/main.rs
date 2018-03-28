extern crate petgraph;
extern crate rand;

use petgraph::{Graph, Undirected};
use petgraph::graph::NodeIndex;
use petgraph::algo::connected_components;
use petgraph::dot::{Dot, Config};

use rand::Rng;

const NODES: usize = 20;

fn main() {
    let mut graph = Graph::<usize, (), Undirected>::new_undirected();

    for n in 0..NODES {
        graph.add_node(n);
    }

    let indices: Vec<NodeIndex> = graph.node_indices().collect();
    let mut rnd = rand::thread_rng();

    while connected_components(&graph) > 1 {
        let a = rnd.choose(&indices).unwrap();
        let b = rnd.choose(&indices).unwrap();

        graph.update_edge(*a, *b, ());
    }

    println!("{:?}", Dot::with_config(&graph, &[Config::EdgeNoLabel]));
    // println!("{}", graph.edge_count());
}
